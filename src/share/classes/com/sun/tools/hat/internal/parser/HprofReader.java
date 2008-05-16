/*
 * Copyright 1997-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */


/*
 * The Original Code is HAT. The Initial Developer of the
 * Original Code is Bill Foote, with contributions from others
 * at JavaSoft/Sun.
 */

package com.sun.tools.hat.internal.parser;

import java.io.*;
import java.util.Date;
import java.util.Hashtable;
import com.sun.tools.hat.internal.model.ArrayTypeCodes;
import com.sun.tools.hat.internal.model.*;

/**
 * Object that's used to read a hprof file.
 *
 * @author      Bill Foote
 */

public class HprofReader extends Reader /* imports */ implements ArrayTypeCodes {

    final static int MAGIC_NUMBER = 0x4a415641;
    // That's "JAVA", the first part of "JAVA PROFILE ..."
    private final static String[] VERSIONS = {
            " PROFILE 1.0\0",
            " PROFILE 1.0.1\0",
            " PROFILE 1.0.2\0",
    };

    private final static int VERSION_JDK12BETA3 = 0;
    private final static int VERSION_JDK12BETA4 = 1;
    private final static int VERSION_JDK6       = 2;
    // These version numbers are indices into VERSIONS.  The instance data
    // member version is set to one of these, and it drives decisions when
    // reading the file.
    //
    // Version 1.0.1 added HPROF_GC_PRIM_ARRAY_DUMP, which requires no
    // version-sensitive parsing.
    //
    // Version 1.0.1 changed the type of a constant pool entry from a signature
    // to a typecode.
    //
    // Version 1.0.2 added HPROF_HEAP_DUMP_SEGMENT and HPROF_HEAP_DUMP_END
    // to allow a large heap to be dumped as a sequence of heap dump segments.
    //
    // The HPROF agent in J2SE 1.2 through to 5.0 generate a version 1.0.1
    // file. In Java SE 6.0 the version is either 1.0.1 or 1.0.2 depending on
    // the size of the heap (normally it will be 1.0.1 but for multi-GB
    // heaps the heap dump will not fit in a HPROF_HEAP_DUMP record so the
    // dump is generated as version 1.0.2).

    //
    // Record types:
    //
    static final int HPROF_UTF8          = 0x01;
    static final int HPROF_LOAD_CLASS    = 0x02;
    static final int HPROF_UNLOAD_CLASS  = 0x03;
    static final int HPROF_FRAME         = 0x04;
    static final int HPROF_TRACE         = 0x05;
    static final int HPROF_ALLOC_SITES   = 0x06;
    static final int HPROF_HEAP_SUMMARY  = 0x07;

    static final int HPROF_START_THREAD  = 0x0a;
    static final int HPROF_END_THREAD    = 0x0b;

    static final int HPROF_HEAP_DUMP     = 0x0c;

    static final int HPROF_CPU_SAMPLES   = 0x0d;
    static final int HPROF_CONTROL_SETTINGS = 0x0e;
    static final int HPROF_LOCKSTATS_WAIT_TIME = 0x10;
    static final int HPROF_LOCKSTATS_HOLD_TIME = 0x11;

    static final int HPROF_GC_ROOT_UNKNOWN       = 0xff;
    static final int HPROF_GC_ROOT_JNI_GLOBAL    = 0x01;
    static final int HPROF_GC_ROOT_JNI_LOCAL     = 0x02;
    static final int HPROF_GC_ROOT_JAVA_FRAME    = 0x03;
    static final int HPROF_GC_ROOT_NATIVE_STACK  = 0x04;
    static final int HPROF_GC_ROOT_STICKY_CLASS  = 0x05;
    static final int HPROF_GC_ROOT_THREAD_BLOCK  = 0x06;
    static final int HPROF_GC_ROOT_MONITOR_USED  = 0x07;
    static final int HPROF_GC_ROOT_THREAD_OBJ    = 0x08;

    static final int HPROF_GC_CLASS_DUMP         = 0x20;
    static final int HPROF_GC_INSTANCE_DUMP      = 0x21;
    static final int HPROF_GC_OBJ_ARRAY_DUMP         = 0x22;
    static final int HPROF_GC_PRIM_ARRAY_DUMP         = 0x23;

    static final int HPROF_HEAP_DUMP_SEGMENT     = 0x1c;
    static final int HPROF_HEAP_DUMP_END         = 0x2c;

    private final static int T_CLASS = 2;

    private int version;        // The version of .hprof being read

    private int debugLevel;
    private int currPos;        // Current position in the file

    private int dumpsToSkip;
    private boolean callStack;  // If true, read the call stack of objects

    private int identifierSize;         // Size, in bytes, of identifiers.
    private Hashtable<Long, String> names;

    // Hashtable<Integer, ThreadObject>, used to map the thread sequence number
    // (aka "serial number") to the thread object ID for
    // HPROF_GC_ROOT_THREAD_OBJ.  ThreadObject is a trivial inner class,
    // at the end of this file.
    private Hashtable<Integer, ThreadObject> threadObjects;

    // Hashtable<Long, String>, maps class object ID to class name
    // (with / converted to .)
    private Hashtable<Long, String> classNameFromObjectID;

    // Hashtable<Integer, Integer>, maps class serial # to class object ID
    private Hashtable<Integer, String> classNameFromSerialNo;

    // Hashtable<Long, StackFrame> maps stack frame ID to StackFrame.
    // Null if we're not tracking them.
    private Hashtable<Long, StackFrame> stackFrames;

    // Hashtable<Integer, StackTrace> maps stack frame ID to StackTrace
    // Null if we're not tracking them.
    private Hashtable<Integer, StackTrace> stackTraces;

    private Snapshot snapshot;

    public HprofReader(String fileName, PositionDataInputStream in,
                       int dumpNumber, boolean callStack, int debugLevel)
                       throws IOException {
        super(in);
        RandomAccessFile file = new RandomAccessFile(fileName, "r");
        this.snapshot = new Snapshot(MappedReadBuffer.create(file));
        this.dumpsToSkip = dumpNumber - 1;
        this.callStack = callStack;
        this.debugLevel = debugLevel;
        names = new Hashtable<Long, String>();
        threadObjects = new Hashtable<Integer, ThreadObject>(43);
        classNameFromObjectID = new Hashtable<Long, String>();
        if (callStack) {
            stackFrames = new Hashtable<Long, StackFrame>(43);
            stackTraces = new Hashtable<Integer, StackTrace>(43);
            classNameFromSerialNo = new Hashtable<Integer, String>();
        }
    }

    public Snapshot read() throws IOException {
        currPos = 4;    // 4 because of the magic number
        version = readVersionHeader();
        identifierSize = in.readInt();
        snapshot.setIdentifierSize(identifierSize);
        if (version >= VERSION_JDK12BETA4) {
            snapshot.setNewStyleArrayClass(true);
        } else {
            snapshot.setNewStyleArrayClass(false);
        }

        currPos += 4;
        if (identifierSize != 4 && identifierSize != 8) {
            throw new IOException("I'm sorry, but I can't deal with an identifier size of " + identifierSize + ".  I can only deal with 4 or 8.");
        }
        System.out.println("Dump file created " + (new Date(in.readLong())));
        currPos += 8;

        for (;;) {
            int type;
            try {
                type = in.readUnsignedByte();
            } catch (EOFException ignored) {
                break;
            }
            in.readInt();       // Timestamp of this record
            int length = in.readInt();
            if (debugLevel > 0) {
                System.out.println("Read record type " + type
                                   + ", length " + length
                                   + " at position " + toHex(currPos));
            }
            if (length < 0) {
                throw new IOException("Bad record length of " + length
                                      + " at byte " + toHex(currPos+5)
                                      + " of file.");
            }
            currPos += 9 + length;
            switch (type) {
                case HPROF_UTF8: {
                    long id = readID();
                    byte[] chars = new byte[length - identifierSize];
                    in.readFully(chars);
                    names.put(new Long(id), new String(chars));
                    break;
                }
                case HPROF_LOAD_CLASS: {
                    int serialNo = in.readInt();        // Not used
                    long classID = readID();
                    int stackTraceSerialNo = in.readInt();
                    long classNameID = readID();
                    Long classIdI = new Long(classID);
                    String nm = getNameFromID(classNameID).replace('/', '.');
                    classNameFromObjectID.put(classIdI, nm);
                    if (classNameFromSerialNo != null) {
                        classNameFromSerialNo.put(new Integer(serialNo), nm);
                    }
                    break;
                }

                case HPROF_HEAP_DUMP: {
                    if (dumpsToSkip <= 0) {
                        try {
                            readHeapDump(length, currPos);
                        } catch (EOFException exp) {
                            handleEOF(exp, snapshot);
                        }
                        if (debugLevel > 0) {
                            System.out.println("    Finished processing instances in heap dump.");
                        }
                        return snapshot;
                    } else {
                        dumpsToSkip--;
                        skipBytes(length);
                    }
                    break;
                }

                case HPROF_HEAP_DUMP_END: {
                    if (version >= VERSION_JDK6) {
                        if (dumpsToSkip <= 0) {
                            skipBytes(length);  // should be no-op
                            return snapshot;
                        } else {
                            // skip this dump (of the end record for a sequence of dump segments)
                            dumpsToSkip--;
                        }
                    } else {
                        // HPROF_HEAP_DUMP_END only recognized in >= 1.0.2
                        warn("Ignoring unrecognized record type " + type);
                    }
                    skipBytes(length);  // should be no-op
                    break;
                }

                case HPROF_HEAP_DUMP_SEGMENT: {
                    if (version >= VERSION_JDK6) {
                        if (dumpsToSkip <= 0) {
                            try {
                                // read the dump segment
                                readHeapDump(length, currPos);
                            } catch (EOFException exp) {
                                handleEOF(exp, snapshot);
                            }
                        } else {
                            // all segments comprising the heap dump will be skipped
                            skipBytes(length);
                        }
                    } else {
                        // HPROF_HEAP_DUMP_SEGMENT only recognized in >= 1.0.2
                        warn("Ignoring unrecognized record type " + type);
                        skipBytes(length);
                    }
                    break;
                }

                case HPROF_FRAME: {
                    if (stackFrames == null) {
                        skipBytes(length);
                    } else {
                        long id = readID();
                        String methodName = getNameFromID(readID());
                        String methodSig = getNameFromID(readID());
                        String sourceFile = getNameFromID(readID());
                        int classSer = in.readInt();
                        String className = classNameFromSerialNo.get(new Integer(classSer));
                        int lineNumber = in.readInt();
                        if (lineNumber < StackFrame.LINE_NUMBER_NATIVE) {
                            warn("Weird stack frame line number:  " + lineNumber);
                            lineNumber = StackFrame.LINE_NUMBER_UNKNOWN;
                        }
                        stackFrames.put(new Long(id),
                                        new StackFrame(methodName, methodSig,
                                                       className, sourceFile,
                                                       lineNumber));
                    }
                    break;
                }
                case HPROF_TRACE: {
                    if (stackTraces == null) {
                        skipBytes(length);
                    } else {
                        int serialNo = in.readInt();
                        int threadSeq = in.readInt();   // Not used
                        StackFrame[] frames = new StackFrame[in.readInt()];
                        for (int i = 0; i < frames.length; i++) {
                            long fid = readID();
                            frames[i] = stackFrames.get(new Long(fid));
                            if (frames[i] == null) {
                                throw new IOException("Stack frame " + toHex(fid) + " not found");
                            }
                        }
                        stackTraces.put(new Integer(serialNo),
                                        new StackTrace(frames));
                    }
                    break;
                }
                case HPROF_UNLOAD_CLASS:
                case HPROF_ALLOC_SITES:
                case HPROF_START_THREAD:
                case HPROF_END_THREAD:
                case HPROF_HEAP_SUMMARY:
                case HPROF_CPU_SAMPLES:
                case HPROF_CONTROL_SETTINGS:
                case HPROF_LOCKSTATS_WAIT_TIME:
                case HPROF_LOCKSTATS_HOLD_TIME:
                {
                    // Ignore these record types
                    skipBytes(length);
                    break;
                }
                default: {
                    skipBytes(length);
                    warn("Ignoring unrecognized record type " + type);
                }
            }
        }

        return snapshot;
    }

    private void skipBytes(int length) throws IOException {
        in.skipBytes(length);
    }

    private int readVersionHeader() throws IOException {
        int candidatesLeft = VERSIONS.length;
        boolean[] matched = new boolean[VERSIONS.length];
        for (int i = 0; i < candidatesLeft; i++) {
            matched[i] = true;
        }

        int pos = 0;
        while (candidatesLeft > 0) {
            char c = (char) in.readByte();
            currPos++;
            for (int i = 0; i < VERSIONS.length; i++) {
                if (matched[i]) {
                    if (c != VERSIONS[i].charAt(pos)) {   // Not matched
                        matched[i] = false;
                        --candidatesLeft;
                    } else if (pos == VERSIONS[i].length() - 1) {  // Full match
                        return i;
                    }
                }
            }
            ++pos;
        }
        throw new IOException("Version string not recognized at byte " + (pos+3));
    }

    private void readHeapDump(int bytesLeft, int posAtEnd) throws IOException {
        while (bytesLeft > 0) {
            int type = in.readUnsignedByte();
            if (debugLevel > 0) {
                System.out.println("    Read heap sub-record type " + type
                                   + " at position "
                                   + toHex(posAtEnd - bytesLeft));
            }
            bytesLeft--;
            switch(type) {
                case HPROF_GC_ROOT_UNKNOWN: {
                    long id = readID();
                    bytesLeft -= identifierSize;
                    snapshot.addRoot(new Root(id, 0, Root.UNKNOWN, ""));
                    break;
                }
                case HPROF_GC_ROOT_THREAD_OBJ: {
                    long id = readID();
                    int threadSeq = in.readInt();
                    int stackSeq = in.readInt();
                    bytesLeft -= identifierSize + 8;
                    threadObjects.put(new Integer(threadSeq),
                                      new ThreadObject(id, stackSeq));
                    break;
                }
                case HPROF_GC_ROOT_JNI_GLOBAL: {
                    long id = readID();
                    long globalRefId = readID();        // Ignored, for now
                    bytesLeft -= 2*identifierSize;
                    snapshot.addRoot(new Root(id, 0, Root.NATIVE_STATIC, ""));
                    break;
                }
                case HPROF_GC_ROOT_JNI_LOCAL: {
                    long id = readID();
                    int threadSeq = in.readInt();
                    int depth = in.readInt();
                    bytesLeft -= identifierSize + 8;
                    ThreadObject to = getThreadObjectFromSequence(threadSeq);
                    StackTrace st = getStackTraceFromSerial(to.stackSeq);
                    if (st != null) {
                        st = st.traceForDepth(depth+1);
                    }
                    snapshot.addRoot(new Root(id, to.threadId,
                                              Root.NATIVE_LOCAL, "", st));
                    break;
                }
                case HPROF_GC_ROOT_JAVA_FRAME: {
                    long id = readID();
                    int threadSeq = in.readInt();
                    int depth = in.readInt();
                    bytesLeft -= identifierSize + 8;
                    ThreadObject to = getThreadObjectFromSequence(threadSeq);
                    StackTrace st = getStackTraceFromSerial(to.stackSeq);
                    if (st != null) {
                        st = st.traceForDepth(depth+1);
                    }
                    snapshot.addRoot(new Root(id, to.threadId,
                                              Root.JAVA_LOCAL, "", st));
                    break;
                }
                case HPROF_GC_ROOT_NATIVE_STACK: {
                    long id = readID();
                    int threadSeq = in.readInt();
                    bytesLeft -= identifierSize + 4;
                    ThreadObject to = getThreadObjectFromSequence(threadSeq);
                    StackTrace st = getStackTraceFromSerial(to.stackSeq);
                    snapshot.addRoot(new Root(id, to.threadId,
                                              Root.NATIVE_STACK, "", st));
                    break;
                }
                case HPROF_GC_ROOT_STICKY_CLASS: {
                    long id = readID();
                    bytesLeft -= identifierSize;
                    snapshot.addRoot(new Root(id, 0, Root.SYSTEM_CLASS, ""));
                    break;
                }
                case HPROF_GC_ROOT_THREAD_BLOCK: {
                    long id = readID();
                    int threadSeq = in.readInt();
                    bytesLeft -= identifierSize + 4;
                    ThreadObject to = getThreadObjectFromSequence(threadSeq);
                    StackTrace st = getStackTraceFromSerial(to.stackSeq);
                    snapshot.addRoot(new Root(id, to.threadId,
                                     Root.THREAD_BLOCK, "", st));
                    break;
                }
                case HPROF_GC_ROOT_MONITOR_USED: {
                    long id = readID();
                    bytesLeft -= identifierSize;
                    snapshot.addRoot(new Root(id, 0, Root.BUSY_MONITOR, ""));
                    break;
                }
                case HPROF_GC_CLASS_DUMP: {
                    int bytesRead = readClass();
                    bytesLeft -= bytesRead;
                    break;
                }
                case HPROF_GC_INSTANCE_DUMP: {
                    int bytesRead = readInstance();
                    bytesLeft -= bytesRead;
                    break;
                }
                case HPROF_GC_OBJ_ARRAY_DUMP: {
                    int bytesRead = readArray(false);
                    bytesLeft -= bytesRead;
                    break;
                }
                case HPROF_GC_PRIM_ARRAY_DUMP: {
                    int bytesRead = readArray(true);
                    bytesLeft -= bytesRead;
                    break;
                }
                default: {
                    throw new IOException("Unrecognized heap dump sub-record type:  " + type);
                }
            }
        }
        if (bytesLeft != 0) {
            warn("Error reading heap dump or heap dump segment:  Byte count is " + bytesLeft + " instead of 0");
            skipBytes(bytesLeft);
        }
        if (debugLevel > 0) {
            System.out.println("    Finished heap sub-records.");
        }
    }

    private long readID() throws IOException {
        return (identifierSize == 4)?
            (Snapshot.SMALL_ID_MASK & (long)in.readInt()) : in.readLong();
    }

    //
    // Read a java value.  If result is non-null, it's expected to be an
    // array of one element.  We use it to fake multiple return values.
    // @returns the number of bytes read
    //
    private int readValue(JavaThing[] resultArr) throws IOException {
        byte type = in.readByte();
        return 1 + readValueForType(type, resultArr);
    }

    private int readValueForType(byte type, JavaThing[] resultArr)
            throws IOException {
        if (version >= VERSION_JDK12BETA4) {
            type = signatureFromTypeId(type);
        }
        return readValueForTypeSignature(type, resultArr);
    }

    private int readValueForTypeSignature(byte type, JavaThing[] resultArr)
            throws IOException {
        switch (type) {
            case '[':
            case 'L': {
                long id = readID();
                if (resultArr != null) {
                    resultArr[0] = new JavaObjectRef(id);
                }
                return identifierSize;
            }
            case 'Z': {
                int b = in.readByte();
                if (b != 0 && b != 1) {
                    warn("Illegal boolean value read");
                }
                if (resultArr != null) {
                    resultArr[0] = new JavaBoolean(b != 0);
                }
                return 1;
            }
            case 'B': {
                byte b = in.readByte();
                if (resultArr != null) {
                    resultArr[0] = new JavaByte(b);
                }
                return 1;
            }
            case 'S': {
                short s = in.readShort();
                if (resultArr != null) {
                    resultArr[0] = new JavaShort(s);
                }
                return 2;
            }
            case 'C': {
                char ch = in.readChar();
                if (resultArr != null) {
                    resultArr[0] = new JavaChar(ch);
                }
                return 2;
            }
            case 'I': {
                int val = in.readInt();
                if (resultArr != null) {
                    resultArr[0] = new JavaInt(val);
                }
                return 4;
            }
            case 'J': {
                long val = in.readLong();
                if (resultArr != null) {
                    resultArr[0] = new JavaLong(val);
                }
                return 8;
            }
            case 'F': {
                float val = in.readFloat();
                if (resultArr != null) {
                    resultArr[0] = new JavaFloat(val);
                }
                return 4;
            }
            case 'D': {
                double val = in.readDouble();
                if (resultArr != null) {
                    resultArr[0] = new JavaDouble(val);
                }
                return 8;
            }
            default: {
                throw new IOException("Bad value signature:  " + type);
            }
        }
    }

    private ThreadObject getThreadObjectFromSequence(int threadSeq)
            throws IOException {
        ThreadObject to = threadObjects.get(new Integer(threadSeq));
        if (to == null) {
            throw new IOException("Thread " + threadSeq +
                                  " not found for JNI local ref");
        }
        return to;
    }

    private String getNameFromID(long id) throws IOException {
        return getNameFromID(new Long(id));
    }

    private String getNameFromID(Long id) throws IOException {
        if (id.longValue() == 0L) {
            return "";
        }
        String result = names.get(id);
        if (result == null) {
            warn("Name not found at " + toHex(id.longValue()));
            return "unresolved name " + toHex(id.longValue());
        }
        return result;
    }

    private StackTrace getStackTraceFromSerial(int ser) throws IOException {
        if (stackTraces == null) {
            return null;
        }
        StackTrace result = stackTraces.get(new Integer(ser));
        if (result == null) {
            warn("Stack trace not found for serial # " + ser);
        }
        return result;
    }

    //
    // Handle a HPROF_GC_CLASS_DUMP
    // Return number of bytes read
    //
    private int readClass() throws IOException {
        long id = readID();
        StackTrace stackTrace = getStackTraceFromSerial(in.readInt());
        long superId = readID();
        long classLoaderId = readID();
        long signersId = readID();
        long protDomainId = readID();
        long reserved1 = readID();
        long reserved2 = readID();
        int instanceSize = in.readInt();
        int bytesRead = 7 * identifierSize + 8;

        int numConstPoolEntries = in.readUnsignedShort();
        bytesRead += 2;
        for (int i = 0; i < numConstPoolEntries; i++) {
            int index = in.readUnsignedShort(); // unused
            bytesRead += 2;
            bytesRead += readValue(null);       // We ignore the values
        }

        int numStatics = in.readUnsignedShort();
        bytesRead += 2;
        JavaThing[] valueBin = new JavaThing[1];
        JavaStatic[] statics = new JavaStatic[numStatics];
        for (int i = 0; i < numStatics; i++) {
            long nameId = readID();
            bytesRead += identifierSize;
            byte type = in.readByte();
            bytesRead++;
            bytesRead += readValueForType(type, valueBin);
            String fieldName = getNameFromID(nameId);
            if (version >= VERSION_JDK12BETA4) {
                type = signatureFromTypeId(type);
            }
            String signature = "" + ((char) type);
            JavaField f = new JavaField(fieldName, signature);
            statics[i] = new JavaStatic(f, valueBin[0]);
        }

        int numFields = in.readUnsignedShort();
        bytesRead += 2;
        JavaField[] fields = new JavaField[numFields];
        for (int i = 0; i < numFields; i++) {
            long nameId = readID();
            bytesRead += identifierSize;
            byte type = in.readByte();
            bytesRead++;
            String fieldName = getNameFromID(nameId);
            if (version >= VERSION_JDK12BETA4) {
                type = signatureFromTypeId(type);
            }
            String signature = "" + ((char) type);
            fields[i] = new JavaField(fieldName, signature);
        }
        String name = classNameFromObjectID.get(new Long(id));
        if (name == null) {
            warn("Class name not found for " + toHex(id));
            name = "unknown-name@" + toHex(id);
        }
        JavaClass c = new JavaClass(id, name, superId, classLoaderId, signersId,
                                    protDomainId, fields, statics,
                                    instanceSize);
        snapshot.addClass(id, c);
        snapshot.setSiteTrace(c, stackTrace);

        return bytesRead;
    }

    private String toHex(long addr) {
        return com.sun.tools.hat.internal.util.Misc.toHex(addr);
    }

    //
    // Handle a HPROF_GC_INSTANCE_DUMP
    // Return number of bytes read
    //
    private int readInstance() throws IOException {
        long start = in.position();
        long id = readID();
        StackTrace stackTrace = getStackTraceFromSerial(in.readInt());
        long classID = readID();
        int bytesFollowing = in.readInt();
        int bytesRead = (2 * identifierSize) + 8 + bytesFollowing;
        JavaObject jobj = new JavaObject(classID, start);
        skipBytes(bytesFollowing);
        snapshot.addHeapObject(id, jobj);
        snapshot.setSiteTrace(jobj, stackTrace);
        return bytesRead;
    }

    //
    // Handle a HPROF_GC_OBJ_ARRAY_DUMP or HPROF_GC_PRIM_ARRAY_DUMP
    // Return number of bytes read
    //
    private int readArray(boolean isPrimitive) throws IOException {
        long start = in.position();
        long id = readID();
        StackTrace stackTrace = getStackTraceFromSerial(in.readInt());
        int num = in.readInt();
        int bytesRead = identifierSize + 8;
        long elementClassID;
        if (isPrimitive) {
            elementClassID = in.readByte();
            bytesRead++;
        } else {
            elementClassID = readID();
            bytesRead += identifierSize;
        }

        // Check for primitive arrays:
        byte primitiveSignature = 0x00;
        int elSize = 0;
        if (isPrimitive || version < VERSION_JDK12BETA4) {
            switch ((int)elementClassID) {
                case T_BOOLEAN: {
                    primitiveSignature = (byte) 'Z';
                    elSize = 1;
                    break;
                }
                case T_CHAR: {
                    primitiveSignature = (byte) 'C';
                    elSize = 2;
                    break;
                }
                case T_FLOAT: {
                    primitiveSignature = (byte) 'F';
                    elSize = 4;
                    break;
                }
                case T_DOUBLE: {
                    primitiveSignature = (byte) 'D';
                    elSize = 8;
                    break;
                }
                case T_BYTE: {
                    primitiveSignature = (byte) 'B';
                    elSize = 1;
                    break;
                }
                case T_SHORT: {
                    primitiveSignature = (byte) 'S';
                    elSize = 2;
                    break;
                }
                case T_INT: {
                    primitiveSignature = (byte) 'I';
                    elSize = 4;
                    break;
                }
                case T_LONG: {
                    primitiveSignature = (byte) 'J';
                    elSize = 8;
                    break;
                }
            }
            if (version >= VERSION_JDK12BETA4 && primitiveSignature == 0x00) {
                throw new IOException("Unrecognized typecode:  "
                                        + elementClassID);
            }
        }
        if (primitiveSignature != 0x00) {
            int size = elSize * num;
            bytesRead += size;
            JavaValueArray va = new JavaValueArray(primitiveSignature, start);
            skipBytes(size);
            snapshot.addHeapObject(id, va);
            snapshot.setSiteTrace(va, stackTrace);
        } else {
            int sz = num * identifierSize;
            bytesRead += sz;
            JavaObjectArray arr = new JavaObjectArray(elementClassID, start);
            skipBytes(sz);
            snapshot.addHeapObject(id, arr);
            snapshot.setSiteTrace(arr, stackTrace);
        }
        return bytesRead;
    }

    private byte signatureFromTypeId(byte typeId) throws IOException {
        switch (typeId) {
            case T_CLASS: {
                return (byte) 'L';
            }
            case T_BOOLEAN: {
                return (byte) 'Z';
            }
            case T_CHAR: {
                return (byte) 'C';
            }
            case T_FLOAT: {
                return (byte) 'F';
            }
            case T_DOUBLE: {
                return (byte) 'D';
            }
            case T_BYTE: {
                return (byte) 'B';
            }
            case T_SHORT: {
                return (byte) 'S';
            }
            case T_INT: {
                return (byte) 'I';
            }
            case T_LONG: {
                return (byte) 'J';
            }
            default: {
                throw new IOException("Invalid type id of " + typeId);
            }
        }
    }

    private void handleEOF(EOFException exp, Snapshot snapshot) {
        if (debugLevel > 0) {
            exp.printStackTrace();
        }
        warn("Unexpected EOF. Will miss information...");
        // we have EOF, we have to tolerate missing references
        snapshot.setUnresolvedObjectsOK(true);
    }

    private void warn(String msg) {
        System.out.println("WARNING: " + msg);
    }

    //
    // A trivial data-holder class for HPROF_GC_ROOT_THREAD_OBJ.
    //
    private class ThreadObject {

        long threadId;
        int stackSeq;

        ThreadObject(long threadId, int stackSeq) {
            this.threadId = threadId;
            this.stackSeq = stackSeq;
        }
    }

}

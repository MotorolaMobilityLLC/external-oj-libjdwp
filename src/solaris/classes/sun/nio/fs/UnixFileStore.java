/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package sun.nio.fs;

import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.channels.*;
import java.util.*;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Base implementation of FileStore for Unix/like implementations.
 */

abstract class UnixFileStore
    extends FileStore
{
    // original path of file that identified file system
    private final UnixPath file;

    // device ID
    private final long dev;

    // entry in the mount tab
    private final UnixMountEntry entry;

    // return the device ID where the given file resides
    private static long devFor(UnixPath file) throws IOException {
        try {
            return UnixFileAttributes.get(file, true).dev();
        } catch (UnixException x) {
            x.rethrowAsIOException(file);
            return 0L;  // keep compiler happy
        }
    }

    UnixFileStore(UnixPath file) throws IOException {
        this.file = file;
        this.dev = devFor(file);
        this.entry = findMountEntry();
    }

    UnixFileStore(UnixFileSystem fs, UnixMountEntry entry) throws IOException {
        this.file = new UnixPath(fs, entry.dir());
        this.dev = (entry.dev() == 0L) ? devFor(this.file) : entry.dev();
        this.entry = entry;
    }

    /**
     * Find the mount entry for the file store
     */
    abstract UnixMountEntry findMountEntry() throws IOException;

    /**
     * Returns true if this file store represents a loopback file system that
     * will have the same device ID as underlying file system.
     */
    abstract boolean isLoopback();

    UnixPath file() {
        return file;
    }

    long dev() {
        return dev;
    }

    UnixMountEntry entry() {
        return entry;
    }

    @Override
    public String name() {
        return entry.name();
    }

    @Override
    public String type() {
        return entry.fstype();
    }

    @Override
    public boolean isReadOnly() {
        return entry.isReadOnly();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> view)
    {
        if (view == null)
            throw new NullPointerException();
        if (view == FileStoreSpaceAttributeView.class)
            return (V) new UnixFileStoreSpaceAttributeView(this);
        return (V) null;
    }

    @Override
    public Object getAttribute(String attribute) throws IOException {
        if (attribute.equals("space:totalSpace"))
            return new UnixFileStoreSpaceAttributeView(this)
                .readAttributes().totalSpace();
        if (attribute.equals("space:usableSpace"))
            return new UnixFileStoreSpaceAttributeView(this)
                 .readAttributes().usableSpace();
        if (attribute.equals("space:unallocatedSpace"))
            return new UnixFileStoreSpaceAttributeView(this)
                 .readAttributes().unallocatedSpace();
        throw new UnsupportedOperationException("'" + attribute + "' not recognized");
    }

    @Override
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
        if (type == null)
            throw new NullPointerException();
        if (type == BasicFileAttributeView.class)
            return true;
        if (type == PosixFileAttributeView.class ||
            type == FileOwnerAttributeView.class)
        {
            // lookup fstypes.properties
            FeatureStatus status = checkIfFeaturePresent("posix");
            if (status == FeatureStatus.NOT_PRESENT)
                return false;
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsFileAttributeView(String name) {
        if (name.equals("basic") || name.equals("unix"))
            return true;
        if (name.equals("posix"))
            return supportsFileAttributeView(PosixFileAttributeView.class);
        if (name.equals("owner"))
            return supportsFileAttributeView(FileOwnerAttributeView.class);
        return false;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == this)
            return true;
        if (!(ob instanceof UnixFileStore))
            return false;
        UnixFileStore other = (UnixFileStore)ob;
        if (dev != other.dev)
            return false;
        // deviceIDs are equal but they may not be equal if one or both of
        // them is a loopback file system
        boolean thisIsLoopback = isLoopback();
        if (thisIsLoopback != other.isLoopback())
            return false;  // one, but not both, are lofs
        if (!thisIsLoopback)
            return true;    // neither is lofs
        // both are lofs so compare mount points
        return Arrays.equals(this.entry.dir(), other.entry.dir());
    }

    @Override
    public int hashCode() {
        return (int)(dev ^ (dev >>> 32));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(new String(entry.dir()));
        sb.append(" (");
        sb.append(entry.name());
        sb.append(")");
        return sb.toString();
    }

    private static class UnixFileStoreSpaceAttributeView
        implements FileStoreSpaceAttributeView
    {
        private final UnixFileStore fs;

        UnixFileStoreSpaceAttributeView(UnixFileStore fs) {
            this.fs = fs;
        }

        @Override
        public String name() {
            return "space";
        }

        @Override
        public FileStoreSpaceAttributes readAttributes()
            throws IOException
        {
            UnixPath file = fs.file();
            final UnixFileStoreAttributes attrs;
            try {
                attrs = UnixFileStoreAttributes.get(file);
            } catch (UnixException x) {
                x.rethrowAsIOException(file);
                return null;    // keep compile happy
            }

            return new FileStoreSpaceAttributes() {
                @Override
                public long totalSpace() {
                    return attrs.blockSize() * attrs.totalBlocks();
                }
                @Override
                public long usableSpace() {
                    return attrs.blockSize() * attrs.availableBlocks();
                }
                @Override
                public long unallocatedSpace() {
                    return attrs.blockSize() * attrs.freeBlocks();
                }
            };
        }
    }

    // -- fstypes.properties --

    private static final Object loadLock = new Object();
    private static volatile Properties props;

    enum FeatureStatus {
        PRESENT,
        NOT_PRESENT,
        UNKNOWN;
    }

    /**
     * Returns status to indicate if file system supports a given feature
     */
    FeatureStatus checkIfFeaturePresent(String feature) {
        if (props == null) {
            synchronized (loadLock) {
                if (props == null) {
                    props = AccessController.doPrivileged(
                        new PrivilegedAction<Properties>() {
                            @Override
                            public Properties run() {
                                return loadProperties();
                            }});
                }
            }
        }

        String value = props.getProperty(type());
        if (value != null) {
            String[] values = value.split("\\s");
            for (String s: values) {
                s = s.trim().toLowerCase();
                if (s.equals(feature)) {
                    return FeatureStatus.PRESENT;
                }
                if (s.startsWith("no")) {
                    s = s.substring(2);
                    if (s.equals(feature)) {
                        return FeatureStatus.NOT_PRESENT;
                    }
                }
            }
        }
        return FeatureStatus.UNKNOWN;
    }

    private static Properties loadProperties() {
        Properties result = new Properties();
        String fstypes = System.getProperty("java.home") + "/lib/fstypes.properties";
        Path file = Paths.get(fstypes);
        try {
            ReadableByteChannel rbc = file.newByteChannel();
            try {
                result.load(Channels.newReader(rbc, "UTF-8"));
            } finally {
                rbc.close();
            }
        } catch (IOException x) {
        }
        return result;
    }
}

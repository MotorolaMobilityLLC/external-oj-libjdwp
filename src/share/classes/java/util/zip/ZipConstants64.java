/*
 * Copyright 1995-1996 Sun Microsystems, Inc.  All Rights Reserved.
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

package java.util.zip;

/*
 * This class defines the constants that are used by the classes
 * which manipulate Zip64 files.
 */

class ZipConstants64 {

    /*
     * ZIP64 constants
     */
    static final long ZIP64_ENDSIG = 0x06064b50L;  // "PK\006\006"
    static final long ZIP64_LOCSIG = 0x07064b50L;  // "PK\006\007"
    static final int  ZIP64_ENDHDR = 56;           // ZIP64 end header size
    static final int  ZIP64_LOCHDR = 20;           // ZIP64 end loc header size
    static final int  ZIP64_EXTHDR = 24;           // EXT header size
    static final int  ZIP64_EXTID  = 0x0001;       // Extra field Zip64 header ID

    static final int  ZIP64_MAGICCOUNT = 0xFFFF;
    static final long ZIP64_MAGICVAL = 0xFFFFFFFFL;

    /*
     * Zip64 End of central directory (END) header field offsets
     */
    static final int  ZIP64_ENDLEN = 4;       // size of zip64 end of central dir
    static final int  ZIP64_ENDVEM = 12;      // version made by
    static final int  ZIP64_ENDVER = 14;      // version needed to extract
    static final int  ZIP64_ENDNMD = 16;      // number of this disk
    static final int  ZIP64_ENDDSK = 20;      // disk number of start
    static final int  ZIP64_ENDTOD = 24;      // total number of entries on this disk
    static final int  ZIP64_ENDTOT = 32;      // total number of entries
    static final int  ZIP64_ENDSIZ = 40;      // central directory size in bytes
    static final int  ZIP64_ENDOFF = 48;      // offset of first CEN header
    static final int  ZIP64_ENDEXT = 56;      // zip64 extensible data sector

    /*
     * Zip64 End of central directory locator field offsets
     */
    static final int  ZIP64_LOCDSK = 4;       // disk number start
    static final int  ZIP64_LOCOFF = 8;       // offset of zip64 end
    static final int  ZIP64_LOCTOT = 16;      // total number of disks

    /*
     * Zip64 Extra local (EXT) header field offsets
     */
    static final int  ZIP64_EXTCRC = 4;       // uncompressed file crc-32 value
    static final int  ZIP64_EXTSIZ = 8;       // compressed size, 8-byte
    static final int  ZIP64_EXTLEN = 16;      // uncompressed size, 8-byte

    private ZipConstants64() {}
}

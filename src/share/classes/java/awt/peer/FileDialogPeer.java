/*
 * Copyright 1995-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package java.awt.peer;

import java.awt.FileDialog;
import java.io.FilenameFilter;

/**
 * The peer interface for {@link FileDialog}.
 *
 * The peer interfaces are intended only for use in porting
 * the AWT. They are not intended for use by application
 * developers, and developers should not implement peers
 * nor invoke any of the peer methods directly on the peer
 * instances.
 */
public interface FileDialogPeer extends DialogPeer {

    /**
     * Sets the selected file for this file dialog.
     *
     * @param file the file to set as selected file, or {@code null} for
     *        no selected file
     *
     * @see FileDialog#setFile(String)
     */
    void setFile(String file);

    /**
     * Sets the current directory for this file dialog.
     *
     * @param dir the directory to set
     *
     * @see FileDialog#setDirectory(String)
     */
    void setDirectory(String dir);

    /**
     * Sets the filename filter for filtering the displayed files.
     *
     * @param filter the filter to set
     *
     * @see FileDialog#setFilenameFilter(FilenameFilter)
     */
    void setFilenameFilter(FilenameFilter filter);
}

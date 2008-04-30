/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
 * @test
 * @bug 6690791
 * @summary Checks that there is no ClassCastException
 * in MenuSelectionManager.processMouseEvent()
 * @author Mikhail Lapshin
 * @run main bug6690791
 */

import javax.swing.*;
import java.awt.event.MouseEvent;

public class bug6690791 {
    public static void main(String[] args) throws Exception {
        MouseEvent me = new MouseEvent(new JLabel(), MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), MouseEvent.ALT_MASK,
                10, 10, 100, 100, 1, false, MouseEvent.BUTTON1);
        me.setSource(new Object());
        MenuSelectionManager.defaultManager().processMouseEvent(me);
    }
}

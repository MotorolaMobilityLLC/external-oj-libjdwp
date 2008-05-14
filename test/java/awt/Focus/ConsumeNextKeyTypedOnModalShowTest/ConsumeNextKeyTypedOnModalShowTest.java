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
  @test      %W% %E%
  @bug       6637607
  @summary   Showing a modal dlg on TAB KEY_PRESS shouldn't consume inappropriate KEY_TYPED.
  @author    Anton Tarasov: area=awt-focus
  @library   ../../regtesthelpers
  @build     Util
  @run       main ConsumeNextKeyTypedOnModalShowTest
*/

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.InvocationTargetException;
import test.java.awt.regtesthelpers.Util;

public class ConsumeNextKeyTypedOnModalShowTest extends Applet {
    Robot robot;
    Frame frame = new Frame("Frame");
    Dialog dialog = new Dialog(frame, "Dialog", true);
    TextField tf0 = new TextField();
    TextField tf1 = new TextField();
    Button button = new Button("Button");

    public static void main(String[] args) {
        ConsumeNextKeyTypedOnModalShowTest app = new ConsumeNextKeyTypedOnModalShowTest();
        app.init();
        app.start();
    }

    public void init() {
        robot = Util.createRobot();

        tf0.setPreferredSize(new Dimension(50, 30));
        tf1.setPreferredSize(new Dimension(50, 30));
        frame.setLayout(new FlowLayout());
        frame.add(tf0);
        frame.add(tf1);
        frame.pack();

        dialog.add(button);
        dialog.pack();

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getSource() == tf0) {
                    dialog.setVisible(true);
                }
            }
        }, KeyEvent.KEY_EVENT_MASK);
    }

    public void start() {
        frame.setVisible(true);
        Util.waitTillShown(frame);

        // Show the dialog.
        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_TAB);

        Util.waitForIdle(robot);

        // Dispose the dialog.
        Runnable action = new Runnable() {
            public void run() {
                dialog.dispose();
            }
        };
        if (!Util.trackFocusGained(tf1, action, 2000, false)) {
            throw new RuntimeException("Test failed: TAB was processed incorrectly!");
        }

        // Check for type-ability.
        robot.keyPress(KeyEvent.VK_A);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_A);

        Util.waitForIdle(robot);

        if (tf1.getText().equals("")) {
            throw new RuntimeException("Test failed: couldn't type a char!");
        }
        System.out.println("Test passed.");
    }
}

/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
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
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 *
 *
 * A test service for use in the inetd/System.inheritedChannel unit
 * tests.
 *
 * The test checks that the channel returned by System.inheritiedChannel
 * is in blocking mode and is bound. In addition, in the case of a
 * SocketChannel checks that the socket is connected.
 *
 * The test service is launched with an argument that is the reply port.
 * This reply port is used as an out-of-band connection to the unit test
 * so that the test status can be reported. When the test completes it
 * establishes a TCP connection to the port and sends a PASSED/FAILED
 * message to indicate the test result.
 */
import java.nio.*;
import java.nio.channels.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class StateTestService {

    static boolean failed = false;
    static int reply_port;

    static void check(boolean okay) {
        if (!okay) {
            failed = true;
        }
    }

    private static void reply(String msg) throws IOException {
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), reply_port);
        SocketChannel sc = SocketChannel.open(isa);
        byte b[] = msg.getBytes("UTF-8");
        ByteBuffer bb = ByteBuffer.wrap(b);
        sc.write(bb);
        sc.close();
    }

    public static void main(String args[]) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: StateTestService [reply-port]");
            return;
        }
        reply_port = Integer.parseInt(args[0]);

        Channel c = null;
        try {
            c = System.inheritedChannel();
        } catch (SecurityException se) {
            // ignore
        }
        if (c == null) {
            reply("FAILED");
            return;
        }

        if (c instanceof SocketChannel) {
            SocketChannel sc = (SocketChannel)c;
            check( sc.isBlocking() );
            check( sc.socket().isBound() );
            check( sc.socket().isConnected() );
        }

        if (c instanceof ServerSocketChannel) {
            ServerSocketChannel ssc = (ServerSocketChannel)c;
            check( ssc.isBlocking() );
            check( ssc.socket().isBound() );
        }

        if (c instanceof DatagramChannel) {
            DatagramChannel dc = (DatagramChannel)c;
            check( dc.isBlocking() );
            check( dc.socket().isBound() );
        }

        if (failed) {
            reply("FAILED");
        } else {
            reply("PASSED");
        }

    }

}

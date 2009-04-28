/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

/* @test
 * @bug 4927640
 * @summary Tests the SCTP protocol implementation
 * @author chegar
 */

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.UnsupportedAddressTypeException;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;
import static java.lang.System.out;
import static java.lang.System.err;

/**
 * Tests connect, finishConnect, isConnectionPending,
 * getRemoteAddresses and association.
 */
public class Connect {
    final CountDownLatch finishedLatch = new CountDownLatch(1);

    void test(String[] args) {
        SocketAddress address = null;
        Server server = null;

        if (!Util.isSCTPSupported()) {
            out.println("SCTP protocol is not supported");
            out.println("Test cannot be run");
            return;
        }

        if (args.length == 2) {
            /* requested to connect to a specific address */
            try {
                int port = Integer.valueOf(args[1]);
                address = new InetSocketAddress(args[0], port);
            } catch (NumberFormatException nfe) {
                err.println(nfe);
            }
        } else {
            /* start server on local machine, default */
            try {
                server = new Server();
                server.start();
                address = server.address();
                debug("Server started and listening on " + address);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }

        doTest(address);
    }

    void doTest(SocketAddress addr) {
        SctpChannel channel = null;
        final SocketAddress peerAddress = addr;

        try {
            channel = SctpChannel.open();

            /* TEST 0.5 Verify default values for new/unconnected channel */
            check(channel.getRemoteAddresses().isEmpty(),
                    "non empty set for unconnected channel");
            check(channel.association() == null,
                    "non-null association for unconnected channel");
            check(!channel.isConnectionPending(),
                    "should not have a connection pending");

            /* TEST 1: non-blocking connect */
            channel.configureBlocking(false);
            if (channel.connect(peerAddress) != true) {
                debug("non-blocking connect did not immediately succeed");
                check(channel.isConnectionPending(),
                        "should return true for isConnectionPending");
                try {
                    channel.connect(peerAddress);
                    fail("should have thrown ConnectionPendingException");
                } catch (ConnectionPendingException cpe) {
                    pass();
                } catch (IOException ioe) {
                    unexpected(ioe);
                }
                channel.configureBlocking(true);
                check(channel.finishConnect(),
                        "finishConnect should have returned true");
            }

            /* TEST 1.5 Verify after connect */
            check(!channel.getRemoteAddresses().isEmpty(),
                    "empty set for connected channel");
            check(channel.association() != null,
                    "null association for connected channel");
            check(!channel.isConnectionPending(),
                    "pending connection for connected channel");

            /* TEST 2: Verify AlreadyConnectedException thrown */
            try {
                channel.connect(peerAddress);
                fail("should have thrown AlreadyConnectedException");
            } catch (AlreadyConnectedException unused) {
                pass();
            }  catch (IOException ioe) {
                unexpected(ioe);
            }

            /* TEST 3: UnresolvedAddressException */
            channel.close();
            channel = SctpChannel.open();
            InetSocketAddress unresolved =
                    InetSocketAddress.createUnresolved("xxyyzzabc", 4567);
            try {
                channel.connect(unresolved);
                fail("should have thrown UnresolvedAddressException");
            } catch (UnresolvedAddressException unused) {
                pass();
            }  catch (IOException ioe) {
                unexpected(ioe);
            }

            /* TEST 4: UnsupportedAddressTypeException */
            SocketAddress unsupported = new UnsupportedSocketAddress();
            try {
                channel.connect(unsupported);
                fail("should have thrown UnsupportedAddressTypeException");
            } catch (UnsupportedAddressTypeException unused) {
                pass();
            }  catch (IOException ioe) {
                unexpected(ioe);
            }

            /* TEST 5: ClosedChannelException */
            channel.close();
            final SctpChannel closedChannel = channel;
            testCCE(new Callable<Void>() {
                public Void call() throws IOException {
                    closedChannel.connect(peerAddress); return null; } });

            /* TEST 5.5 getRemoteAddresses */
            testCCE(new Callable<Void>() {
                public Void call() throws IOException {
                    closedChannel.getRemoteAddresses(); return null; } });
            testCCE(new Callable<Void>() {
                public Void call() throws IOException {
                    closedChannel.association(); return null; } });
            check(!channel.isConnectionPending(),
                    "pending connection for closed channel");

            /* Run some more finishConnect tests */

            /* TEST 6: NoConnectionPendingException */
            channel = SctpChannel.open();
            try {
                channel.finishConnect();
                fail("should have thrown NoConnectionPendingException");
            } catch (NoConnectionPendingException unused) {
                pass();
            }  catch (IOException ioe) {
                unexpected(ioe);
            }

            /* TEST 7: ClosedChannelException */
            channel.close();
            final SctpChannel cceChannel = channel;
            testCCE(new Callable<Void>() {
                public Void call() throws IOException {
                    cceChannel.finishConnect(); return null; } });
        } catch (IOException ioe) {
            unexpected(ioe);
        } finally {
            finishedLatch.countDown();
            try { if (channel != null) channel.close(); }
            catch (IOException e) { unexpected(e);}
        }
    }

    class UnsupportedSocketAddress extends SocketAddress { }

    void testCCE(Callable callable) {
        try {
            callable.call();
            fail("should have thrown ClosedChannelException");
        } catch (ClosedChannelException cce) {
           pass();
        } catch (Exception ioe) {
            unexpected(ioe);
        }
    }

    class Server implements Runnable
    {
        final InetSocketAddress serverAddr;
        private SctpServerChannel ssc;

        public Server() throws IOException {
            ssc = SctpServerChannel.open().bind(null);
            java.util.Set<SocketAddress> addrs = ssc.getAllLocalAddresses();
            if (addrs.isEmpty())
                debug("addrs should not be empty");

            serverAddr = (InetSocketAddress) addrs.iterator().next();
        }

        public void start() {
            (new Thread(this, "Server-"  + serverAddr.getPort())).start();
        }

        public InetSocketAddress address() {
            return serverAddr;
        }

        @Override
        public void run() {
            SctpChannel sc = null;
            try {
                sc = ssc.accept();
                finishedLatch.await();
            } catch (IOException ioe) {
                unexpected(ioe);
            } catch (InterruptedException ie) {
                unexpected(ie);
            } finally {
                try { if (ssc != null) ssc.close(); }
                catch (IOException  ioe) { unexpected(ioe); }
                try { if (sc != null) sc.close(); }
                catch (IOException  ioe) { unexpected(ioe); }
            }
        }
    }

        //--------------------- Infrastructure ---------------------------
    boolean debug = true;
    volatile int passed = 0, failed = 0;
    void pass() {passed++;}
    void fail() {failed++; Thread.dumpStack();}
    void fail(String msg) {System.err.println(msg); fail();}
    void unexpected(Throwable t) {failed++; t.printStackTrace();}
    void check(boolean cond) {if (cond) pass(); else fail();}
    void check(boolean cond, String failMessage) {if (cond) pass(); else fail(failMessage);}
    void debug(String message) {if(debug) { System.out.println(message); }  }
    public static void main(String[] args) throws Throwable {
        Class<?> k = new Object(){}.getClass().getEnclosingClass();
        try {k.getMethod("instanceMain",String[].class)
                .invoke( k.newInstance(), (Object) args);}
        catch (Throwable e) {throw e.getCause();}}
    public void instanceMain(String[] args) throws Throwable {
        try {test(args);} catch (Throwable t) {unexpected(t);}
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0) throw new AssertionError("Some tests failed");}

}

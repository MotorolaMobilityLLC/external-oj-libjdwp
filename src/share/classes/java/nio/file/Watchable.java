/*
 * Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package java.nio.file;

import java.io.IOException;

/**
 * An object that may be registered with a watch service so that it can be
 * <em>watched</em> for changes and events.
 *
 * <p> This interface defines the {@link #register register} method to register
 * the object with a {@link WatchService} returning a {@link WatchKey} to
 * represent the registration. An object may be registered with more than one
 * watch service. Registration with a watch service is cancelled by invoking the
 * key's {@link WatchKey#cancel cancel} method.
 *
 * @since 1.7
 *
 * @see Path#register
 */

public interface Watchable {

    /**
     * Registers an object with a watch service.
     *
     * <p> If the file system object identified by this object is currently
     * registered with the watch service then the watch key, representing that
     * registration, is returned after changing the event set or modifiers to
     * those specified by the {@code events} and {@code modifiers} parameters.
     * Changing the event set does not cause pending events for the object to be
     * discarded. Objects are automatically registered for the {@link
     * StandardWatchEventKind#OVERFLOW OVERFLOW} event. This event is not
     * required to be present in the array of events.
     *
     * <p> Otherwise the file system object has not yet been registered with the
     * given watch service, so it is registered and the resulting new key is
     * returned.
     *
     * <p> Implementations of this interface should specify the events they
     * support.
     *
     * @param   watcher
     *          The watch service to which this object is to be registered
     * @param   events
     *          The events for which this object should be registered
     * @param   modifiers
     *          The modifiers, if any, that modify how the object is registered
     *
     * @return  A key representing the registration of this object with the
     *          given watch service
     *
     * @throws  UnsupportedOperationException
     *          If unsupported events or modifiers are specified
     * @throws  IllegalArgumentException
     *          If an invalid of combination of events are modifiers are specified
     * @throws  ClosedWatchServiceException
     *          If the watch service is closed
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          If a security manager is installed and it denies an unspecified
     *          permission required to monitor this object. Implementations of
     *          this interface should specify the permission checks.
     */
    WatchKey register(WatchService watcher,
                      WatchEvent.Kind<?>[] events,
                      WatchEvent.Modifier... modifiers)
        throws IOException;


    /**
     * Registers an object with a watch service.
     *
     * <p> An invocation of this method behaves in exactly the same way as the
     * invocation
     * <pre>
     *     watchable.{@link #register(WatchService,WatchEvent.Kind[],WatchEvent.Modifier[]) register}(watcher, events, new WatchEvent.Modifier[0]);
     * </pre>
     *
     * @param   watcher
     *          The watch service to which this object is to be registered
     * @param   events
     *          The events for which this object should be registered
     *
     * @return  A key representing the registration of this object with the
     *          given watch service
     *
     * @throws  UnsupportedOperationException
     *          If unsupported events are specified
     * @throws  IllegalArgumentException
     *          If an invalid of combination of events are specified
     * @throws  ClosedWatchServiceException
     *          If the watch service is closed
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          If a security manager is installed and it denies an unspecified
     *          permission required to monitor this object. Implementations of
     *          this interface should specify the permission checks.
     */
    WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events)
        throws IOException;
}

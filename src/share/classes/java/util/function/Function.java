/*
 * Copyright (c) 2010, 2012 Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package java.util.function;

/**
 * Apply a function to the input object yielding an appropriate result object. A
 * function may variously provide a mapping between types, object instances or
 * keys and values or any other form of transformation upon the input.
 *
 * @param <T> the type of input objects to the {@code apply} operation
 * @param <R> the type of result objects from the {@code apply} operation. May
 * be the same type as {@code <T>}.
 *
 * @since 1.8
 */
public interface Function<T, R> {

    /**
     * Yield an appropriate result object for the input object.
     *
     * @param t the input object
     * @return the function result
     */
    public R apply(T t);
}

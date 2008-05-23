/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tracing.dtrace;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;


/**
 * This annotation describes the interface's field attributes
 * for the probes in a provider.
 *
 * This annotation provides the contents of field-specific annotations
 * that specify the stability attributes and dependency class of a
 * particular field, for the probes in a provider.
 * <p>
 * The default interface attributes for unspecified fields is
 * Private/Private/Unknown.
 * <p>
 * @see <a href="http://docs.sun.com/app/docs/doc/817-6223/6mlkidlnp?a=view">Solaris Dynamic Tracing Guide, Chapter 39: Stability</a>
 * @since 1.7
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Attributes {
  /**
   * The stability level of the name.
   */
  StabilityLevel name() default StabilityLevel.PRIVATE;

  /**
   * The stability level of the data.
   */
  StabilityLevel data() default StabilityLevel.PRIVATE;

  /**
   * The interface attribute's dependency class.
   */
  DependencyClass dependency()  default DependencyClass.UNKNOWN;
}

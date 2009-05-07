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

package java.dyn;

import sun.dyn.util.BytecodeName;

/**
 * An <code>invokedynamic</code> call site, as reified to the bootstrap method.
 * Every instance of a call site corresponds to a distinct instance
 * of the <code>invokedynamic</code> instruction.
 * Call sites have state, one reference word, called the <code>target</code>,
 * and typed as a {@link MethodHandle}.  When this state is null (as it is
 * initially) the call site is in the unlinked state.  Otherwise, it is said
 * to be linked to its target.
 * <p>
 * When an unlinked call site is executed, a bootstrap routine is called
 * to finish the execution of the call site, and optionally to link
 * the call site.
 * <p>
 * @author John Rose, JSR 292 EG
 */
public class CallSite {
    // Fields used only by the JVM.  Do not use or change.
    private Object vmmethod;
    int callerMID, callerBCI;  // supplied by the JVM

    MethodHandle target;
    final Object caller;  // usually a class
    final String name;
    final MethodType type;

    public CallSite(Object caller, String name, MethodType type) {
        this.caller = caller;
        this.name = name;
        this.type = type;
    }

    private static void privateInitializeCallSite(CallSite site, int callerMID, int callerBCI) {
        site.callerMID = callerMID;
        site.callerBCI = callerBCI;
        if (site.target == null)
            site.setTarget(site.initialTarget());
    }

    /**
     * Just after a call site is created by a bootstrap method handle,
     * if the target has not been initialized by the factory method itself,
     * the method {@code initialTarget} is called to produce an initial
     * non-null target.  (Live call sites must never have null targets.)
     * <p>
     * If the bootstrap method itself does not initialize the call site,
     * this method must be overridden, because it just raises an
     * {@code InvokeDynamicBootstrapError}.
     */
    protected MethodHandle initialTarget() {
        throw new InvokeDynamicBootstrapError("target must be initialized before call site is linked: "+this);
    }

    /**
     * Report the current linkage state of the call site.  (This is mutable.)
     * The value is null if and only if the call site is currently unlinked.
     * When a linked call site is invoked, the target method is used directly.
     * When an unlinked call site is invoked, its bootstrap method receives
     * the call, as if via {@link Linkage#bootstrapInvokeDynamic}.
     * <p>
     * The interactions of {@code getTarget} with memory are the same
     * as of a read from an ordinary variable, such as an array element or a
     * non-volatile, non-final field.
     * <p>
     * In particular, the current thread may choose to reuse the result
     * of a previous read of the target from memory, and may fail to see
     * a recent update to the target by another thread.
     * @return the current linkage state of the call site
     * @see #setTarget
     */
    public MethodHandle getTarget() {
        return target;
    }

    /**
     * Link or relink the call site, by setting its target method.
     * <p>
     * The interactions of {@code setTarget} with memory are the same
     * as of a write to an ordinary variable, such as an array element or a
     * non-volatile, non-final field.
     * <p>
     * In particular, unrelated threads may fail to see the updated target
     * until they perform a read from memory.
     * Stronger guarantees can be created by putting appropriate operations
     * into the bootstrap method and/or the target methods used
     * at any given call site.
     * @param target the new target, or null if it is to be unlinked
     * @throws WrongMethodTypeException if the new target is not null
     *         and has a method type that differs from the call site's {@link #type}
     */
    public void setTarget(MethodHandle target) {
        checkTarget(target);
        this.target = target;
    }

    protected void checkTarget(MethodHandle target) {
        if (!canSetTarget(target))
            throw new WrongMethodTypeException(String.valueOf(target));
    }

    protected boolean canSetTarget(MethodHandle target) {
        return (target != null && target.type() == type());
    }

    /**
     * Report the class containing the call site.
     * This is immutable static context.
     * @return class containing the call site
     */
    public Class<?> callerClass() {
        return (Class) caller;
    }

    /**
     * Report the method name specified in the {@code invokedynamic} instruction.
     * This is immutable static context.
     * <p>
     * Note that the name is a JVM bytecode name, and as such can be any
     * non-empty string, as long as it does not contain certain "dangerous"
     * characters such as slash {@code '/'} and dot {@code '.'}.
     * See the Java Virtual Machine specification for more details.
     * <p>
     * Application such as a language runtimes may need to encode
     * arbitrary program element names and other configuration information
     * into the name.  A standard convention for doing this is
     * <a href="http://blogs.sun.com/jrose/entry/symbolic_freedom_in_the_vm">specified here</a>.
     * @return method name specified by the call site
     */
    public String name() {
        return name;
    }

    /**
     * Report the method name specified in the {@code invokedynamic} instruction,
     * as a series of components, individually demangled according to
     * the standard convention
     * <a href="http://blogs.sun.com/jrose/entry/symbolic_freedom_in_the_vm">specified here</a>.
     * <p>
     * Non-empty runs of characters between dangerous characters are demangled.
     * Each component is either a completely arbitrary demangled string,
     * or else a character constant for a punctuation character, typically ':'.
     * (In principle, the character can be any dangerous character that the
     * JVM lets through in a method name, such as '$' or ']'.
     * Runtime implementors are encouraged to use colon ':' for building
     * structured names.)
     * <p>
     * In the common case where the name contains no dangerous characters,
     * the result is an array whose only element array is the demangled
     * name at the call site.  Such a demangled name can be any sequence
     * of any number of any unicode characters.
     * @return method name components specified by the call site
     */
    public Object[] nameComponents() {
        return BytecodeName.parseBytecodeName(name);
    }

    /**
     * Report the resolved result and parameter types of this call site,
     * which are derived from its bytecode-level invocation descriptor.
     * The types are packaged into a {@link MethodType}.
     * Any linked target of this call site must be exactly this method type.
     * This is immutable static context.
     * @return method type specified by the call site
     */
    public MethodType type() {
        return type;
    }

    @Override
    public String toString() {
        return "CallSite#"+hashCode()+"["+name+type+" => "+target+"]";
    }
}

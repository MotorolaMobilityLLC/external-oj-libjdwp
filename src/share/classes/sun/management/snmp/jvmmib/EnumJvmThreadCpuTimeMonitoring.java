/*
 * Copyright (c) 2003, 2012, Oracle and/or its affiliates. All rights reserved.
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

package sun.management.snmp.jvmmib;

//
// Generated by mibgen version 5.0 (06/02/03) when compiling JVM-MANAGEMENT-MIB.
//

// java imports
//
import java.io.Serializable;
import java.util.Hashtable;

// RI imports
//
import com.sun.jmx.snmp.Enumerated;

/**
 * The class is used for representing "JvmThreadCpuTimeMonitoring".
 */
public class EnumJvmThreadCpuTimeMonitoring extends Enumerated implements Serializable {

    static final long serialVersionUID = -532837824105215699L;
    protected static Hashtable<Integer, String> intTable =
            new Hashtable<>();
    protected static Hashtable<String, Integer> stringTable =
            new Hashtable<>();
    static  {
        intTable.put(new Integer(3), "enabled");
        intTable.put(new Integer(4), "disabled");
        intTable.put(new Integer(1), "unsupported");
        stringTable.put("enabled", new Integer(3));
        stringTable.put("disabled", new Integer(4));
        stringTable.put("unsupported", new Integer(1));
    }

    public EnumJvmThreadCpuTimeMonitoring(int valueIndex) throws IllegalArgumentException {
        super(valueIndex);
    }

    public EnumJvmThreadCpuTimeMonitoring(Integer valueIndex) throws IllegalArgumentException {
        super(valueIndex);
    }

    public EnumJvmThreadCpuTimeMonitoring() throws IllegalArgumentException {
        super();
    }

    public EnumJvmThreadCpuTimeMonitoring(String x) throws IllegalArgumentException {
        super(x);
    }

    protected Hashtable<Integer,String> getIntTable() {
        return intTable ;
    }

    protected Hashtable<String,Integer> getStringTable() {
        return stringTable ;
    }

}

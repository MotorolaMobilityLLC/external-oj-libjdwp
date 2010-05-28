/*
 * Copyright (c) 2003, 2004, Oracle and/or its affiliates. All rights reserved.
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

// jmx imports
//
import com.sun.jmx.snmp.SnmpOidRecord;

// jdmk imports
//
import com.sun.jmx.snmp.SnmpOidTableSupport;

/**
 * The class contains metadata definitions for "JVM-MANAGEMENT-MIB".
 * Call SnmpOid.setSnmpOidTable(new JVM_MANAGEMENT_MIBOidTable()) to load the metadata in the SnmpOidTable.
 */
public class JVM_MANAGEMENT_MIBOidTable extends SnmpOidTableSupport implements Serializable {

    /**
     * Default constructor. Initialize the Mib tree.
     */
    public JVM_MANAGEMENT_MIBOidTable() {
        super("JVM_MANAGEMENT_MIB");
        loadMib(varList);
    }

    static SnmpOidRecord varList [] = {
        new SnmpOidRecord("jvmOSProcessorCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.6.4", "I"),
        new SnmpOidRecord("jvmOSVersion", "1.3.6.1.4.1.42.2.145.3.163.1.1.6.3", "S"),
        new SnmpOidRecord("jvmOSArch", "1.3.6.1.4.1.42.2.145.3.163.1.1.6.2", "S"),
        new SnmpOidRecord("jvmOSName", "1.3.6.1.4.1.42.2.145.3.163.1.1.6.1", "S"),
        new SnmpOidRecord("jvmJITCompilerTimeMonitoring", "1.3.6.1.4.1.42.2.145.3.163.1.1.5.3", "I"),
        new SnmpOidRecord("jvmJITCompilerTimeMs", "1.3.6.1.4.1.42.2.145.3.163.1.1.5.2", "C64"),
        new SnmpOidRecord("jvmJITCompilerName", "1.3.6.1.4.1.42.2.145.3.163.1.1.5.1", "S"),
        new SnmpOidRecord("jvmRTLibraryPathTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.23", "TA"),
        new SnmpOidRecord("jvmRTLibraryPathEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.23.1", "EN"),
        new SnmpOidRecord("jvmRTLibraryPathItem", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.23.1.2", "S"),
        new SnmpOidRecord("jvmRTLibraryPathIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.23.1.1", "I"),
        new SnmpOidRecord("jvmRTClassPathTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.22", "TA"),
        new SnmpOidRecord("jvmRTClassPathEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.22.1", "EN"),
        new SnmpOidRecord("jvmRTClassPathItem", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.22.1.2", "S"),
        new SnmpOidRecord("jvmRTClassPathIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.22.1.1", "I"),
        new SnmpOidRecord("jvmRTBootClassPathTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.21", "TA"),
        new SnmpOidRecord("jvmRTBootClassPathEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.21.1", "EN"),
        new SnmpOidRecord("jvmRTBootClassPathItem", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.21.1.2", "S"),
        new SnmpOidRecord("jvmRTBootClassPathIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.21.1.1", "I"),
        new SnmpOidRecord("jvmRTBootClassPathSupport", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.9", "I"),
        new SnmpOidRecord("jvmRTInputArgsTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.20", "TA"),
        new SnmpOidRecord("jvmRTInputArgsEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.20.1", "EN"),
        new SnmpOidRecord("jvmRTInputArgsItem", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.20.1.2", "S"),
        new SnmpOidRecord("jvmRTInputArgsIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.20.1.1", "I"),
        new SnmpOidRecord("jvmRTManagementSpecVersion", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.8", "S"),
        new SnmpOidRecord("jvmRTSpecVersion", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.7", "S"),
        new SnmpOidRecord("jvmRTSpecVendor", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.6", "S"),
        new SnmpOidRecord("jvmRTSpecName", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.5", "S"),
        new SnmpOidRecord("jvmRTVMVersion", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.4", "S"),
        new SnmpOidRecord("jvmRTVMVendor", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.3", "S"),
        new SnmpOidRecord("jvmRTStartTimeMs", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.12", "C64"),
        new SnmpOidRecord("jvmRTUptimeMs", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.11", "C64"),
        new SnmpOidRecord("jvmRTVMName", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.2", "S"),
        new SnmpOidRecord("jvmRTName", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.1", "S"),
        new SnmpOidRecord("jvmRTInputArgsCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.4.10", "I"),
        new SnmpOidRecord("jvmThreadCpuTimeMonitoring", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.6", "I"),
        new SnmpOidRecord("jvmThreadContentionMonitoring", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.5", "I"),
        new SnmpOidRecord("jvmThreadTotalStartedCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.4", "C64"),
        new SnmpOidRecord("jvmThreadPeakCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.3", "C"),
        new SnmpOidRecord("jvmThreadDaemonCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.2", "G"),
        new SnmpOidRecord("jvmThreadCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.1", "G"),
        new SnmpOidRecord("jvmThreadInstanceTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10", "TA"),
        new SnmpOidRecord("jvmThreadInstanceEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1", "EN"),
        new SnmpOidRecord("jvmThreadInstName", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.9", "S"),
        new SnmpOidRecord("jvmThreadInstCpuTimeNs", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.8", "C64"),
        new SnmpOidRecord("jvmThreadInstWaitTimeMs", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.7", "C64"),
        new SnmpOidRecord("jvmThreadInstWaitCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.6", "C64"),
        new SnmpOidRecord("jvmThreadInstBlockTimeMs", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.5", "C64"),
        new SnmpOidRecord("jvmThreadInstBlockCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.4", "C64"),
        new SnmpOidRecord("jvmThreadInstState", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.3", "S"),
        new SnmpOidRecord("jvmThreadInstLockOwnerPtr", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.11", "OI"),
        new SnmpOidRecord("jvmThreadInstId", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.2", "C64"),
        new SnmpOidRecord("jvmThreadInstLockName", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.10", "S"),
        new SnmpOidRecord("jvmThreadInstIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.10.1.1", "S"),
        new SnmpOidRecord("jvmThreadPeakCountReset", "1.3.6.1.4.1.42.2.145.3.163.1.1.3.7", "C64"),
        new SnmpOidRecord("jvmMemMgrPoolRelTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.120", "TA"),
        new SnmpOidRecord("jvmMemMgrPoolRelEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.120.1", "EN"),
        new SnmpOidRecord("jvmMemMgrRelPoolName", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.120.1.3", "S"),
        new SnmpOidRecord("jvmMemMgrRelManagerName", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.120.1.2", "S"),
        new SnmpOidRecord("jvmMemoryNonHeapMaxSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.23", "C64"),
        new SnmpOidRecord("jvmMemoryNonHeapCommitted", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.22", "C64"),
        new SnmpOidRecord("jvmMemoryNonHeapUsed", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.21", "C64"),
        new SnmpOidRecord("jvmMemPoolTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110", "TA"),
        new SnmpOidRecord("jvmMemPoolEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1", "EN"),
        new SnmpOidRecord("jvmMemPoolCollectMaxSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.33", "C64"),
        new SnmpOidRecord("jvmMemPoolCollectCommitted", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.32", "C64"),
        new SnmpOidRecord("jvmMemPoolCollectUsed", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.31", "C64"),
        new SnmpOidRecord("jvmMemPoolCollectThreshdSupport", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.133", "I"),
        new SnmpOidRecord("jvmMemPoolCollectThreshdCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.132", "C64"),
        new SnmpOidRecord("jvmMemPoolCollectThreshold", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.131", "C64"),
        new SnmpOidRecord("jvmMemPoolMaxSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.13", "C64"),
        new SnmpOidRecord("jvmMemPoolCommitted", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.12", "C64"),
        new SnmpOidRecord("jvmMemPoolUsed", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.11", "C64"),
        new SnmpOidRecord("jvmMemPoolInitSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.10", "C64"),
        new SnmpOidRecord("jvmMemPoolThreshdSupport", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.112", "I"),
        new SnmpOidRecord("jvmMemPoolThreshdCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.111", "C64"),
        new SnmpOidRecord("jvmMemPoolThreshold", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.110", "C64"),
        new SnmpOidRecord("jvmMemPoolPeakReset", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.5", "C64"),
        new SnmpOidRecord("jvmMemPoolState", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.4", "I"),
        new SnmpOidRecord("jvmMemPoolType", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.3", "I"),
        new SnmpOidRecord("jvmMemPoolName", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.2", "S"),
        new SnmpOidRecord("jvmMemPoolPeakMaxSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.23", "C64"),
        new SnmpOidRecord("jvmMemPoolIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.1", "I"),
        new SnmpOidRecord("jvmMemPoolPeakCommitted", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.22", "C64"),
        new SnmpOidRecord("jvmMemPoolPeakUsed", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.110.1.21", "C64"),
        new SnmpOidRecord("jvmMemoryNonHeapInitSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.20", "C64"),
        new SnmpOidRecord("jvmMemoryHeapMaxSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.13", "C64"),
        new SnmpOidRecord("jvmMemoryHeapCommitted", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.12", "C64"),
        new SnmpOidRecord("jvmMemoryGCCall", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.3", "I"),
        new SnmpOidRecord("jvmMemoryHeapUsed", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.11", "C64"),
        new SnmpOidRecord("jvmMemoryGCVerboseLevel", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.2", "I"),
        new SnmpOidRecord("jvmMemGCTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.101", "TA"),
        new SnmpOidRecord("jvmMemGCEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.101.1", "EN"),
        new SnmpOidRecord("jvmMemGCTimeMs", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.101.1.3", "C64"),
        new SnmpOidRecord("jvmMemGCCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.101.1.2", "C64"),
        new SnmpOidRecord("jvmMemoryHeapInitSize", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.10", "C64"),
        new SnmpOidRecord("jvmMemoryPendingFinalCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.1", "G"),
        new SnmpOidRecord("jvmMemManagerTable", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.100", "TA"),
        new SnmpOidRecord("jvmMemManagerEntry", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.100.1", "EN"),
        new SnmpOidRecord("jvmMemManagerState", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.100.1.3", "I"),
        new SnmpOidRecord("jvmMemManagerName", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.100.1.2", "S"),
        new SnmpOidRecord("jvmMemManagerIndex", "1.3.6.1.4.1.42.2.145.3.163.1.1.2.100.1.1", "I"),
        new SnmpOidRecord("jvmClassesVerboseLevel", "1.3.6.1.4.1.42.2.145.3.163.1.1.1.4", "I"),
        new SnmpOidRecord("jvmClassesUnloadedCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.1.3", "C64"),
        new SnmpOidRecord("jvmClassesTotalLoadedCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.1.2", "C64"),
        new SnmpOidRecord("jvmClassesLoadedCount", "1.3.6.1.4.1.42.2.145.3.163.1.1.1.1", "G"),
        new SnmpOidRecord("jvmLowMemoryPoolUsageNotif", "1.3.6.1.4.1.42.2.145.3.163.1.2.2.1.0.1", "NT"),
        new SnmpOidRecord("jvmLowMemoryPoolCollectNotif", "1.3.6.1.4.1.42.2.145.3.163.1.2.2.1.0.2", "NT")    };
}

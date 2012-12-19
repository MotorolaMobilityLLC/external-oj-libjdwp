/*
 * Copyright (c) 1997, 2012, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.jmx.snmp.agent;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.Vector;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;

import static com.sun.jmx.defaults.JmxProperties.SNMP_ADAPTOR_LOGGER;
import com.sun.jmx.snmp.SnmpOid;
import com.sun.jmx.snmp.SnmpVarBind;
import com.sun.jmx.snmp.SnmpDefinitions;
import com.sun.jmx.snmp.SnmpStatusException;

/**
 * Abstract class for representing an SNMP MIB.
 * <P>
 * When compiling a SNMP MIB, among all the classes generated by
 * <CODE>mibgen</CODE>, there is one which extends <CODE>SnmpMib</CODE>
 * for representing a whole MIB.
 * <BR>The class is used by the SNMP protocol adaptor as the entry point in
 * the MIB.
 *
 * <p>This generated class can be subclassed in your code in order to
 * plug in your own specific behaviour.
 * </p>
 *
 * <p><b>This API is a Sun Microsystems internal API  and is subject
 * to change without notice.</b></p>
 */
public abstract class SnmpMib extends SnmpMibAgent implements Serializable {

    /**
     * Default constructor.
     * Initializes the OID tree.
     */
    public SnmpMib() {
        root= new SnmpMibOid();
    }


    // --------------------------------------------------------------------
    // POLYMORHIC METHODS
    // --------------------------------------------------------------------

    /**
     * <p>
     * This callback should return the OID associated to the group
     * identified by the given <code>groupName</code>.
     * </p>
     *
     * <p>
     * This method is provided as a hook to plug-in some custom
     * specific behavior. Although doing so is discouraged you might
     * want to subclass this method in order to store & provide more metadata
     * information (mapping OID <-> symbolic name) within the agent,
     * or to "change" the root of the MIB OID by prefixing the
     * defaultOid by an application dependant OID string, for instance.
     * </p>
     *
     * <p>
     * The default implementation of this method is to return the given
     * <code>defaultOid</code>
     * </p>
     *
     * @param groupName   The java-ized name of the SNMP group.
     * @param defaultOid  The OID defined in the MIB for that group
     *                    (in dot notation).
     *
     * @return The OID of the group identified by <code>groupName</code>,
     *         in dot-notation.
     */
    protected String getGroupOid(String groupName, String defaultOid) {
        return defaultOid;
    }

    /**
     * <p>
     * This callback should return the ObjectName associated to the
     * group identified by the given <code>groupName</code>.
     * </p>
     *
     * <p>
     * This method is provided as a hook to plug-in some custom
     * specific behavior. You might want to override this method
     * in order to provide a different object naming scheme than
     * that proposed by default by <code>mibgen</code>.
     * </p>
     *
     * <p>
     * This method is only meaningful if the MIB is registered
     * in the MBeanServer, otherwise, it will not be called.
     * </p>
     *
     * <p>
     * The default implementation of this method is to return an ObjectName
     * built from the given <code>defaultName</code>.
     * </p>
     *
     * @param name  The java-ized name of the SNMP group.
     * @param oid   The OID returned by getGroupOid() - in dot notation.
     * @param defaultName The name by default generated by <code>
     *                    mibgen</code>
     *
     * @return The ObjectName of the group identified by <code>name</code>
     */
    protected ObjectName getGroupObjectName(String name, String oid,
                                            String defaultName)
        throws MalformedObjectNameException {
        return new ObjectName(defaultName);
    }

    /**
     * <p>
     * Register an SNMP group and its metadata node in the MIB.
     * </p>
     *
     * <p>
     * This method is provided as a hook to plug-in some custom
     * specific behavior. You might want to override this method
     * if you want to set special links between the MBean, its metadata
     * node, its OID or ObjectName etc..
     * </p>
     *
     * <p>
     * If the MIB is not registered in the MBeanServer, the <code>
     * server</code> and <code>groupObjName</code> parameters will be
     * <code>null</code>.<br>
     * If the given group MBean is not <code>null</code>, and if the
     * <code>server</code> and <code>groupObjName</code> parameters are
     * not null, then this method will also automatically register the
     * group MBean with the given MBeanServer <code>server</code>.
     * </p>
     *
     * @param groupName  The java-ized name of the SNMP group.
     * @param groupOid   The OID as returned by getGroupOid() - in dot
     *                   notation.
     * @param groupObjName The ObjectName as returned by getGroupObjectName().
     *                   This parameter may be <code>null</code> if the
     *                   MIB is not registered in the MBeanServer.
     * @param node       The metadata node, as returned by the metadata
     *                   factory method for this group.
     * @param group      The MBean for this group, as returned by the
     *                   MBean factory method for this group.
     * @param server     The MBeanServer in which the groups are to be
     *                   registered. This parameter will be <code>null</code>
     *                   if the MIB is not registered, otherwise it is a
     *                   reference to the MBeanServer in which the MIB is
     *                   registered.
     *
     */
    protected void registerGroupNode(String groupName,   String groupOid,
                                     ObjectName groupObjName, SnmpMibNode node,
                                     Object group, MBeanServer server)
        throws NotCompliantMBeanException, MBeanRegistrationException,
        InstanceAlreadyExistsException, IllegalAccessException {
        root.registerNode(groupOid,node);
        if (server != null && groupObjName != null && group != null)
            server.registerMBean(group,groupObjName);
    }

    /**
     * <p>
     * Register an SNMP Table metadata node in the MIB.
     * </p>
     *
     * <p>
     * <b><i>
     * This method is used internally and you should never need to
     * call it directly.</i></b><br> It is used to establish the link
     * between an SNMP table metadata node and its bean-like counterpart.
     * <br>
     * The group metadata nodes will create and register their
     * underlying table metadata nodes in the MIB using this
     * method. <br>
     * The metadata nodes will be later retrieved from the MIB by the
     * bean-like table objects using the getRegisterTableMeta() method.
     * </p>
     *
     * @param name      The java-ized name of the SNMP table.
     * @param table     The SNMP table metadata node - usually this
     *                  corresponds to a <code>mibgen</code> generated
     *                  object.
     */
    public abstract void registerTableMeta(String name, SnmpMibTable table);

    /**
     * Returns a registered SNMP Table metadata node.
     *
     * <p><b><i>
     * This method is used internally and you should never need to
     * call it directly.
     * </i></b></p>
     *
     */
    public abstract SnmpMibTable getRegisteredTableMeta(String name);

    // --------------------------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------------------------

    /**
     * Processes a <CODE>get</CODE> operation.
     *
     **/
    // Implements the method defined in SnmpMibAgent. See SnmpMibAgent
    // for java-doc
    //
    @Override
    public void get(SnmpMibRequest req) throws SnmpStatusException {

        // Builds the request tree: creation is not allowed, operation
        // is not atomic.

        final int reqType = SnmpDefinitions.pduGetRequestPdu;
        SnmpRequestTree handlers = getHandlers(req,false,false,reqType);

        SnmpRequestTree.Handler h = null;
        SnmpMibNode meta = null;

        if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
            SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(),
                    "get", "Processing handlers for GET... ");
        }

        // For each sub-request stored in the request-tree, invoke the
        // get() method.
        for (Enumeration<SnmpRequestTree.Handler> eh=handlers.getHandlers();eh.hasMoreElements();) {
            h = eh.nextElement();

            // Gets the Meta node. It can be either a Group Meta or a
            // Table Meta.
            //
            meta = handlers.getMetaNode(h);

            // Gets the depth of the Meta node in the OID tree
            final int depth = handlers.getOidDepth(h);

            for (Enumeration<SnmpMibSubRequest> rqs=handlers.getSubRequests(h);
                 rqs.hasMoreElements();) {

                // Invoke the get() operation.
                meta.get(rqs.nextElement(),depth);
            }
        }
    }

    /**
     * Processes a <CODE>set</CODE> operation.
     *
     */
    // Implements the method defined in SnmpMibAgent. See SnmpMibAgent
    // for java-doc
    //
    @Override
    public void set(SnmpMibRequest req) throws SnmpStatusException {

        SnmpRequestTree handlers = null;

        // Optimization: we're going to get the whole SnmpRequestTree
        // built in the "check" method, so that we don't have to rebuild
        // it here.
        //
        if (req instanceof SnmpMibRequestImpl)
            handlers = ((SnmpMibRequestImpl)req).getRequestTree();

        // Optimization didn't work: we have to rebuild the tree.
        //
        // Builds the request tree: creation is not allowed, operation
        // is atomic.
        //
        final int reqType = SnmpDefinitions.pduSetRequestPdu;
        if (handlers == null) handlers = getHandlers(req,false,true,reqType);
        handlers.switchCreationFlag(false);
        handlers.setPduType(reqType);

        SnmpRequestTree.Handler h;
        SnmpMibNode meta;

        if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
            SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(),
                    "set", "Processing handlers for SET... ");
        }

        // For each sub-request stored in the request-tree, invoke the
        // get() method.
        for (Enumeration<SnmpRequestTree.Handler> eh=handlers.getHandlers();eh.hasMoreElements();) {
            h = eh.nextElement();

            // Gets the Meta node. It can be either a Group Meta or a
            // Table Meta.
            //
            meta = handlers.getMetaNode(h);

            // Gets the depth of the Meta node in the OID tree
            final int depth = handlers.getOidDepth(h);

            for (Enumeration<SnmpMibSubRequest> rqs=handlers.getSubRequests(h);
                 rqs.hasMoreElements();) {

                // Invoke the set() operation
                meta.set(rqs.nextElement(),depth);
            }
        }
    }

    /**
     * Checks if a <CODE>set</CODE> operation can be performed.
     * If the operation cannot be performed, the method will raise a
     * <CODE>SnmpStatusException</CODE>.
     *
     */
    // Implements the method defined in SnmpMibAgent. See SnmpMibAgent
    // for java-doc
    //
    @Override
    public void check(SnmpMibRequest req) throws SnmpStatusException {

        final int reqType = SnmpDefinitions.pduWalkRequest;
        // Builds the request tree: creation is allowed, operation
        // is atomic.
        SnmpRequestTree handlers = getHandlers(req,true,true,reqType);

        SnmpRequestTree.Handler h;
        SnmpMibNode meta;

        if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
            SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(),
                    "check", "Processing handlers for CHECK... ");
        }

        // For each sub-request stored in the request-tree, invoke the
        // check() method.
        for (Enumeration<SnmpRequestTree.Handler> eh=handlers.getHandlers();eh.hasMoreElements();) {
            h = eh.nextElement();

            // Gets the Meta node. It can be either a Group Meta or a
            // Table Meta.
            //
            meta = handlers.getMetaNode(h);

            // Gets the depth of the Meta node in the OID tree
            final int depth = handlers.getOidDepth(h);

            for (Enumeration<SnmpMibSubRequest> rqs=handlers.getSubRequests(h);
                 rqs.hasMoreElements();) {

                // Invoke the check() operation
                meta.check(rqs.nextElement(),depth);
            }
        }

        // Optimization: we're going to pass the whole SnmpRequestTree
        // to the "set" method, so that we don't have to rebuild it there.
        //
        if (req instanceof SnmpMibRequestImpl) {
            ((SnmpMibRequestImpl)req).setRequestTree(handlers);
        }

    }

    /**
     * Processes a <CODE>getNext</CODE> operation.
     *
     */
    // Implements the method defined in SnmpMibAgent. See SnmpMibAgent
    // for java-doc
    //
    @Override
    public void getNext(SnmpMibRequest req) throws SnmpStatusException {
        // Build the request tree for the operation
        // The subrequest stored in the request tree are valid GET requests
        SnmpRequestTree handlers = getGetNextHandlers(req);

        SnmpRequestTree.Handler h;
        SnmpMibNode meta;

        if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
            SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(),
                    "getNext", "Processing handlers for GET-NEXT... ");
        }

        // Now invoke get() for each subrequest of the request tree.
        for (Enumeration<SnmpRequestTree.Handler> eh=handlers.getHandlers();eh.hasMoreElements();) {
            h = eh.nextElement();

            // Gets the Meta node. It can be either a Group Meta or a
            // Table Meta.
            //
            meta = handlers.getMetaNode(h);

            // Gets the depth of the Meta node in the OID tree
            int depth = handlers.getOidDepth(h);

            for (Enumeration<SnmpMibSubRequest> rqs=handlers.getSubRequests(h);
                 rqs.hasMoreElements();) {

                // Invoke the get() operation
                meta.get(rqs.nextElement(),depth);
            }
        }
    }


    /**
     * Processes a <CODE>getBulk</CODE> operation.
     * The method implements the <CODE>getBulk</CODE> operation by calling
     * appropriately the <CODE>getNext</CODE> method.
     *
     */
    // Implements the method defined in SnmpMibAgent. See SnmpMibAgent
    // for java-doc
    //
    @Override
    public void getBulk(SnmpMibRequest req, int nonRepeat, int maxRepeat)
        throws SnmpStatusException {

        getBulkWithGetNext(req, nonRepeat, maxRepeat);
    }

    /**
     * Gets the root object identifier of the MIB.
     * <P>In order to be accurate, the method should be called once the
     * MIB is fully initialized (that is, after a call to <CODE>init</CODE>
     * or <CODE>preRegister</CODE>).
     *
     * @return The root object identifier.
     */
    @Override
    public long[] getRootOid() {

        if( rootOid == null) {
            Vector<Integer> list= new Vector<>(10);

            // Ask the tree to do the job !
            //
            root.getRootOid(list);

            // Now format the result
            //
            rootOid= new long[list.size()];
            int i=0;
            for(Enumeration<Integer> e= list.elements(); e.hasMoreElements(); ) {
                Integer val= e.nextElement();
                rootOid[i++]= val.longValue();
            }
        }
        return rootOid;

    }

    // --------------------------------------------------------------------
    // PRIVATE METHODS
    //---------------------------------------------------------------------

    /**
     * This method builds the temporary request-tree that will be used to
     * perform the SNMP request associated with the given vector of varbinds
     * `list'.
     *
     * @param req The SnmpMibRequest object holding the varbind list
     *             concerning this MIB.
     * @param createflag Indicates whether the operation allow for creation
     *        of new instances (ie: it is a SET).
     * @param atomic Indicates whether the operation is atomic or not.
     * @param type Request type (from SnmpDefinitions).
     *
     * @return The request-tree where the original varbind list has been
     *         dispatched to the appropriate nodes.
     */
    private SnmpRequestTree getHandlers(SnmpMibRequest req,
                                        boolean createflag, boolean atomic,
                                        int type)
        throws SnmpStatusException {

        // Build an empty request tree
        SnmpRequestTree handlers =
            new SnmpRequestTree(req,createflag,type);

        int index=0;
        SnmpVarBind var;
        final int ver= req.getVersion();

        // For each varbind in the list finds its handling node.
        for (Enumeration<SnmpVarBind> e= req.getElements(); e.hasMoreElements(); index++) {

            var= e.nextElement();

            try {
                // Find the handling node for this varbind.
                root.findHandlingNode(var,var.oid.longValue(false),
                                      0,handlers);
            } catch(SnmpStatusException x) {

                if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                    SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                            SnmpMib.class.getName(),
                            "getHandlers",
                            "Couldn't find a handling node for " +
                            var.oid.toString());
                }

                // If the operation is atomic (Check/Set) or the version
                // is V1 we must generate an exception.
                //
                if (ver == SnmpDefinitions.snmpVersionOne) {

                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getHandlers", "\tV1: Throwing exception");
                    }

                    // The index in the exception must correspond to the
                    // SNMP index ...
                    //
                    final SnmpStatusException sse =
                        new SnmpStatusException(x, index + 1);
                    sse.initCause(x);
                    throw sse;
                } else if ((type == SnmpDefinitions.pduWalkRequest)   ||
                           (type == SnmpDefinitions.pduSetRequestPdu)) {
                    final int status =
                        SnmpRequestTree.mapSetException(x.getStatus(),ver);

                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getHandlers", "\tSET: Throwing exception");
                    }

                    final SnmpStatusException sse =
                        new SnmpStatusException(status, index + 1);
                    sse.initCause(x);
                    throw sse;
                } else if (atomic) {

                    // Should never come here...
                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getHandlers", "\tATOMIC: Throwing exception");
                    }

                    final SnmpStatusException sse =
                        new SnmpStatusException(x, index + 1);
                    sse.initCause(x);
                    throw sse;
                }

                final int status =
                    SnmpRequestTree.mapGetException(x.getStatus(),ver);

                if (status == SnmpStatusException.noSuchInstance) {

                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getHandlers",
                                "\tGET: Registering noSuchInstance");
                    }

                    var.value= SnmpVarBind.noSuchInstance;

                } else if (status == SnmpStatusException.noSuchObject) {
                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getHandlers",
                                "\tGET: Registering noSuchObject");
                    }

                        var.value= SnmpVarBind.noSuchObject;

                } else {

                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getHandlers",
                                "\tGET: Registering global error: " + status);
                    }

                    final SnmpStatusException sse =
                        new SnmpStatusException(status, index + 1);
                    sse.initCause(x);
                    throw sse;
                }
            }
        }
        return handlers;
    }

    /**
     * This method builds the temporary request-tree that will be used to
     * perform the SNMP GET-NEXT request associated with the given vector
     * of varbinds `list'.
     *
     * @param req The SnmpMibRequest object holding the varbind list
     *             concerning this MIB.
     *
     * @return The request-tree where the original varbind list has been
     *         dispatched to the appropriate nodes, and where the original
     *         OIDs have been replaced with the correct "next" OID.
     */
    private SnmpRequestTree getGetNextHandlers(SnmpMibRequest req)
        throws SnmpStatusException {

        // Creates an empty request tree, no entry creation is allowed (false)
        SnmpRequestTree handlers = new
            SnmpRequestTree(req,false,SnmpDefinitions.pduGetNextRequestPdu);

        // Sets the getNext flag: if version=V2, status exception are
        // transformed in  endOfMibView
        handlers.setGetNextFlag();

        if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
            SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(),
                    "getGetNextHandlers", "Received MIB request : " + req);
        }
        AcmChecker checker = new AcmChecker(req);
        int index=0;
        SnmpVarBind var = null;
        final int ver= req.getVersion();
        SnmpOid original = null;
        // For each varbind, finds the handling node.
        // This function has the side effect of transforming a GET-NEXT
        // request into a valid GET request, replacing the OIDs in the
        // original GET-NEXT request with the OID of the first leaf that
        // follows.
        for (Enumeration<SnmpVarBind> e= req.getElements(); e.hasMoreElements(); index++) {

            var = e.nextElement();
            SnmpOid result;
            try {
                // Find the node handling the OID that follows the varbind
                // OID. `result' contains this next leaf OID.
                //ACM loop.
                if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                    SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                            SnmpMib.class.getName(),
                            "getGetNextHandlers", " Next OID of : " + var.oid);
                }
                result = new SnmpOid(root.findNextHandlingNode
                                     (var,var.oid.longValue(false),0,
                                      0,handlers, checker));

                if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                    SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                            SnmpMib.class.getName(),
                            "getGetNextHandlers", " is : " + result);
                }
                // We replace the varbind original OID with the OID of the
                // leaf object we have to return.
                var.oid = result;
            } catch(SnmpStatusException x) {

                // if (isDebugOn())
                //    debug("getGetNextHandlers",
                //        "Couldn't find a handling node for "
                //        + var.oid.toString());

                if (ver == SnmpDefinitions.snmpVersionOne) {
                    if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                        SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                                SnmpMib.class.getName(),
                                "getGetNextHandlers",
                                "\tThrowing exception " + x.toString());
                    }
                    // The index in the exception must correspond to the
                    // SNMP index ...
                    //
                    throw new SnmpStatusException(x, index + 1);
                }
                if (SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
                    SNMP_ADAPTOR_LOGGER.logp(Level.FINEST,
                            SnmpMib.class.getName(),
                            "getGetNextHandlers",
                            "Exception : " + x.getStatus());
                }

                var.setSnmpValue(SnmpVarBind.endOfMibView);
            }
        }
        return handlers;
    }

    // --------------------------------------------------------------------
    // PROTECTED VARIABLES
    // --------------------------------------------------------------------

    /**
     * The top element in the Mib tree.
     * @serial
     */
    protected SnmpMibOid root;


    // --------------------------------------------------------------------
    // PRIVATE VARIABLES
    // --------------------------------------------------------------------

    /**
     * The root object identifier of the MIB.
     */
    private transient long[] rootOid= null;
}

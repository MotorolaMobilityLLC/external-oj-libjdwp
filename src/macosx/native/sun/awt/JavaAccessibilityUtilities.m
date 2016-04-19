/*
 * Copyright (c) 2011, 2016, Oracle and/or its affiliates. All rights reserved.
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

#import "JavaAccessibilityUtilities.h"

#import <AppKit/AppKit.h>
#import <JavaNativeFoundation/JavaNativeFoundation.h>


static BOOL JavaAccessibilityIsSupportedAttribute(id element, NSString *attribute);
static void JavaAccessibilityLogError(NSString *message);
static void _JavaAccessibilityRaiseException(NSString *reason, SInt32 errorCode);
static NSString *AttributeWithoutAXPrefix(NSString *attribute);
static SEL JavaAccessibilityAttributeGetter(NSString *attribute);
static SEL JavaAccessibilityAttributeSettableTester(NSString *attribute);
static SEL JavaAccessibilityAttributeSetter(NSString *attribute);

NSString *const JavaAccessibilityIgnore = @"JavaAxIgnore";

NSMutableDictionary *sRoles = nil;
void initializeRoles();

// Unique
static JNF_CLASS_CACHE(sjc_AccessibleState, "javax/accessibility/AccessibleState");

// Duplicate
JNF_CLASS_CACHE(sjc_CAccessibility, "sun/lwawt/macosx/CAccessibility");
JNF_CLASS_CACHE(sjc_AccessibleComponent, "javax/accessibility/AccessibleComponent");
JNF_CLASS_CACHE(sjc_AccessibleContext, "javax/accessibility/AccessibleContext");
JNF_CLASS_CACHE(sjc_Accessible, "javax/accessibility/Accessible");
JNF_CLASS_CACHE(sjc_AccessibleRole, "javax/accessibility/AccessibleRole");
JNF_CLASS_CACHE(sjc_Point, "java/awt/Point");
JNF_CLASS_CACHE(sjc_AccessibleText, "javax/accessibility/AccessibleText");

JNF_MEMBER_CACHE(sjf_key, sjc_AccessibleRole, "key", "Ljava/lang/String;");
JNF_MEMBER_CACHE(sjf_X, sjc_Point, "x", "I");
JNF_MEMBER_CACHE(sjf_Y, sjc_Point, "y", "I");

NSSize getAxComponentSize(JNIEnv *env, jobject axComponent, jobject component)
{
    static JNF_CLASS_CACHE(jc_Dimension, "java/awt/Dimension");
    static JNF_MEMBER_CACHE(jf_width, jc_Dimension, "width", "I");
    static JNF_MEMBER_CACHE(jf_height, jc_Dimension, "height", "I");
    static JNF_STATIC_MEMBER_CACHE(jm_getSize, sjc_CAccessibility, "getSize", "(Ljavax/accessibility/AccessibleComponent;Ljava/awt/Component;)Ljava/awt/Dimension;");

    jobject dimension = CallStaticObjectMethodWeakly_LL(env, jm_getSize, axComponent, component); // AWT_THREADING Safe (AWTRunLoopMode)

    if (dimension == NULL) return NSZeroSize;
    return NSMakeSize(JNFGetIntField(env, dimension, jf_width), JNFGetIntField(env, dimension, jf_height));
}

NSString *getJavaRole(JNIEnv *env, jobject axComponent, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(sjm_getAccessibleRole, sjc_CAccessibility, "getAccessibleRole", "(Ljavax/accessibility/Accessible;Ljava/awt/Component;)Ljava/lang/String;");
    jobject axRole = CallStaticObjectMethodWeakly_LL(env, sjm_getAccessibleRole, axComponent, component); // AWT_THREADING Safe (AWTRunLoopMode)
    if (axRole == NULL) return @"unknown";

    NSString* str = JNFJavaToNSString(env, axRole);
    (*env)->DeleteLocalRef(env, axRole);
    return str;
}

jobject getAxSelection(JNIEnv *env, jobject axContext, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_getAccessibleSelection, sjc_CAccessibility, "getAccessibleSelection", "(Ljavax/accessibility/AccessibleContext;Ljava/awt/Component;)Ljavax/accessibility/AccessibleSelection;");
    return CallStaticObjectMethodWeakly_LL(env, jm_getAccessibleSelection, axContext, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

jobject getAxContextSelection(JNIEnv *env, jobject axContext, jint index, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_ax_getAccessibleSelection, sjc_CAccessibility, "ax_getAccessibleSelection", "(Ljavax/accessibility/AccessibleContext;ILjava/awt/Component;)Ljavax/accessibility/Accessible;");
    return CallStaticObjectMethodWeakly_LIL(env, jm_ax_getAccessibleSelection, axContext, index, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

void setAxContextSelection(JNIEnv *env, jobject axContext, jint index, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_addAccessibleSelection, sjc_CAccessibility, "addAccessibleSelection", "(Ljavax/accessibility/AccessibleContext;ILjava/awt/Component;)V");
    CallStaticVoidMethodWeakly_LIL(env, jm_addAccessibleSelection, axContext, index, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

jobject getAxContext(JNIEnv *env, jobject accessible, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_getAccessibleContext, sjc_CAccessibility, "getAccessibleContext", "(Ljavax/accessibility/Accessible;Ljava/awt/Component;)Ljavax/accessibility/AccessibleContext;");
    return CallStaticObjectMethodWeakly_LL(env, jm_getAccessibleContext, accessible, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

BOOL isChildSelected(JNIEnv *env, jobject accessible, jint index, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_isAccessibleChildSelected, sjc_CAccessibility, "isAccessibleChildSelected", "(Ljavax/accessibility/Accessible;ILjava/awt/Component;)Z");
    return CallStaticBooleanMethodWeakly_LIL(env, jm_isAccessibleChildSelected, accessible, index, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

jobject getAxStateSet(JNIEnv *env, jobject axContext, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_getAccessibleStateSet, sjc_CAccessibility, "getAccessibleStateSet", "(Ljavax/accessibility/AccessibleContext;Ljava/awt/Component;)Ljavax/accessibility/AccessibleStateSet;");
    return CallStaticObjectMethodWeakly_LL(env, jm_getAccessibleStateSet, axContext, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

BOOL containsAxState(JNIEnv *env, jobject axContext, jobject axState, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_contains, sjc_CAccessibility, "contains", "(Ljavax/accessibility/AccessibleContext;Ljavax/accessibility/AccessibleState;Ljava/awt/Component;)Z");
    return CallStaticBooleanMethodWeakly_LLL(env, jm_contains, axContext, axState, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

BOOL isVertical(JNIEnv *env, jobject axContext, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_VERTICAL, sjc_AccessibleState, "VERTICAL", "Ljavax/accessibility/AccessibleState;");
    jobject axVertState = JNFGetStaticObjectField(env, jm_VERTICAL);
    BOOL vertical = containsAxState(env, axContext, axVertState, component);
    (*env)->DeleteLocalRef(env, axVertState);
    return vertical;
}

BOOL isHorizontal(JNIEnv *env, jobject axContext, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_HORIZONTAL, sjc_AccessibleState, "HORIZONTAL", "Ljavax/accessibility/AccessibleState;");
    jobject axHorizState = JNFGetStaticObjectField(env, jm_HORIZONTAL);
    BOOL horizontal = containsAxState(env, axContext, axHorizState, component);
    (*env)->DeleteLocalRef(env, axHorizState);
    return horizontal;
}

BOOL isShowing(JNIEnv *env, jobject axContext, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_SHOWING, sjc_AccessibleState, "SHOWING", "Ljavax/accessibility/AccessibleState;");
    jobject axVisibleState = JNFGetStaticObjectField(env, jm_SHOWING);
    BOOL showing = containsAxState(env, axContext, axVisibleState, component);
    (*env)->DeleteLocalRef(env, axVisibleState);
    return showing;
}

NSPoint getAxComponentLocationOnScreen(JNIEnv *env, jobject axComponent, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_getLocationOnScreen, sjc_CAccessibility, "getLocationOnScreen", "(Ljavax/accessibility/AccessibleComponent;Ljava/awt/Component;)Ljava/awt/Point;");
    jobject jpoint = CallStaticObjectMethodWeakly_LL(env, jm_getLocationOnScreen, axComponent, component); // AWT_THREADING Safe (AWTRunLoopMode)
    if (jpoint == NULL) return NSZeroPoint;
    return NSMakePoint(JNFGetIntField(env, jpoint, sjf_X), JNFGetIntField(env, jpoint, sjf_Y));
}

jint getAxTextCharCount(JNIEnv *env, jobject axText, jobject component)
{
    static JNF_STATIC_MEMBER_CACHE(jm_getCharCount, sjc_CAccessibility, "getCharCount", "(Ljavax/accessibility/AccessibleText;Ljava/awt/Component;)I");
    return CallStaticIntMethodWeakly_LL(env, jm_getCharCount, axText, component); // AWT_THREADING Safe (AWTRunLoopMode)
}

// The following JavaAccessibility methods are copied from the corresponding
// NSAccessibility methods in NSAccessibility.m.
//
// They implement a key-value-like coding scheme to transform messages like
//        [self accessibilityAttributeValue:NSAccessibilityEnabledAttribute]
// into calls on to specific methods like
//        [self accessibilityEnabledAttribute].

static NSString *AttributeWithoutAXPrefix(NSString *attribute)
{
    return [attribute hasPrefix:@"AX"] ? [attribute substringFromIndex:2] : attribute;
}

static SEL JavaAccessibilityAttributeGetter(NSString *attribute)
{
    return NSSelectorFromString([NSString stringWithFormat:@"accessibility%@Attribute", AttributeWithoutAXPrefix(attribute)]);
}

static SEL JavaAccessibilityAttributeSettableTester(NSString *attribute)
{
    return NSSelectorFromString([NSString stringWithFormat:@"accessibilityIs%@AttributeSettable", AttributeWithoutAXPrefix(attribute)]);
}

static SEL JavaAccessibilityAttributeSetter(NSString *attribute)
{
    return NSSelectorFromString([NSString stringWithFormat:@"accessibilitySet%@Attribute:", AttributeWithoutAXPrefix(attribute)]);
}

id JavaAccessibilityAttributeValue(id element, NSString *attribute)
{
    if (!JavaAccessibilityIsSupportedAttribute(element, attribute)) return nil;

    SEL getter = JavaAccessibilityAttributeGetter(attribute);
#ifdef JAVA_AX_DEBUG_PARMS
    if (![element respondsToSelector:getter]) {
        JavaAccessibilityRaiseUnimplementedAttributeException(__FUNCTION__, element, attribute);
        return nil;
    }
#endif

    return [element performSelector:getter];
}

BOOL JavaAccessibilityIsAttributeSettable(id element, NSString *attribute)
{
    if (!JavaAccessibilityIsSupportedAttribute(element, attribute)) return NO;

    SEL tester = JavaAccessibilityAttributeSettableTester(attribute);
#ifdef JAVA_AX_DEBUG_PARMS
    if (![element respondsToSelector:tester]) {
        JavaAccessibilityRaiseUnimplementedAttributeException(__FUNCTION__, element, attribute);
        return NO;
    }
#endif

    return [element performSelector:tester] != nil;
}

void JavaAccessibilitySetAttributeValue(id element, NSString *attribute ,id value)
{
    if (!JavaAccessibilityIsSupportedAttribute(element, attribute)) return;

    SEL setter = JavaAccessibilityAttributeSetter(attribute);
    if (![element accessibilityIsAttributeSettable:attribute]) return;

#ifdef JAVA_AX_DEBUG_PARMS
    if (![element respondsToSelector:setter]) {
        JavaAccessibilityRaiseUnimplementedAttributeException(__FUNCTION__, element, attribute);
        return;
    }
#endif

    [element performSelector:setter withObject:value];
}

static BOOL JavaAccessibilityIsSupportedAttribute(id element, NSString *attribute)
{
    return [[element accessibilityAttributeNames] indexOfObject:attribute] != NSNotFound;
}

/*
 * Class:     sun_lwawt_macosx_CAccessibility
 * Method:    roleKey
 * Signature: (Ljavax/accessibility/AccessibleRole;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_sun_lwawt_macosx_CAccessibility_roleKey
(JNIEnv *env, jclass clz, jobject axRole)
{
    return JNFGetObjectField(env, axRole, sjf_key);
}


// errors from NSAccessibilityErrors
void JavaAccessibilityRaiseSetAttributeToIllegalTypeException(const char *functionName, id element, NSString *attribute, id value)
{
    NSString *reason = [NSString stringWithFormat:@"%s: Attempt set \"%@\" attribute to illegal type of value (%@:%@) for element: %@", functionName, attribute, [value class], value, element];
    _JavaAccessibilityRaiseException(reason, kAXErrorIllegalArgument);
}

void JavaAccessibilityRaiseUnimplementedAttributeException(const char *functionName, id element, NSString *attribute)
{
    NSString *reason = [NSString stringWithFormat:@"%s: \"%@\" attribute unimplemented by element: %@", functionName, attribute, element];
    _JavaAccessibilityRaiseException(reason, kAXErrorFailure);
}

void JavaAccessibilityRaiseIllegalParameterTypeException(const char *functionName, id element, NSString *attribute, id parameter)
{
    NSString *reason = [NSString stringWithFormat:@"%s: \"%@\" parameterized attribute passed illegal type of parameter (%@:%@) for element: %@", functionName, attribute, [parameter class], parameter, element];
    _JavaAccessibilityRaiseException(reason, kAXErrorIllegalArgument);
}

static void _JavaAccessibilityRaiseException(NSString *reason, SInt32 errorCode)
{
    JavaAccessibilityLogError(reason);
    [[NSException exceptionWithName:NSAccessibilityException reason:reason userInfo:[NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithInt:errorCode], NSAccessibilityErrorCodeExceptionInfo, nil]] raise];
}

static void JavaAccessibilityLogError(NSString *message)
{
    NSLog(@"!!! %@", message);
}

// end appKit copies

/*
 To get the roles below, verify the perl has table below called macRoleCodes is correct.
 Then copy the perl code into a perl script called makeAxTables.pl (make
 sure to chmod +x makeAxTables.pl). Then run the perl script like this:

 ./makeAxTables.pl /Builds/jdk1_4_1/

 It will then write the void initializeRoles() method below to stdout.

 Any new AccessibleRole items that aren't in the perl hash table will be written out as follows:
 // Unknown AccessibleRole: <role>

 Add these unknowns to the perl hash table and re-run the script, and use the new generated table.
*/

// NOTE: Don't modify this directly. It is machine generated. See below
void initializeRoles()
{
    sRoles = [[NSMutableDictionary alloc] initWithCapacity:56];

    [sRoles setObject:JavaAccessibilityIgnore forKey:@"alert"];
    [sRoles setObject:NSAccessibilityGroupRole forKey:@"awtcomponent"];
    [sRoles setObject:NSAccessibilityGroupRole forKey:@"canvas"];
    [sRoles setObject:NSAccessibilityCheckBoxRole forKey:@"checkbox"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"colorchooser"];
    [sRoles setObject:NSAccessibilityColumnRole forKey:@"columnheader"];
    [sRoles setObject:NSAccessibilityComboBoxRole forKey:@"combobox"];
    [sRoles setObject:NSAccessibilityTextFieldRole forKey:@"dateeditor"];
    [sRoles setObject:NSAccessibilityImageRole forKey:@"desktopicon"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"desktoppane"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"dialog"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"directorypane"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"filechooser"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"filler"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"fontchooser"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"frame"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"glasspane"];
    [sRoles setObject:NSAccessibilityGroupRole forKey:@"groupbox"];
    [sRoles setObject:NSAccessibilityStaticTextRole forKey:@"hyperlink"]; //maybe a group?
    [sRoles setObject:NSAccessibilityImageRole forKey:@"icon"];
    [sRoles setObject:NSAccessibilityGroupRole forKey:@"internalframe"];
    [sRoles setObject:NSAccessibilityStaticTextRole forKey:@"label"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"layeredpane"];
    [sRoles setObject:NSAccessibilityListRole forKey:@"list"]; // maybe a group? AccessibleRole.java says a list is: "An object that presents a list of objects to the user and allows the user to select one or more of them."
    [sRoles setObject:NSAccessibilityListRole forKey:@"listitem"];
    [sRoles setObject:NSAccessibilityMenuRole forKey:@"menu"];
    [sRoles setObject:NSAccessibilityMenuBarRole forKey:@"menubar"];
    [sRoles setObject:NSAccessibilityMenuItemRole forKey:@"menuitem"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"optionpane"];
    [sRoles setObject:NSAccessibilityRadioButtonRole forKey:@"pagetab"]; // cmcnote: cocoa tabs are radio buttons - one selected button out of a group of options
    [sRoles setObject:NSAccessibilityTabGroupRole forKey:@"pagetablist"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"panel"];
    [sRoles setObject:NSAccessibilityTextFieldRole forKey:@"passwordtext"];
    [sRoles setObject:NSAccessibilityPopUpButtonRole forKey:@"popupmenu"];
    [sRoles setObject:NSAccessibilityProgressIndicatorRole forKey:@"progressbar"];
    [sRoles setObject:NSAccessibilityButtonRole forKey:@"pushbutton"];
    [sRoles setObject:NSAccessibilityRadioButtonRole forKey:@"radiobutton"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"rootpane"];
    [sRoles setObject:NSAccessibilityRowRole forKey:@"rowheader"];
    [sRoles setObject:NSAccessibilityScrollBarRole forKey:@"scrollbar"];
    [sRoles setObject:NSAccessibilityScrollAreaRole forKey:@"scrollpane"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"separator"];
    [sRoles setObject:NSAccessibilitySliderRole forKey:@"slider"];
    [sRoles setObject:NSAccessibilityIncrementorRole forKey:@"spinbox"];
    [sRoles setObject:NSAccessibilitySplitGroupRole forKey:@"splitpane"];
    [sRoles setObject:NSAccessibilityValueIndicatorRole forKey:@"statusbar"];
    [sRoles setObject:NSAccessibilityGroupRole forKey:@"swingcomponent"];
    [sRoles setObject:NSAccessibilityTableRole forKey:@"table"];
    [sRoles setObject:NSAccessibilityTextFieldRole forKey:@"text"];
    [sRoles setObject:NSAccessibilityTextAreaRole forKey:@"textarea"]; // supports top/bottom of document notifications: CAccessability.getAccessibleRole()
    [sRoles setObject:NSAccessibilityCheckBoxRole forKey:@"togglebutton"];
    [sRoles setObject:NSAccessibilityToolbarRole forKey:@"toolbar"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"tooltip"];
    [sRoles setObject:NSAccessibilityBrowserRole forKey:@"tree"];
    [sRoles setObject:NSAccessibilityUnknownRole forKey:@"unknown"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"viewport"];
    [sRoles setObject:JavaAccessibilityIgnore forKey:@"window"];
}

//****************************************************
// Helper methods to deal with global weak refs nicely
//****************************************************

jobject CallStaticObjectMethodWeakly_LL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j1, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jobject CallStaticObjectMethodWeakly_L(JNIEnv *env, JNFMemberInfo *method, jobject weak)
{
    jobject j = (*env)->NewLocalRef(env, weak);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j);
    }
    (*env)->DeleteLocalRef(env, j);
    return res;
}

jobject CallStaticObjectMethodWeakly_LLIZ(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jint i, jboolean b)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j1, j2, i, b);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jobject CallStaticObjectMethodWeakly_LFF(JNIEnv *env, JNFMemberInfo *method, jobject weak, jfloat f1, jfloat f2)
{
    jobject j = (*env)->NewLocalRef(env, weak);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j, f1, f2);
    }
    (*env)->DeleteLocalRef(env, j);
    return res;
}

jobject CallStaticObjectMethodWeakly_LIIL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jint i1, jint i2, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j1, i1, i2, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jobject CallStaticObjectMethodWeakly_LLII(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jint i1, jint i2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j1, j2, i1, i2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jobject CallStaticObjectMethodWeakly_LLI(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jint i)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j1, j2, i);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jobject CallStaticObjectMethodWeakly_LIL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jint i, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject res = NULL;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticObjectMethod(env, method, j1, i, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jboolean CallStaticBooleanMethodWeakly_LIL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jint i, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jboolean res = 0;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticBooleanMethod(env, method, j1, i, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jboolean CallStaticBooleanMethodWeakly_LLL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jobject weak3)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject j3 = (*env)->NewLocalRef(env, weak3);
    jboolean res = 0;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL) && !(*env)->IsSameObject(env, j3, NULL)) {
        res = JNFCallStaticBooleanMethod(env, method, j1, j2, j3);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    (*env)->DeleteLocalRef(env, j3);
    return res;
}

jboolean CallStaticBooleanMethodWeakly_LL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jboolean res = 0;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticBooleanMethod(env, method, j1, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jint CallStaticIntMethodWeakly_LL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jint res = 0;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticIntMethod(env, method, j1, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jint CallStaticIntMethodWeakly_LLI(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jint i)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jint res = 0;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticIntMethod(env, method, j1, j2, i);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jint CallStaticIntMethodWeakly_LLII(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jint i1, jint i2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jint res = 0;
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        res = JNFCallStaticIntMethod(env, method, j1, j2, i1, i2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    return res;
}

jlong CallStaticLongMethodWeakly_L(JNIEnv *env, JNFMemberInfo *method, jobject weak)
{
    jobject j = (*env)->NewLocalRef(env, weak);
    jlong res = 0;
    if (!(*env)->IsSameObject(env, j, NULL)) {
        res = JNFCallStaticLongMethod(env, method, j);
    }
    (*env)->DeleteLocalRef(env, j);
    return res;
}

void CallStaticVoidMethodWeakly_LIL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jint i, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        JNFCallStaticVoidMethod(env, method, j1, i, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
}

void CallStaticVoidMethodWeakly_LL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        JNFCallStaticVoidMethod(env, method, j1, j2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
}

void CallStaticVoidMethodWeakly_LLL(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jobject weak3)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    jobject j3 = (*env)->NewLocalRef(env, weak3);
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL) && !(*env)->IsSameObject(env, j3, NULL)) {
        JNFCallStaticVoidMethod(env, method, j1, j2, j3);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
    (*env)->DeleteLocalRef(env, j3);
}

void CallStaticVoidMethodWeakly_LLII(JNIEnv *env, JNFMemberInfo *method, jobject weak1, jobject weak2, jint i1, jint i2)
{
    jobject j1 = (*env)->NewLocalRef(env, weak1);
    jobject j2 = (*env)->NewLocalRef(env, weak2);
    if (!(*env)->IsSameObject(env, j1, NULL) && !(*env)->IsSameObject(env, j2, NULL)) {
        JNFCallStaticVoidMethod(env, method, j1, j2, i1, i2);
    }
    (*env)->DeleteLocalRef(env, j1);
    (*env)->DeleteLocalRef(env, j2);
}

/*
 * Copyright (c) 2011, 2014, Oracle and/or its affiliates. All rights reserved.
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

package sun.lwawt.macosx;

import sun.awt.SunToolkit;
import sun.lwawt.LWWindowPeer;
import sun.lwawt.PlatformEventNotifier;
import sun.lwawt.PlatformWindow;
import sun.util.logging.PlatformLogger;

import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * Translates NSEvents/NPCocoaEvents into AWT events.
 */
final class CPlatformResponder {

    private static final PlatformLogger keyboardLog = PlatformLogger.getLogger("sun.lwawt.macosx.CPlatformResponder");

    private final PlatformEventNotifier eventNotifier;
    private final boolean isNpapiCallback;
    private int lastKeyPressCode = KeyEvent.VK_UNDEFINED;
    private final DeltaAccumulator deltaAccumulatorX = new DeltaAccumulator();
    private final DeltaAccumulator deltaAccumulatorY = new DeltaAccumulator();
    private boolean momentumStarted;
    private int momentumX;
    private int momentumY;
    private int momentumModifiers;
    private int lastDraggedAbsoluteX;
    private int lastDraggedAbsoluteY;
    private int lastDraggedRelativeX;
    private int lastDraggedRelativeY;

    CPlatformResponder(final PlatformEventNotifier eventNotifier,
                       final boolean isNpapiCallback) {
        this.eventNotifier = eventNotifier;
        this.isNpapiCallback = isNpapiCallback;
    }

    /**
     * Handles mouse events.
     */
    void handleMouseEvent(PlatformWindow platformWindow, int eventType, int modifierFlags, int buttonNumber,
                          int clickCount, int x, int y, int absoluteX,
                          int absoluteY) {
        final SunToolkit tk = (SunToolkit)Toolkit.getDefaultToolkit();
        if ((buttonNumber > 2 && !tk.areExtraMouseButtonsEnabled())
                || buttonNumber > tk.getNumberOfButtons() - 1) {
            return;
        }

        int jeventType = isNpapiCallback ? NSEvent.npToJavaEventType(eventType) :
                                           NSEvent.nsToJavaEventType(eventType);

        boolean dragged = jeventType == MouseEvent.MOUSE_DRAGGED;
        if (dragged  // ignore dragged event that does not change any location
                && lastDraggedAbsoluteX == absoluteX && lastDraggedRelativeX == x
                && lastDraggedAbsoluteY == absoluteY && lastDraggedRelativeY == y) return;

        if (dragged || jeventType == MouseEvent.MOUSE_PRESSED) {
            lastDraggedAbsoluteX = absoluteX;
            lastDraggedAbsoluteY = absoluteY;
            lastDraggedRelativeX = x;
            lastDraggedRelativeY = y;
        }

        int jbuttonNumber = MouseEvent.NOBUTTON;
        int jclickCount = 0;

        if (jeventType != MouseEvent.MOUSE_MOVED &&
            jeventType != MouseEvent.MOUSE_ENTERED &&
            jeventType != MouseEvent.MOUSE_EXITED)
        {
            jbuttonNumber = NSEvent.nsToJavaButton(buttonNumber);
            jclickCount = clickCount;
        }

        int jmodifiers = NSEvent.nsToJavaMouseModifiers(buttonNumber,
                                                        modifierFlags);
        boolean jpopupTrigger = NSEvent.isPopupTrigger(jmodifiers);

        eventNotifier.notifyMouseEvent(platformWindow, jeventType, System.currentTimeMillis(), jbuttonNumber,
                x, y, absoluteX, absoluteY, jmodifiers, jclickCount,
                jpopupTrigger, null);
    }

    /**
     * Handles scroll events.
     */
    void handleScrollEvent(int x, int y, final int modifierFlags,
                           final double deltaX, final double deltaY,
                           final int scrollPhase) {
        final int buttonNumber = CocoaConstants.kCGMouseButtonCenter;
        int jmodifiers = NSEvent.nsToJavaMouseModifiers(buttonNumber,
                                                        modifierFlags);
        if (scrollPhase > NSEvent.SCROLL_PHASE_UNSUPPORTED) {
            if (scrollPhase == NSEvent.SCROLL_PHASE_BEGAN) {
                momentumStarted = false;
            } else if (scrollPhase == NSEvent.SCROLL_PHASE_MOMENTUM_BEGAN) {
                momentumStarted = true;
                momentumX = x;
                momentumY = y;
                momentumModifiers = jmodifiers;
            } else if (momentumStarted) {
                x = momentumX;
                y = momentumY;
                jmodifiers = momentumModifiers;
            }
        }
        final boolean isShift = (jmodifiers & InputEvent.SHIFT_DOWN_MASK) != 0;

        int roundDeltaX = deltaAccumulatorX.getRoundedDelta(deltaX, scrollPhase);
        int roundDeltaY = deltaAccumulatorY.getRoundedDelta(deltaY, scrollPhase);

        // Vertical scroll.
        if (!isShift && (deltaY != 0.0 || roundDeltaY != 0)) {
            dispatchScrollEvent(x, y, jmodifiers, roundDeltaY, deltaY);
        }
        // Horizontal scroll or shirt+vertical scroll.
        final double delta = isShift && deltaY != 0.0 ? deltaY : deltaX;
        final int roundDelta = isShift && roundDeltaY != 0 ? roundDeltaY : roundDeltaX;
        if (delta != 0.0 || roundDelta != 0) {
            jmodifiers |= InputEvent.SHIFT_DOWN_MASK;
            dispatchScrollEvent(x, y, jmodifiers, roundDelta, delta);
        }
    }

    private void dispatchScrollEvent(final int x, final int y,
                                     final int modifiers,
                                     final int roundDelta, final double delta) {
        final long when = System.currentTimeMillis();
        final int scrollType = MouseWheelEvent.WHEEL_UNIT_SCROLL;
        final int scrollAmount = 1;
        // invert the wheelRotation for the peer
        eventNotifier.notifyMouseWheelEvent(when, x, y, modifiers, scrollType,
                scrollAmount, -roundDelta, -delta, null);
    }

    private void handleFlagChangedEvent(int modifierFlags, short keyCode) {
        int[] in = new int[] {modifierFlags, keyCode};
        int[] out = new int[3]; // [jkeyCode, jkeyLocation, jkeyType]

        NSEvent.nsKeyModifiersToJavaKeyInfo(in, out);

        int jkeyCode = out[0];
        int jkeyLocation = out[1];
        int jeventType = out[2];

        int jmodifiers = NSEvent.nsToJavaKeyModifiers(modifierFlags);
        long when = System.currentTimeMillis();

        if (jeventType == KeyEvent.KEY_PRESSED) {
            lastKeyPressCode = jkeyCode;
        }
        eventNotifier.notifyKeyEvent(jeventType, when, jmodifiers,
                jkeyCode, KeyEvent.CHAR_UNDEFINED, jkeyLocation);
    }

    private static char mapNsCharsToCompatibleWithJava (char ch) {
        switch (ch) {
            case 0x0003:          // NSEnterCharacter
            case 0x000d:          // NSCarriageReturnCharacter
                return 0x000a;    // NSNewlineCharacter
//            case  0x007f:         // NSDeleteCharacter
//                return 0x0008;    // NSBackspaceCharacter
            case  0xF728:         // NSDeleteFunctionKey
                return 0x0008;    // NSDeleteCharacter
            case  0x0019:         // NSBackTabCharacter
                return 0x0009;    // NSTabCharacter
        }
        return ch;
    }

    /**
     * Handles key events.
     */
    void handleKeyEvent(NSEvent nsEvent)
    {
        boolean isFlagsChangedEvent =
                isNpapiCallback ? (nsEvent.getType() == CocoaConstants.NPCocoaEventFlagsChanged) :
                        (nsEvent.getType() == CocoaConstants.NSFlagsChanged);

        int jeventType = KeyEvent.KEY_PRESSED;
        int jkeyCode = KeyEvent.VK_UNDEFINED;
        int jkeyLocation = KeyEvent.KEY_LOCATION_UNKNOWN;
        boolean postsTyped = false;


        if (isFlagsChangedEvent) {
            handleFlagChangedEvent(nsEvent.getModifierFlags(), nsEvent.getKeyCode());
            return;
        }

        int jmodifiers = NSEvent.nsToJavaKeyModifiers(nsEvent.getModifierFlags());

        boolean metaAltCtrlAreNotPressed = (jmodifiers &
                (InputEvent.META_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK
                | InputEvent.CTRL_DOWN_MASK)
        ) == 0;

        boolean shiftIsPressed = (jmodifiers & InputEvent.SHIFT_DOWN_MASK) != 0;

        boolean useShiftedCharacters = metaAltCtrlAreNotPressed && shiftIsPressed;

        boolean shiftAltDownAreNotPressed = (jmodifiers &
                (InputEvent.META_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK
                | InputEvent.SHIFT_DOWN_MASK)
        ) == 0;

        boolean ctrlIsPressed = (jmodifiers & InputEvent.CTRL_DOWN_MASK) != 0;

        boolean isISOControl = false;

        char checkedChar = (nsEvent.getCharacters() == null
                || nsEvent.getCharacters().isEmpty()) ? KeyEvent.CHAR_UNDEFINED : nsEvent.getCharacters().charAt(0);

        if (shiftAltDownAreNotPressed && ctrlIsPressed) {
            if (Character.isISOControl(checkedChar)) {
                isISOControl = true;
            }
        }

        char characterToGetKeyCode = KeyEvent.CHAR_UNDEFINED;

        boolean charactersIgnoringModifiersIsValid  = (nsEvent.getCharactersIgnoringModifiers() != null && nsEvent.getCharactersIgnoringModifiers().length() > 0);
        boolean charactersIsValid  = (nsEvent.getCharacters() != null && nsEvent.getCharacters().length() > 0);

        // We use this char to find a character that is printed depending on pressing modifiers
        characterToGetKeyCode = charactersIgnoringModifiersIsValid
            ? nsEvent.getCharactersIgnoringModifiers().charAt(0)
            : charactersIsValid ? nsEvent.getCharacters().charAt(0) : KeyEvent.CHAR_UNDEFINED;

        // We use char candidate if modifiers are not used
        // otherwise, we use char ignoring modifiers
        int[] in = new int[] {
                characterToGetKeyCode,
                nsEvent.getModifierFlags(),
                nsEvent.isHasDeadKey() ? 1 : 0,
                nsEvent.getKeyCode()
        };

        int[] out = new int[3]; // [jkeyCode, jkeyLocation, deadChar]

        postsTyped = NSEvent.nsToJavaKeyInfo(in, out);

        char characterToSendWithTheEvent = KeyEvent.CHAR_UNDEFINED;

        if(nsEvent.isHasDeadKey()){
            characterToSendWithTheEvent = (char) out[2];
            jkeyCode = nsEvent.getDeadKeyCode();
            if(characterToSendWithTheEvent == 0){
                return;
            }
        }

        // If Pinyin Simplified input method is selected, CAPS_LOCK key is supposed to switch
        // input to la tin letters.
        // It is necessary to use charIgnoringModifiers instead of charCandidate for event
        // generation in such case to avoid uppercase letters in text components.
        LWCToolkit lwcToolkit = (LWCToolkit)Toolkit.getDefaultToolkit();
        if (lwcToolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK) &&
                Locale.SIMPLIFIED_CHINESE.equals(lwcToolkit.getDefaultKeyboardLocale())) {
            characterToSendWithTheEvent = characterToGetKeyCode;
        }

        jkeyCode = out[0];

        jkeyLocation = out[1];
        jeventType = isNpapiCallback ? NSEvent.npToJavaEventType(nsEvent.getType()) :
                NSEvent.nsToJavaEventType(nsEvent.getType());


        if (isISOControl) {
            characterToSendWithTheEvent = checkedChar;
        } else  if (useShiftedCharacters && nsEvent.getCharactersIgnoringModifiers() != null && !nsEvent.getCharactersIgnoringModifiers().isEmpty()) {
            characterToSendWithTheEvent = nsEvent.getCharactersIgnoringModifiers().charAt(0);
        } else  if (nsEvent.getCharactersIgnoringModifiersAndShift() != null && !nsEvent.getCharactersIgnoringModifiersAndShift().isEmpty()) {
            characterToSendWithTheEvent = nsEvent.getCharactersIgnoringModifiersAndShift().charAt(0);
        } else if (nsEvent.getCharacters() != null && !nsEvent.getCharacters().isEmpty() && metaAltCtrlAreNotPressed && shiftIsPressed) {
            characterToSendWithTheEvent = checkedChar;
        }

        characterToSendWithTheEvent = mapNsCharsToCompatibleWithJava(characterToSendWithTheEvent);

        if (jmodifiers != 0 && nsEvent.getCharactersIgnoringModifiersAndShift() != null && !nsEvent.getCharactersIgnoringModifiersAndShift().isEmpty()) {
            String stringWithChar = NSEvent.nsToJavaChar(characterToSendWithTheEvent, nsEvent.getModifierFlags());
            characterToSendWithTheEvent = stringWithChar == null ? KeyEvent.CHAR_UNDEFINED : stringWithChar.charAt(0);
        } else {
            String stringWithChar = NSEvent.nsToJavaChar(characterToSendWithTheEvent, nsEvent.getModifierFlags());
            characterToSendWithTheEvent = stringWithChar == null ? KeyEvent.CHAR_UNDEFINED : stringWithChar.charAt(0);
        }


        long when = System.currentTimeMillis();

        if (jeventType == KeyEvent.KEY_PRESSED) {
            lastKeyPressCode = jkeyCode;
        }

        eventNotifier.notifyKeyEvent(jeventType, when, jmodifiers,
                jkeyCode, characterToSendWithTheEvent, jkeyLocation);

        // Current browser may be sending input events, so don't
        // post the KEY_TYPED here.
        postsTyped &= true;

        // That's the reaction on the PRESSED (not RELEASED) event as it comes to
        // appear in MacOSX.
        // Modifier keys (shift, etc) don't want to send TYPED events.
        // On the other hand we don't want to generate keyTyped events
        // for clipboard related shortcuts like Meta + [CVX]
        if (jeventType == KeyEvent.KEY_PRESSED && postsTyped &&
                (jmodifiers & KeyEvent.META_DOWN_MASK) == 0) {

            char characterToSendWithTypedEvent = KeyEvent.CHAR_UNDEFINED;

            if (nsEvent.getCharacters()!= null ) {
                characterToSendWithTypedEvent = mapNsCharsToCompatibleWithJava(checkedChar);
                String stringWithChar = NSEvent.nsToJavaChar(characterToSendWithTypedEvent, nsEvent.getModifierFlags());
                characterToSendWithTypedEvent = stringWithChar == null ? KeyEvent.CHAR_UNDEFINED :  stringWithChar.charAt(0);
            }

            boolean nonInputMethodsModifiersAreNotPressed = (jmodifiers &
                    (InputEvent.META_DOWN_MASK | InputEvent.CTRL_DOWN_MASK)
            ) == 0;

            if (nonInputMethodsModifiersAreNotPressed) {
                eventNotifier.notifyKeyEvent(KeyEvent.KEY_TYPED, when, jmodifiers,
                        jkeyCode, characterToSendWithTypedEvent,
                        KeyEvent.KEY_LOCATION_UNKNOWN);
            }
        }
    }

    void handleInputEvent(String text) {
        if (text != null) {
            int index = 0, length = text.length();
            char c = 0;
            while (index < length) {
                c = text.charAt(index);
                eventNotifier.notifyKeyEvent(KeyEvent.KEY_TYPED,
                        System.currentTimeMillis(),
                        0, KeyEvent.VK_UNDEFINED, c,
                        KeyEvent.KEY_LOCATION_UNKNOWN);
                index++;
            }
            eventNotifier.notifyKeyEvent(KeyEvent.KEY_RELEASED,
                    System.currentTimeMillis(),
                    0, lastKeyPressCode, c,
                    KeyEvent.KEY_LOCATION_UNKNOWN);
        }
    }

    void handleWindowFocusEvent(boolean gained, LWWindowPeer opposite) {
        eventNotifier.notifyActivation(gained, opposite);
    }

    static class DeltaAccumulator {

        double accumulatedDelta;
        boolean accumulate;

        int getRoundedDelta(double delta, int scrollPhase) {

            int roundDelta = (int) Math.round(delta);

            if (scrollPhase == NSEvent.SCROLL_PHASE_UNSUPPORTED) { // mouse wheel
                if (roundDelta == 0 && delta != 0) {
                    roundDelta = delta > 0 ? 1 : -1;
                }
            } else { // trackpad
                if (scrollPhase == NSEvent.SCROLL_PHASE_BEGAN) {
                    accumulatedDelta = 0;
                    accumulate = true;
                }
                else if (scrollPhase == NSEvent.SCROLL_PHASE_MOMENTUM_BEGAN) {
                    accumulate = true;
                }
                if (accumulate) {

                    accumulatedDelta += delta;

                    roundDelta = (int) Math.round(accumulatedDelta);

                    accumulatedDelta -= roundDelta;

                    if (scrollPhase == NSEvent.SCROLL_PHASE_ENDED) {
                        accumulate = false;
                    }
                }
            }

            return roundDelta;
        }
    }
}

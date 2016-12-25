package sun.awt;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.event.KeyEvent;

public class ExtendedKeyCodes {
    /**
     * ATTN: These are the readonly hashes with load factor == 1;
     * adding a value, please set the inital capacity to exact number of items
     * or higher.
     */
     // Keycodes declared in KeyEvent.java with corresponding Unicode values.
     private final static HashMap<Integer, Integer>  regularKeyCodesMap =
                                          new HashMap<Integer,Integer>(98, 1.0f);

     // Keycodes derived from Unicode values. Here should be collected codes
     // for characters appearing on the primary layer of at least one
     // known keyboard layout. For instance, sterling sign is on the primary layer
     // of the Mac Italian layout.
     private final static HashSet<Integer> extendedKeyCodesSet =
                                                  new HashSet<Integer>(501, 1.0f);
     final public static int getExtendedKeyCodeForChar( int c ) {
         int uc = Character.toUpperCase( c );
         int lc = Character.toLowerCase( c );
         if (regularKeyCodesMap.containsKey( c )) {
             if(regularKeyCodesMap.containsKey(uc)) {
                 return regularKeyCodesMap.get( uc );
             }
             return regularKeyCodesMap.get( c );
         }
         uc += 0x01000000;
         lc += 0x01000000;
         if (extendedKeyCodesSet.contains( uc )) {
             return uc;
         }else if (extendedKeyCodesSet.contains( lc )) {
             return lc;
         }
         return KeyEvent.VK_UNDEFINED;
     }
     static {
         regularKeyCodesMap.put(0x08, KeyEvent.VK_BACK_SPACE);
         regularKeyCodesMap.put(0x09, KeyEvent.VK_TAB);
         regularKeyCodesMap.put(0x0a, KeyEvent.VK_ENTER);
         regularKeyCodesMap.put(0x1B, KeyEvent.VK_ESCAPE);
         regularKeyCodesMap.put(0x20AC, KeyEvent.VK_EURO_SIGN);
         regularKeyCodesMap.put(0x20, KeyEvent.VK_SPACE);
         regularKeyCodesMap.put(0x21, KeyEvent.VK_EXCLAMATION_MARK);
         regularKeyCodesMap.put(0x22, KeyEvent.VK_QUOTEDBL);
         regularKeyCodesMap.put(0x23, KeyEvent.VK_NUMBER_SIGN);
         regularKeyCodesMap.put(0x24, KeyEvent.VK_DOLLAR);
         regularKeyCodesMap.put(0x26, KeyEvent.VK_AMPERSAND);
         regularKeyCodesMap.put(0x27, KeyEvent.VK_QUOTE);
         regularKeyCodesMap.put(0x28, KeyEvent.VK_LEFT_PARENTHESIS);
         regularKeyCodesMap.put(0x29, KeyEvent.VK_RIGHT_PARENTHESIS);
         regularKeyCodesMap.put(0x2A, KeyEvent.VK_ASTERISK);
         regularKeyCodesMap.put(0x2B, KeyEvent.VK_PLUS);
         regularKeyCodesMap.put(0x2C, KeyEvent.VK_COMMA);
         regularKeyCodesMap.put(0x2D, KeyEvent.VK_MINUS);
         regularKeyCodesMap.put(0x2E, KeyEvent.VK_PERIOD);
         regularKeyCodesMap.put(0x2F, KeyEvent.VK_SLASH);
         regularKeyCodesMap.put(0x30, KeyEvent.VK_0);
         regularKeyCodesMap.put(0x31, KeyEvent.VK_1);
         regularKeyCodesMap.put(0x32, KeyEvent.VK_2);
         regularKeyCodesMap.put(0x33, KeyEvent.VK_3);
         regularKeyCodesMap.put(0x34, KeyEvent.VK_4);
         regularKeyCodesMap.put(0x35, KeyEvent.VK_5);
         regularKeyCodesMap.put(0x36, KeyEvent.VK_6);
         regularKeyCodesMap.put(0x37, KeyEvent.VK_7);
         regularKeyCodesMap.put(0x38, KeyEvent.VK_8);
         regularKeyCodesMap.put(0x39, KeyEvent.VK_9);
         regularKeyCodesMap.put(0x3A, KeyEvent.VK_COLON);
         regularKeyCodesMap.put(0x3B, KeyEvent.VK_SEMICOLON);
         regularKeyCodesMap.put(0x3C, KeyEvent.VK_LESS);
         regularKeyCodesMap.put(0x3D, KeyEvent.VK_EQUALS);
         regularKeyCodesMap.put(0x3E, KeyEvent.VK_GREATER);
         regularKeyCodesMap.put(0x40, KeyEvent.VK_AT);
         regularKeyCodesMap.put(0x41, KeyEvent.VK_A);
         regularKeyCodesMap.put(0x42, KeyEvent.VK_B);
         regularKeyCodesMap.put(0x43, KeyEvent.VK_C);
         regularKeyCodesMap.put(0x44, KeyEvent.VK_D);
         regularKeyCodesMap.put(0x45, KeyEvent.VK_E);
         regularKeyCodesMap.put(0x46, KeyEvent.VK_F);
         regularKeyCodesMap.put(0x47, KeyEvent.VK_G);
         regularKeyCodesMap.put(0x48, KeyEvent.VK_H);
         regularKeyCodesMap.put(0x49, KeyEvent.VK_I);
         regularKeyCodesMap.put(0x4A, KeyEvent.VK_J);
         regularKeyCodesMap.put(0x4B, KeyEvent.VK_K);
         regularKeyCodesMap.put(0x4C, KeyEvent.VK_L);
         regularKeyCodesMap.put(0x4D, KeyEvent.VK_M);
         regularKeyCodesMap.put(0x4E, KeyEvent.VK_N);
         regularKeyCodesMap.put(0x4F, KeyEvent.VK_O);
         regularKeyCodesMap.put(0x50, KeyEvent.VK_P);
         regularKeyCodesMap.put(0x51, KeyEvent.VK_Q);
         regularKeyCodesMap.put(0x52, KeyEvent.VK_R);
         regularKeyCodesMap.put(0x53, KeyEvent.VK_S);
         regularKeyCodesMap.put(0x54, KeyEvent.VK_T);
         regularKeyCodesMap.put(0x55, KeyEvent.VK_U);
         regularKeyCodesMap.put(0x56, KeyEvent.VK_V);
         regularKeyCodesMap.put(0x57, KeyEvent.VK_W);
         regularKeyCodesMap.put(0x58, KeyEvent.VK_X);
         regularKeyCodesMap.put(0x59, KeyEvent.VK_Y);
         regularKeyCodesMap.put(0x5A, KeyEvent.VK_Z);
         regularKeyCodesMap.put(0x5B, KeyEvent.VK_OPEN_BRACKET);
         regularKeyCodesMap.put(0x5C, KeyEvent.VK_BACK_SLASH);
         regularKeyCodesMap.put(0x5D, KeyEvent.VK_CLOSE_BRACKET);
         regularKeyCodesMap.put(0x5E, KeyEvent.VK_CIRCUMFLEX);
         regularKeyCodesMap.put(0x5F, KeyEvent.VK_UNDERSCORE);
         regularKeyCodesMap.put(0x60, KeyEvent.VK_BACK_QUOTE);
         regularKeyCodesMap.put(0x61, KeyEvent.VK_A);
         regularKeyCodesMap.put(0x62, KeyEvent.VK_B);
         regularKeyCodesMap.put(0x63, KeyEvent.VK_C);
         regularKeyCodesMap.put(0x64, KeyEvent.VK_D);
         regularKeyCodesMap.put(0x65, KeyEvent.VK_E);
         regularKeyCodesMap.put(0x66, KeyEvent.VK_F);
         regularKeyCodesMap.put(0x67, KeyEvent.VK_G);
         regularKeyCodesMap.put(0x68, KeyEvent.VK_H);
         regularKeyCodesMap.put(0x69, KeyEvent.VK_I);
         regularKeyCodesMap.put(0x6A, KeyEvent.VK_J);
         regularKeyCodesMap.put(0x6B, KeyEvent.VK_K);
         regularKeyCodesMap.put(0x6C, KeyEvent.VK_L);
         regularKeyCodesMap.put(0x6D, KeyEvent.VK_M);
         regularKeyCodesMap.put(0x6E, KeyEvent.VK_N);
         regularKeyCodesMap.put(0x6F, KeyEvent.VK_O);
         regularKeyCodesMap.put(0x70, KeyEvent.VK_P);
         regularKeyCodesMap.put(0x71, KeyEvent.VK_Q);
         regularKeyCodesMap.put(0x72, KeyEvent.VK_R);
         regularKeyCodesMap.put(0x73, KeyEvent.VK_S);
         regularKeyCodesMap.put(0x74, KeyEvent.VK_T);
         regularKeyCodesMap.put(0x75, KeyEvent.VK_U);
         regularKeyCodesMap.put(0x76, KeyEvent.VK_V);
         regularKeyCodesMap.put(0x77, KeyEvent.VK_W);
         regularKeyCodesMap.put(0x78, KeyEvent.VK_X);
         regularKeyCodesMap.put(0x79, KeyEvent.VK_Y);
         regularKeyCodesMap.put(0x7A, KeyEvent.VK_Z);
         regularKeyCodesMap.put(0x7B, KeyEvent.VK_BRACELEFT);
         regularKeyCodesMap.put(0x7D, KeyEvent.VK_BRACERIGHT);
         regularKeyCodesMap.put(0x7F, KeyEvent.VK_DELETE);
         regularKeyCodesMap.put(0xA1, KeyEvent.VK_INVERTED_EXCLAMATION_MARK);


         regularKeyCodesMap.put(0xF701, KeyEvent.VK_DOWN);    // NSDownArrowFunctionKey
         regularKeyCodesMap.put(0xF702, KeyEvent.VK_LEFT);    // NSLeftArrowFunctionKey
         regularKeyCodesMap.put(0xF703, KeyEvent.VK_RIGHT);   // NSRightArrowFunctionKey
         regularKeyCodesMap.put(0xF704, KeyEvent.VK_F1);      // NSF1FunctionKey
         regularKeyCodesMap.put(0xF705, KeyEvent.VK_F2);      // NSF2FunctionKey
         regularKeyCodesMap.put(0xF706, KeyEvent.VK_F3);      // NSF3FunctionKey
         regularKeyCodesMap.put(0xF707, KeyEvent.VK_F4);      // NSF4FunctionKey
         regularKeyCodesMap.put(0xF708, KeyEvent.VK_F5);      // NSF5FunctionKey
         regularKeyCodesMap.put(0xF709, KeyEvent.VK_F6);      // NSF6FunctionKey
         regularKeyCodesMap.put(0xF70A, KeyEvent.VK_F7);      // NSF7FunctionKey
         regularKeyCodesMap.put(0xF70B, KeyEvent.VK_F8);      // NSF8FunctionKey
         regularKeyCodesMap.put(0xF70C, KeyEvent.VK_F9);      // NSF9FunctionKey
         regularKeyCodesMap.put(0xF70D, KeyEvent.VK_F10);     // NSF10FunctionKey
         regularKeyCodesMap.put(0xF70E, KeyEvent.VK_F11);     // NSF11FunctionKey
         regularKeyCodesMap.put(0xF70F, KeyEvent.VK_F12);     // NSF12FunctionKey
         regularKeyCodesMap.put(0xF710, KeyEvent.VK_F13);     // NSF13FunctionKey
         regularKeyCodesMap.put(0xF711, KeyEvent.VK_F14);     // NSF14FunctionKey
         regularKeyCodesMap.put(0xF712, KeyEvent.VK_F15);     // NSF15FunctionKey
         regularKeyCodesMap.put(0xF713, KeyEvent.VK_F16);     // NSF16FunctionKey
         regularKeyCodesMap.put(0xF714, KeyEvent.VK_F17);     // NSF17FunctionKey
         regularKeyCodesMap.put(0xF715, KeyEvent.VK_F18);     // NSF18FunctionKey
         regularKeyCodesMap.put(0xF716, KeyEvent.VK_F19);     // NSF19FunctionKey
         regularKeyCodesMap.put(0xF717, KeyEvent.VK_F20);     // NSF20FunctionKey
         regularKeyCodesMap.put(0xF718, KeyEvent.VK_F21);     // NSF21FunctionKey
         regularKeyCodesMap.put(0xF719, KeyEvent.VK_F22);     // NSF22FunctionKey
         regularKeyCodesMap.put(0xF71A, KeyEvent.VK_F23);     // NSF23FunctionKey
         regularKeyCodesMap.put(0xF71B, KeyEvent.VK_F24);     // NSF24FunctionKey
         regularKeyCodesMap.put(0xF727, KeyEvent.VK_INSERT);    // NSInsertFunctionKey
         regularKeyCodesMap.put(0xF728, KeyEvent.VK_DELETE);    // NSDeleteFunctionKey
         regularKeyCodesMap.put(0xF729, KeyEvent.VK_HOME);    // NSHomeFunctionKey
         regularKeyCodesMap.put(0xF72A, KeyEvent.VK_BEGIN);    // NSBeginFunctionKey
         regularKeyCodesMap.put(0xF72B, KeyEvent.VK_END);    // NSEndFunctionKey
         regularKeyCodesMap.put(0xF72C, KeyEvent.VK_PAGE_UP);    // NSPageUpFunctionKey
         regularKeyCodesMap.put(0xF72D, KeyEvent.VK_PAGE_DOWN);    // NSPageDownFunctionKey
         regularKeyCodesMap.put(0xF72E, KeyEvent.VK_PRINTSCREEN);    // NSPrintScreenFunctionKey
         regularKeyCodesMap.put(0xF72F, KeyEvent.VK_SCROLL_LOCK);    // NSScrollLockFunctionKey
         regularKeyCodesMap.put(0xF730, KeyEvent.VK_PAUSE);    // NSPauseFunctionKey
         regularKeyCodesMap.put(0xF734, KeyEvent.VK_STOP);    // NSStopFunctionKey
         regularKeyCodesMap.put(0xF735, KeyEvent.VK_CONTEXT_MENU);    // NSMenuFunctionKey
         regularKeyCodesMap.put(0xF738, KeyEvent.VK_PRINTSCREEN);    // NSPrintFunctionKey
         regularKeyCodesMap.put(0xF739, KeyEvent.VK_CLEAR);    // NSClearLineFunctionKey
         regularKeyCodesMap.put(0xF746, KeyEvent.VK_HELP);    // NSHelpFunctionKey

         extendedKeyCodesSet.add(0x01000000+0x0060);
         extendedKeyCodesSet.add(0x01000000+0x007C);
         extendedKeyCodesSet.add(0x01000000+0x007E);
         extendedKeyCodesSet.add(0x01000000+0x00A2);
         extendedKeyCodesSet.add(0x01000000+0x00A3);
         extendedKeyCodesSet.add(0x01000000+0x00A5);
         extendedKeyCodesSet.add(0x01000000+0x00A7);
         extendedKeyCodesSet.add(0x01000000+0x00A8);
         extendedKeyCodesSet.add(0x01000000+0x00AB);
         extendedKeyCodesSet.add(0x01000000+0x00B0);
         extendedKeyCodesSet.add(0x01000000+0x00B1);
         extendedKeyCodesSet.add(0x01000000+0x00B2);
         extendedKeyCodesSet.add(0x01000000+0x00B3);
         extendedKeyCodesSet.add(0x01000000+0x00B4);
         extendedKeyCodesSet.add(0x01000000+0x00B5);
         extendedKeyCodesSet.add(0x01000000+0x00B6);
         extendedKeyCodesSet.add(0x01000000+0x00B7);
         extendedKeyCodesSet.add(0x01000000+0x00B9);
         extendedKeyCodesSet.add(0x01000000+0x00BA);
         extendedKeyCodesSet.add(0x01000000+0x00BB);
         extendedKeyCodesSet.add(0x01000000+0x00BC);
         extendedKeyCodesSet.add(0x01000000+0x00BD);
         extendedKeyCodesSet.add(0x01000000+0x00BE);
         extendedKeyCodesSet.add(0x01000000+0x00BF);
         extendedKeyCodesSet.add(0x01000000+0x00C4);
         extendedKeyCodesSet.add(0x01000000+0x00C5);
         extendedKeyCodesSet.add(0x01000000+0x00C6);
         extendedKeyCodesSet.add(0x01000000+0x00C7);
         extendedKeyCodesSet.add(0x01000000+0x00D1);
         extendedKeyCodesSet.add(0x01000000+0x00D6);
         extendedKeyCodesSet.add(0x01000000+0x00D7);
         extendedKeyCodesSet.add(0x01000000+0x00D8);
         extendedKeyCodesSet.add(0x01000000+0x00DF);
         extendedKeyCodesSet.add(0x01000000+0x00E0);
         extendedKeyCodesSet.add(0x01000000+0x00E1);
         extendedKeyCodesSet.add(0x01000000+0x00E2);
         extendedKeyCodesSet.add(0x01000000+0x00E4);
         extendedKeyCodesSet.add(0x01000000+0x00E5);
         extendedKeyCodesSet.add(0x01000000+0x00E6);
         extendedKeyCodesSet.add(0x01000000+0x00E7);
         extendedKeyCodesSet.add(0x01000000+0x00E8);
         extendedKeyCodesSet.add(0x01000000+0x00E9);
         extendedKeyCodesSet.add(0x01000000+0x00EA);
         extendedKeyCodesSet.add(0x01000000+0x00EB);
         extendedKeyCodesSet.add(0x01000000+0x00EC);
         extendedKeyCodesSet.add(0x01000000+0x00ED);
         extendedKeyCodesSet.add(0x01000000+0x00EE);
         extendedKeyCodesSet.add(0x01000000+0x00F0);
         extendedKeyCodesSet.add(0x01000000+0x00F1);
         extendedKeyCodesSet.add(0x01000000+0x00F2);
         extendedKeyCodesSet.add(0x01000000+0x00F3);
         extendedKeyCodesSet.add(0x01000000+0x00F4);
         extendedKeyCodesSet.add(0x01000000+0x00F5);
         extendedKeyCodesSet.add(0x01000000+0x00F6);
         extendedKeyCodesSet.add(0x01000000+0x00F7);
         extendedKeyCodesSet.add(0x01000000+0x00F8);
         extendedKeyCodesSet.add(0x01000000+0x00F9);
         extendedKeyCodesSet.add(0x01000000+0x00FA);
         extendedKeyCodesSet.add(0x01000000+0x00FB);
         extendedKeyCodesSet.add(0x01000000+0x00FC);
         extendedKeyCodesSet.add(0x01000000+0x00FD);
         extendedKeyCodesSet.add(0x01000000+0x00FE);
         extendedKeyCodesSet.add(0x01000000+0x0105);
         extendedKeyCodesSet.add(0x01000000+0x02DB);
         extendedKeyCodesSet.add(0x01000000+0x0142);
         extendedKeyCodesSet.add(0x01000000+0x013E);
         extendedKeyCodesSet.add(0x01000000+0x015B);
         extendedKeyCodesSet.add(0x01000000+0x0161);
         extendedKeyCodesSet.add(0x01000000+0x015F);
         extendedKeyCodesSet.add(0x01000000+0x0165);
         extendedKeyCodesSet.add(0x01000000+0x017E);
         extendedKeyCodesSet.add(0x01000000+0x017C);
         extendedKeyCodesSet.add(0x01000000+0x0103);
         extendedKeyCodesSet.add(0x01000000+0x0107);
         extendedKeyCodesSet.add(0x01000000+0x010D);
         extendedKeyCodesSet.add(0x01000000+0x0119);
         extendedKeyCodesSet.add(0x01000000+0x011B);
         extendedKeyCodesSet.add(0x01000000+0x0111);
         extendedKeyCodesSet.add(0x01000000+0x0148);
         extendedKeyCodesSet.add(0x01000000+0x0151);
         extendedKeyCodesSet.add(0x01000000+0x0171);
         extendedKeyCodesSet.add(0x01000000+0x0159);
         extendedKeyCodesSet.add(0x01000000+0x016F);
         extendedKeyCodesSet.add(0x01000000+0x0163);
         extendedKeyCodesSet.add(0x01000000+0x02D9);
         extendedKeyCodesSet.add(0x01000000+0x0130);
         extendedKeyCodesSet.add(0x01000000+0x0127);
         extendedKeyCodesSet.add(0x01000000+0x0125);
         extendedKeyCodesSet.add(0x01000000+0x0131);
         extendedKeyCodesSet.add(0x01000000+0x011F);
         extendedKeyCodesSet.add(0x01000000+0x0135);
         extendedKeyCodesSet.add(0x01000000+0x010B);
         extendedKeyCodesSet.add(0x01000000+0x0109);
         extendedKeyCodesSet.add(0x01000000+0x0121);
         extendedKeyCodesSet.add(0x01000000+0x011D);
         extendedKeyCodesSet.add(0x01000000+0x016D);
         extendedKeyCodesSet.add(0x01000000+0x015D);
         extendedKeyCodesSet.add(0x01000000+0x0138);
         extendedKeyCodesSet.add(0x01000000+0x0157);
         extendedKeyCodesSet.add(0x01000000+0x013C);
         extendedKeyCodesSet.add(0x01000000+0x0113);
         extendedKeyCodesSet.add(0x01000000+0x0123);
         extendedKeyCodesSet.add(0x01000000+0x0167);
         extendedKeyCodesSet.add(0x01000000+0x014B);
         extendedKeyCodesSet.add(0x01000000+0x0101);
         extendedKeyCodesSet.add(0x01000000+0x012F);
         extendedKeyCodesSet.add(0x01000000+0x0117);
         extendedKeyCodesSet.add(0x01000000+0x012B);
         extendedKeyCodesSet.add(0x01000000+0x0146);
         extendedKeyCodesSet.add(0x01000000+0x014D);
         extendedKeyCodesSet.add(0x01000000+0x0137);
         extendedKeyCodesSet.add(0x01000000+0x0173);
         extendedKeyCodesSet.add(0x01000000+0x016B);
         extendedKeyCodesSet.add(0x01000000+0x0153);
         extendedKeyCodesSet.add(0x01000000+0x30FC);
         extendedKeyCodesSet.add(0x01000000+0x30A2);
         extendedKeyCodesSet.add(0x01000000+0x30A4);
         extendedKeyCodesSet.add(0x01000000+0x30A6);
         extendedKeyCodesSet.add(0x01000000+0x30A8);
         extendedKeyCodesSet.add(0x01000000+0x30AA);
         extendedKeyCodesSet.add(0x01000000+0x30AB);
         extendedKeyCodesSet.add(0x01000000+0x30AD);
         extendedKeyCodesSet.add(0x01000000+0x30AF);
         extendedKeyCodesSet.add(0x01000000+0x30B1);
         extendedKeyCodesSet.add(0x01000000+0x30B3);
         extendedKeyCodesSet.add(0x01000000+0x30B5);
         extendedKeyCodesSet.add(0x01000000+0x30B7);
         extendedKeyCodesSet.add(0x01000000+0x30B9);
         extendedKeyCodesSet.add(0x01000000+0x30BB);
         extendedKeyCodesSet.add(0x01000000+0x30BD);
         extendedKeyCodesSet.add(0x01000000+0x30BF);
         extendedKeyCodesSet.add(0x01000000+0x30C1);
         extendedKeyCodesSet.add(0x01000000+0x30C4);
         extendedKeyCodesSet.add(0x01000000+0x30C6);
         extendedKeyCodesSet.add(0x01000000+0x30C8);
         extendedKeyCodesSet.add(0x01000000+0x30CA);
         extendedKeyCodesSet.add(0x01000000+0x30CB);
         extendedKeyCodesSet.add(0x01000000+0x30CC);
         extendedKeyCodesSet.add(0x01000000+0x30CD);
         extendedKeyCodesSet.add(0x01000000+0x30CE);
         extendedKeyCodesSet.add(0x01000000+0x30CF);
         extendedKeyCodesSet.add(0x01000000+0x30D2);
         extendedKeyCodesSet.add(0x01000000+0x30D5);
         extendedKeyCodesSet.add(0x01000000+0x30D8);
         extendedKeyCodesSet.add(0x01000000+0x30DB);
         extendedKeyCodesSet.add(0x01000000+0x30DE);
         extendedKeyCodesSet.add(0x01000000+0x30DF);
         extendedKeyCodesSet.add(0x01000000+0x30E0);
         extendedKeyCodesSet.add(0x01000000+0x30E1);
         extendedKeyCodesSet.add(0x01000000+0x30E2);
         extendedKeyCodesSet.add(0x01000000+0x30E4);
         extendedKeyCodesSet.add(0x01000000+0x30E6);
         extendedKeyCodesSet.add(0x01000000+0x30E8);
         extendedKeyCodesSet.add(0x01000000+0x30E9);
         extendedKeyCodesSet.add(0x01000000+0x30EA);
         extendedKeyCodesSet.add(0x01000000+0x30EB);
         extendedKeyCodesSet.add(0x01000000+0x30EC);
         extendedKeyCodesSet.add(0x01000000+0x30ED);
         extendedKeyCodesSet.add(0x01000000+0x30EF);
         extendedKeyCodesSet.add(0x01000000+0x30F3);
         extendedKeyCodesSet.add(0x01000000+0x309B);
         extendedKeyCodesSet.add(0x01000000+0x309C);
         extendedKeyCodesSet.add(0x01000000+0x06F0);
         extendedKeyCodesSet.add(0x01000000+0x06F1);
         extendedKeyCodesSet.add(0x01000000+0x06F2);
         extendedKeyCodesSet.add(0x01000000+0x06F3);
         extendedKeyCodesSet.add(0x01000000+0x06F4);
         extendedKeyCodesSet.add(0x01000000+0x06F5);
         extendedKeyCodesSet.add(0x01000000+0x06F6);
         extendedKeyCodesSet.add(0x01000000+0x06F7);
         extendedKeyCodesSet.add(0x01000000+0x06F8);
         extendedKeyCodesSet.add(0x01000000+0x06F9);
         extendedKeyCodesSet.add(0x01000000+0x0670);
         extendedKeyCodesSet.add(0x01000000+0x067E);
         extendedKeyCodesSet.add(0x01000000+0x0686);
         extendedKeyCodesSet.add(0x01000000+0x060C);
         extendedKeyCodesSet.add(0x01000000+0x06D4);
         extendedKeyCodesSet.add(0x01000000+0x0660);
         extendedKeyCodesSet.add(0x01000000+0x0661);
         extendedKeyCodesSet.add(0x01000000+0x0662);
         extendedKeyCodesSet.add(0x01000000+0x0663);
         extendedKeyCodesSet.add(0x01000000+0x0664);
         extendedKeyCodesSet.add(0x01000000+0x0665);
         extendedKeyCodesSet.add(0x01000000+0x0666);
         extendedKeyCodesSet.add(0x01000000+0x0667);
         extendedKeyCodesSet.add(0x01000000+0x0668);
         extendedKeyCodesSet.add(0x01000000+0x0669);
         extendedKeyCodesSet.add(0x01000000+0x061B);
         extendedKeyCodesSet.add(0x01000000+0x0621);
         extendedKeyCodesSet.add(0x01000000+0x0624);
         extendedKeyCodesSet.add(0x01000000+0x0626);
         extendedKeyCodesSet.add(0x01000000+0x0627);
         extendedKeyCodesSet.add(0x01000000+0x0628);
         extendedKeyCodesSet.add(0x01000000+0x0629);
         extendedKeyCodesSet.add(0x01000000+0x062A);
         extendedKeyCodesSet.add(0x01000000+0x062B);
         extendedKeyCodesSet.add(0x01000000+0x062C);
         extendedKeyCodesSet.add(0x01000000+0x062D);
         extendedKeyCodesSet.add(0x01000000+0x062E);
         extendedKeyCodesSet.add(0x01000000+0x062F);
         extendedKeyCodesSet.add(0x01000000+0x0630);
         extendedKeyCodesSet.add(0x01000000+0x0631);
         extendedKeyCodesSet.add(0x01000000+0x0632);
         extendedKeyCodesSet.add(0x01000000+0x0633);
         extendedKeyCodesSet.add(0x01000000+0x0634);
         extendedKeyCodesSet.add(0x01000000+0x0635);
         extendedKeyCodesSet.add(0x01000000+0x0636);
         extendedKeyCodesSet.add(0x01000000+0x0637);
         extendedKeyCodesSet.add(0x01000000+0x0638);
         extendedKeyCodesSet.add(0x01000000+0x0639);
         extendedKeyCodesSet.add(0x01000000+0x063A);
         extendedKeyCodesSet.add(0x01000000+0x0641);
         extendedKeyCodesSet.add(0x01000000+0x0642);
         extendedKeyCodesSet.add(0x01000000+0x0643);
         extendedKeyCodesSet.add(0x01000000+0x0644);
         extendedKeyCodesSet.add(0x01000000+0x0645);
         extendedKeyCodesSet.add(0x01000000+0x0646);
         extendedKeyCodesSet.add(0x01000000+0x0647);
         extendedKeyCodesSet.add(0x01000000+0x0648);
         extendedKeyCodesSet.add(0x01000000+0x0649);
         extendedKeyCodesSet.add(0x01000000+0x064A);
         extendedKeyCodesSet.add(0x01000000+0x064E);
         extendedKeyCodesSet.add(0x01000000+0x064F);
         extendedKeyCodesSet.add(0x01000000+0x0650);
         extendedKeyCodesSet.add(0x01000000+0x0652);
         extendedKeyCodesSet.add(0x01000000+0x0698);
         extendedKeyCodesSet.add(0x01000000+0x06A4);
         extendedKeyCodesSet.add(0x01000000+0x06A9);
         extendedKeyCodesSet.add(0x01000000+0x06AF);
         extendedKeyCodesSet.add(0x01000000+0x06BE);
         extendedKeyCodesSet.add(0x01000000+0x06CC);
         extendedKeyCodesSet.add(0x01000000+0x06CC);
         extendedKeyCodesSet.add(0x01000000+0x06D2);
         extendedKeyCodesSet.add(0x01000000+0x0493);
         extendedKeyCodesSet.add(0x01000000+0x0497);
         extendedKeyCodesSet.add(0x01000000+0x049B);
         extendedKeyCodesSet.add(0x01000000+0x049D);
         extendedKeyCodesSet.add(0x01000000+0x04A3);
         extendedKeyCodesSet.add(0x01000000+0x04AF);
         extendedKeyCodesSet.add(0x01000000+0x04B1);
         extendedKeyCodesSet.add(0x01000000+0x04B3);
         extendedKeyCodesSet.add(0x01000000+0x04B9);
         extendedKeyCodesSet.add(0x01000000+0x04BB);
         extendedKeyCodesSet.add(0x01000000+0x04D9);
         extendedKeyCodesSet.add(0x01000000+0x04E9);
         extendedKeyCodesSet.add(0x01000000+0x0452);
         extendedKeyCodesSet.add(0x01000000+0x0453);
         extendedKeyCodesSet.add(0x01000000+0x0451);
         extendedKeyCodesSet.add(0x01000000+0x0454);
         extendedKeyCodesSet.add(0x01000000+0x0455);
         extendedKeyCodesSet.add(0x01000000+0x0456);
         extendedKeyCodesSet.add(0x01000000+0x0457);
         extendedKeyCodesSet.add(0x01000000+0x0458);
         extendedKeyCodesSet.add(0x01000000+0x0459);
         extendedKeyCodesSet.add(0x01000000+0x045A);
         extendedKeyCodesSet.add(0x01000000+0x045B);
         extendedKeyCodesSet.add(0x01000000+0x045C);
         extendedKeyCodesSet.add(0x01000000+0x0491);
         extendedKeyCodesSet.add(0x01000000+0x045E);
         extendedKeyCodesSet.add(0x01000000+0x045F);
         extendedKeyCodesSet.add(0x01000000+0x2116);
         extendedKeyCodesSet.add(0x01000000+0x044E);
         extendedKeyCodesSet.add(0x01000000+0x0430);
         extendedKeyCodesSet.add(0x01000000+0x0431);
         extendedKeyCodesSet.add(0x01000000+0x0446);
         extendedKeyCodesSet.add(0x01000000+0x0434);
         extendedKeyCodesSet.add(0x01000000+0x0435);
         extendedKeyCodesSet.add(0x01000000+0x0444);
         extendedKeyCodesSet.add(0x01000000+0x0433);
         extendedKeyCodesSet.add(0x01000000+0x0445);
         extendedKeyCodesSet.add(0x01000000+0x0438);
         extendedKeyCodesSet.add(0x01000000+0x0439);
         extendedKeyCodesSet.add(0x01000000+0x043A);
         extendedKeyCodesSet.add(0x01000000+0x043B);
         extendedKeyCodesSet.add(0x01000000+0x043C);
         extendedKeyCodesSet.add(0x01000000+0x043D);
         extendedKeyCodesSet.add(0x01000000+0x043E);
         extendedKeyCodesSet.add(0x01000000+0x043F);
         extendedKeyCodesSet.add(0x01000000+0x044F);
         extendedKeyCodesSet.add(0x01000000+0x0440);
         extendedKeyCodesSet.add(0x01000000+0x0441);
         extendedKeyCodesSet.add(0x01000000+0x0442);
         extendedKeyCodesSet.add(0x01000000+0x0443);
         extendedKeyCodesSet.add(0x01000000+0x0436);
         extendedKeyCodesSet.add(0x01000000+0x0432);
         extendedKeyCodesSet.add(0x01000000+0x044C);
         extendedKeyCodesSet.add(0x01000000+0x044B);
         extendedKeyCodesSet.add(0x01000000+0x0437);
         extendedKeyCodesSet.add(0x01000000+0x0448);
         extendedKeyCodesSet.add(0x01000000+0x044D);
         extendedKeyCodesSet.add(0x01000000+0x0449);
         extendedKeyCodesSet.add(0x01000000+0x0447);
         extendedKeyCodesSet.add(0x01000000+0x044A);
         extendedKeyCodesSet.add(0x01000000+0x2015);
         extendedKeyCodesSet.add(0x01000000+0x03B1);
         extendedKeyCodesSet.add(0x01000000+0x03B2);
         extendedKeyCodesSet.add(0x01000000+0x03B3);
         extendedKeyCodesSet.add(0x01000000+0x03B4);
         extendedKeyCodesSet.add(0x01000000+0x03B5);
         extendedKeyCodesSet.add(0x01000000+0x03B6);
         extendedKeyCodesSet.add(0x01000000+0x03B7);
         extendedKeyCodesSet.add(0x01000000+0x03B8);
         extendedKeyCodesSet.add(0x01000000+0x03B9);
         extendedKeyCodesSet.add(0x01000000+0x03BA);
         extendedKeyCodesSet.add(0x01000000+0x03BB);
         extendedKeyCodesSet.add(0x01000000+0x03BC);
         extendedKeyCodesSet.add(0x01000000+0x03BD);
         extendedKeyCodesSet.add(0x01000000+0x03BE);
         extendedKeyCodesSet.add(0x01000000+0x03BF);
         extendedKeyCodesSet.add(0x01000000+0x03C0);
         extendedKeyCodesSet.add(0x01000000+0x03C1);
         extendedKeyCodesSet.add(0x01000000+0x03C3);
         extendedKeyCodesSet.add(0x01000000+0x03C2);
         extendedKeyCodesSet.add(0x01000000+0x03C4);
         extendedKeyCodesSet.add(0x01000000+0x03C5);
         extendedKeyCodesSet.add(0x01000000+0x03C6);
         extendedKeyCodesSet.add(0x01000000+0x03C7);
         extendedKeyCodesSet.add(0x01000000+0x03C8);
         extendedKeyCodesSet.add(0x01000000+0x03C9);
         extendedKeyCodesSet.add(0x01000000+0x2190);
         extendedKeyCodesSet.add(0x01000000+0x2192);
         extendedKeyCodesSet.add(0x01000000+0x2193);
         extendedKeyCodesSet.add(0x01000000+0x2013);
         extendedKeyCodesSet.add(0x01000000+0x201C);
         extendedKeyCodesSet.add(0x01000000+0x201D);
         extendedKeyCodesSet.add(0x01000000+0x201E);
         extendedKeyCodesSet.add(0x01000000+0x05D0);
         extendedKeyCodesSet.add(0x01000000+0x05D1);
         extendedKeyCodesSet.add(0x01000000+0x05D2);
         extendedKeyCodesSet.add(0x01000000+0x05D3);
         extendedKeyCodesSet.add(0x01000000+0x05D4);
         extendedKeyCodesSet.add(0x01000000+0x05D5);
         extendedKeyCodesSet.add(0x01000000+0x05D6);
         extendedKeyCodesSet.add(0x01000000+0x05D7);
         extendedKeyCodesSet.add(0x01000000+0x05D8);
         extendedKeyCodesSet.add(0x01000000+0x05D9);
         extendedKeyCodesSet.add(0x01000000+0x05DA);
         extendedKeyCodesSet.add(0x01000000+0x05DB);
         extendedKeyCodesSet.add(0x01000000+0x05DC);
         extendedKeyCodesSet.add(0x01000000+0x05DD);
         extendedKeyCodesSet.add(0x01000000+0x05DE);
         extendedKeyCodesSet.add(0x01000000+0x05DF);
         extendedKeyCodesSet.add(0x01000000+0x05E0);
         extendedKeyCodesSet.add(0x01000000+0x05E1);
         extendedKeyCodesSet.add(0x01000000+0x05E2);
         extendedKeyCodesSet.add(0x01000000+0x05E3);
         extendedKeyCodesSet.add(0x01000000+0x05E4);
         extendedKeyCodesSet.add(0x01000000+0x05E5);
         extendedKeyCodesSet.add(0x01000000+0x05E6);
         extendedKeyCodesSet.add(0x01000000+0x05E7);
         extendedKeyCodesSet.add(0x01000000+0x05E8);
         extendedKeyCodesSet.add(0x01000000+0x05E9);
         extendedKeyCodesSet.add(0x01000000+0x05EA);
         extendedKeyCodesSet.add(0x01000000+0x0E01);
         extendedKeyCodesSet.add(0x01000000+0x0E02);
         extendedKeyCodesSet.add(0x01000000+0x0E03);
         extendedKeyCodesSet.add(0x01000000+0x0E04);
         extendedKeyCodesSet.add(0x01000000+0x0E05);
         extendedKeyCodesSet.add(0x01000000+0x0E07);
         extendedKeyCodesSet.add(0x01000000+0x0E08);
         extendedKeyCodesSet.add(0x01000000+0x0E0A);
         extendedKeyCodesSet.add(0x01000000+0x0E0C);
         extendedKeyCodesSet.add(0x01000000+0x0E14);
         extendedKeyCodesSet.add(0x01000000+0x0E15);
         extendedKeyCodesSet.add(0x01000000+0x0E16);
         extendedKeyCodesSet.add(0x01000000+0x0E17);
         extendedKeyCodesSet.add(0x01000000+0x0E19);
         extendedKeyCodesSet.add(0x01000000+0x0E1A);
         extendedKeyCodesSet.add(0x01000000+0x0E1B);
         extendedKeyCodesSet.add(0x01000000+0x0E1C);
         extendedKeyCodesSet.add(0x01000000+0x0E1D);
         extendedKeyCodesSet.add(0x01000000+0x0E1E);
         extendedKeyCodesSet.add(0x01000000+0x0E1F);
         extendedKeyCodesSet.add(0x01000000+0x0E20);
         extendedKeyCodesSet.add(0x01000000+0x0E21);
         extendedKeyCodesSet.add(0x01000000+0x0E22);
         extendedKeyCodesSet.add(0x01000000+0x0E23);
         extendedKeyCodesSet.add(0x01000000+0x0E25);
         extendedKeyCodesSet.add(0x01000000+0x0E27);
         extendedKeyCodesSet.add(0x01000000+0x0E2A);
         extendedKeyCodesSet.add(0x01000000+0x0E2B);
         extendedKeyCodesSet.add(0x01000000+0x0E2D);
         extendedKeyCodesSet.add(0x01000000+0x0E30);
         extendedKeyCodesSet.add(0x01000000+0x0E31);
         extendedKeyCodesSet.add(0x01000000+0x0E32);
         extendedKeyCodesSet.add(0x01000000+0x0E33);
         extendedKeyCodesSet.add(0x01000000+0x0E34);
         extendedKeyCodesSet.add(0x01000000+0x0E35);
         extendedKeyCodesSet.add(0x01000000+0x0E36);
         extendedKeyCodesSet.add(0x01000000+0x0E37);
         extendedKeyCodesSet.add(0x01000000+0x0E38);
         extendedKeyCodesSet.add(0x01000000+0x0E39);
         extendedKeyCodesSet.add(0x01000000+0x0E3F);
         extendedKeyCodesSet.add(0x01000000+0x0E40);
         extendedKeyCodesSet.add(0x01000000+0x0E41);
         extendedKeyCodesSet.add(0x01000000+0x0E43);
         extendedKeyCodesSet.add(0x01000000+0x0E44);
         extendedKeyCodesSet.add(0x01000000+0x0E45);
         extendedKeyCodesSet.add(0x01000000+0x0E46);
         extendedKeyCodesSet.add(0x01000000+0x0E47);
         extendedKeyCodesSet.add(0x01000000+0x0E48);
         extendedKeyCodesSet.add(0x01000000+0x0E49);
         extendedKeyCodesSet.add(0x01000000+0x0E50);
         extendedKeyCodesSet.add(0x01000000+0x0E51);
         extendedKeyCodesSet.add(0x01000000+0x0E52);
         extendedKeyCodesSet.add(0x01000000+0x0E53);
         extendedKeyCodesSet.add(0x01000000+0x0E54);
         extendedKeyCodesSet.add(0x01000000+0x0E55);
         extendedKeyCodesSet.add(0x01000000+0x0E56);
         extendedKeyCodesSet.add(0x01000000+0x0E57);
         extendedKeyCodesSet.add(0x01000000+0x0E58);
         extendedKeyCodesSet.add(0x01000000+0x0E59);
         extendedKeyCodesSet.add(0x01000000+0x0587);
         extendedKeyCodesSet.add(0x01000000+0x0589);
         extendedKeyCodesSet.add(0x01000000+0x0589);
         extendedKeyCodesSet.add(0x01000000+0x055D);
         extendedKeyCodesSet.add(0x01000000+0x055D);
         extendedKeyCodesSet.add(0x01000000+0x055B);
         extendedKeyCodesSet.add(0x01000000+0x055B);
         extendedKeyCodesSet.add(0x01000000+0x055E);
         extendedKeyCodesSet.add(0x01000000+0x055E);
         extendedKeyCodesSet.add(0x01000000+0x0561);
         extendedKeyCodesSet.add(0x01000000+0x0562);
         extendedKeyCodesSet.add(0x01000000+0x0563);
         extendedKeyCodesSet.add(0x01000000+0x0564);
         extendedKeyCodesSet.add(0x01000000+0x0565);
         extendedKeyCodesSet.add(0x01000000+0x0566);
         extendedKeyCodesSet.add(0x01000000+0x0567);
         extendedKeyCodesSet.add(0x01000000+0x0568);
         extendedKeyCodesSet.add(0x01000000+0x0569);
         extendedKeyCodesSet.add(0x01000000+0x056A);
         extendedKeyCodesSet.add(0x01000000+0x056B);
         extendedKeyCodesSet.add(0x01000000+0x056C);
         extendedKeyCodesSet.add(0x01000000+0x056D);
         extendedKeyCodesSet.add(0x01000000+0x056E);
         extendedKeyCodesSet.add(0x01000000+0x056F);
         extendedKeyCodesSet.add(0x01000000+0x0570);
         extendedKeyCodesSet.add(0x01000000+0x0571);
         extendedKeyCodesSet.add(0x01000000+0x0572);
         extendedKeyCodesSet.add(0x01000000+0x0573);
         extendedKeyCodesSet.add(0x01000000+0x0574);
         extendedKeyCodesSet.add(0x01000000+0x0575);
         extendedKeyCodesSet.add(0x01000000+0x0576);
         extendedKeyCodesSet.add(0x01000000+0x0577);
         extendedKeyCodesSet.add(0x01000000+0x0578);
         extendedKeyCodesSet.add(0x01000000+0x0579);
         extendedKeyCodesSet.add(0x01000000+0x057A);
         extendedKeyCodesSet.add(0x01000000+0x057B);
         extendedKeyCodesSet.add(0x01000000+0x057C);
         extendedKeyCodesSet.add(0x01000000+0x057D);
         extendedKeyCodesSet.add(0x01000000+0x057E);
         extendedKeyCodesSet.add(0x01000000+0x057F);
         extendedKeyCodesSet.add(0x01000000+0x0580);
         extendedKeyCodesSet.add(0x01000000+0x0581);
         extendedKeyCodesSet.add(0x01000000+0x0582);
         extendedKeyCodesSet.add(0x01000000+0x0583);
         extendedKeyCodesSet.add(0x01000000+0x0584);
         extendedKeyCodesSet.add(0x01000000+0x0585);
         extendedKeyCodesSet.add(0x01000000+0x0586);
         extendedKeyCodesSet.add(0x01000000+0x10D0);
         extendedKeyCodesSet.add(0x01000000+0x10D1);
         extendedKeyCodesSet.add(0x01000000+0x10D2);
         extendedKeyCodesSet.add(0x01000000+0x10D3);
         extendedKeyCodesSet.add(0x01000000+0x10D4);
         extendedKeyCodesSet.add(0x01000000+0x10D5);
         extendedKeyCodesSet.add(0x01000000+0x10D6);
         extendedKeyCodesSet.add(0x01000000+0x10D7);
         extendedKeyCodesSet.add(0x01000000+0x10D8);
         extendedKeyCodesSet.add(0x01000000+0x10D9);
         extendedKeyCodesSet.add(0x01000000+0x10DA);
         extendedKeyCodesSet.add(0x01000000+0x10DB);
         extendedKeyCodesSet.add(0x01000000+0x10DC);
         extendedKeyCodesSet.add(0x01000000+0x10DD);
         extendedKeyCodesSet.add(0x01000000+0x10DE);
         extendedKeyCodesSet.add(0x01000000+0x10DF);
         extendedKeyCodesSet.add(0x01000000+0x10E0);
         extendedKeyCodesSet.add(0x01000000+0x10E1);
         extendedKeyCodesSet.add(0x01000000+0x10E2);
         extendedKeyCodesSet.add(0x01000000+0x10E3);
         extendedKeyCodesSet.add(0x01000000+0x10E4);
         extendedKeyCodesSet.add(0x01000000+0x10E5);
         extendedKeyCodesSet.add(0x01000000+0x10E6);
         extendedKeyCodesSet.add(0x01000000+0x10E7);
         extendedKeyCodesSet.add(0x01000000+0x10E8);
         extendedKeyCodesSet.add(0x01000000+0x10E9);
         extendedKeyCodesSet.add(0x01000000+0x10EA);
         extendedKeyCodesSet.add(0x01000000+0x10EB);
         extendedKeyCodesSet.add(0x01000000+0x10EC);
         extendedKeyCodesSet.add(0x01000000+0x10ED);
         extendedKeyCodesSet.add(0x01000000+0x10EE);
         extendedKeyCodesSet.add(0x01000000+0x10EF);
         extendedKeyCodesSet.add(0x01000000+0x10F0);
         extendedKeyCodesSet.add(0x01000000+0x01E7);
         extendedKeyCodesSet.add(0x01000000+0x0259);
         extendedKeyCodesSet.add(0x01000000+0x1EB9);
         extendedKeyCodesSet.add(0x01000000+0x1ECB);
         extendedKeyCodesSet.add(0x01000000+0x1ECD);
         extendedKeyCodesSet.add(0x01000000+0x1EE5);
         extendedKeyCodesSet.add(0x01000000+0x01A1);
         extendedKeyCodesSet.add(0x01000000+0x01B0);
         extendedKeyCodesSet.add(0x01000000+0x20AB);
     }
}

/*
 * Copyright (c) 1999, Oracle and/or its affiliates. All rights reserved.
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

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */


package com.sun.tools.example.debug.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class Icons {

    private static int[]  exec  = {
  0xffd8ffe0, 0x00104a46, 0x49460001, 0x01020000
, 0x00000000, 0xffdb0043, 0x00020101, 0x01010102
, 0x01010102, 0x02020202, 0x04030202, 0x02020504
, 0x04030406, 0x05060606, 0x05060606, 0x07090806
, 0x07090706, 0x06080b08, 0x090a0a0a, 0x0a0a0608
, 0x0b0c0b0a, 0x0c090a0a, 0x0affdb00, 0x43010202
, 0x02020202, 0x05030305, 0x0a070607, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0affc0
, 0x00110800, 0x0c000c03, 0x01220002, 0x11010311
, 0x01ffc400, 0x1f000001, 0x05010101, 0x01010100
, 0x00000000, 0x00000001, 0x02030405, 0x06070809
, 0x0a0bffc4, 0x00b51000, 0x02010303, 0x02040305
, 0x05040400, 0x00017d01, 0x02030004, 0x11051221
, 0x31410613, 0x51610722, 0x71143281, 0x91a10823
, 0x42b1c115, 0x52d1f024, 0x33627282, 0x090a1617
, 0x18191a25, 0x26272829, 0x2a343536, 0x3738393a
, 0x43444546, 0x4748494a, 0x53545556, 0x5758595a
, 0x63646566, 0x6768696a, 0x73747576, 0x7778797a
, 0x83848586, 0x8788898a, 0x92939495, 0x96979899
, 0x9aa2a3a4, 0xa5a6a7a8, 0xa9aab2b3, 0xb4b5b6b7
, 0xb8b9bac2, 0xc3c4c5c6, 0xc7c8c9ca, 0xd2d3d4d5
, 0xd6d7d8d9, 0xdae1e2e3, 0xe4e5e6e7, 0xe8e9eaf1
, 0xf2f3f4f5, 0xf6f7f8f9, 0xfaffc400, 0x1f010003
, 0x01010101, 0x01010101, 0x01000000, 0x00000001
, 0x02030405, 0x06070809, 0x0a0bffc4, 0x00b51100
, 0x02010204, 0x04030407, 0x05040400, 0x01027700
, 0x01020311, 0x04052131, 0x06124151, 0x07617113
, 0x22328108, 0x144291a1, 0xb1c10923, 0x3352f015
, 0x6272d10a, 0x162434e1, 0x25f11718, 0x191a2627
, 0x28292a35, 0x36373839, 0x3a434445, 0x46474849
, 0x4a535455, 0x56575859, 0x5a636465, 0x66676869
, 0x6a737475, 0x76777879, 0x7a828384, 0x85868788
, 0x898a9293, 0x94959697, 0x98999aa2, 0xa3a4a5a6
, 0xa7a8a9aa, 0xb2b3b4b5, 0xb6b7b8b9, 0xbac2c3c4
, 0xc5c6c7c8, 0xc9cad2d3, 0xd4d5d6d7, 0xd8d9dae2
, 0xe3e4e5e6, 0xe7e8e9ea, 0xf2f3f4f5, 0xf6f7f8f9
, 0xfaffda00, 0x0c030100, 0x02110311, 0x003f00fd
, 0xbafda27e, 0x35ea1f03, 0x346f0ef8, 0x86cfc2d3
, 0x6b31ea9e, 0x2ab7d2ee, 0xf4fb38cb, 0x5cc91cb0
, 0xce4790a0, 0xfcd2ef44, 0xc29e1f95, 0xf94b065f
, 0x42a86eb4, 0xed3ef67b, 0x7b9bcb18, 0x6692ce63
, 0x35a492c4, 0x19a090a3, 0x465d09fb, 0xadb1dd72
, 0x39daec3a, 0x13535706, 0x1f0f8ca7, 0x8dad56a5
, 0x5e6a72e5, 0xe485be0b, 0x2b49df77, 0xcceda6ca
, 0xda6ece3a, 0x147150c5, 0xd5a93a97, 0x84b97963
, 0x6f86cbde, 0x77ddf33b, 0x69b2b69b, 0xb3ffd900

    };
    private static int[]  blank  = {
  0xffd8ffe0, 0x00104a46, 0x49460001, 0x01020000
, 0x00000000, 0xffdb0043, 0x00020101, 0x01010102
, 0x01010102, 0x02020202, 0x04030202, 0x02020504
, 0x04030406, 0x05060606, 0x05060606, 0x07090806
, 0x07090706, 0x06080b08, 0x090a0a0a, 0x0a0a0608
, 0x0b0c0b0a, 0x0c090a0a, 0x0affdb00, 0x43010202
, 0x02020202, 0x05030305, 0x0a070607, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0affc0
, 0x00110800, 0x0c000c03, 0x01220002, 0x11010311
, 0x01ffc400, 0x1f000001, 0x05010101, 0x01010100
, 0x00000000, 0x00000001, 0x02030405, 0x06070809
, 0x0a0bffc4, 0x00b51000, 0x02010303, 0x02040305
, 0x05040400, 0x00017d01, 0x02030004, 0x11051221
, 0x31410613, 0x51610722, 0x71143281, 0x91a10823
, 0x42b1c115, 0x52d1f024, 0x33627282, 0x090a1617
, 0x18191a25, 0x26272829, 0x2a343536, 0x3738393a
, 0x43444546, 0x4748494a, 0x53545556, 0x5758595a
, 0x63646566, 0x6768696a, 0x73747576, 0x7778797a
, 0x83848586, 0x8788898a, 0x92939495, 0x96979899
, 0x9aa2a3a4, 0xa5a6a7a8, 0xa9aab2b3, 0xb4b5b6b7
, 0xb8b9bac2, 0xc3c4c5c6, 0xc7c8c9ca, 0xd2d3d4d5
, 0xd6d7d8d9, 0xdae1e2e3, 0xe4e5e6e7, 0xe8e9eaf1
, 0xf2f3f4f5, 0xf6f7f8f9, 0xfaffc400, 0x1f010003
, 0x01010101, 0x01010101, 0x01000000, 0x00000001
, 0x02030405, 0x06070809, 0x0a0bffc4, 0x00b51100
, 0x02010204, 0x04030407, 0x05040400, 0x01027700
, 0x01020311, 0x04052131, 0x06124151, 0x07617113
, 0x22328108, 0x144291a1, 0xb1c10923, 0x3352f015
, 0x6272d10a, 0x162434e1, 0x25f11718, 0x191a2627
, 0x28292a35, 0x36373839, 0x3a434445, 0x46474849
, 0x4a535455, 0x56575859, 0x5a636465, 0x66676869
, 0x6a737475, 0x76777879, 0x7a828384, 0x85868788
, 0x898a9293, 0x94959697, 0x98999aa2, 0xa3a4a5a6
, 0xa7a8a9aa, 0xb2b3b4b5, 0xb6b7b8b9, 0xbac2c3c4
, 0xc5c6c7c8, 0xc9cad2d3, 0xd4d5d6d7, 0xd8d9dae2
, 0xe3e4e5e6, 0xe7e8e9ea, 0xf2f3f4f5, 0xf6f7f8f9
, 0xfaffda00, 0x0c030100, 0x02110311, 0x003f00fd
, 0xfca28a28, 0x03ffd900

    };

   private static int[] stopSignWords = {
  0xffd8ffe0, 0x00104a46, 0x49460001, 0x01020000
, 0x00000000, 0xffdb0043, 0x00020101, 0x01010102
, 0x01010102, 0x02020202, 0x04030202, 0x02020504
, 0x04030406, 0x05060606, 0x05060606, 0x07090806
, 0x07090706, 0x06080b08, 0x090a0a0a, 0x0a0a0608
, 0x0b0c0b0a, 0x0c090a0a, 0x0affdb00, 0x43010202
, 0x02020202, 0x05030305, 0x0a070607, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a
, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0a0a0a, 0x0a0affc0
, 0x00110800, 0x0c000c03, 0x01220002, 0x11010311
, 0x01ffc400, 0x1f000001, 0x05010101, 0x01010100
, 0x00000000, 0x00000001, 0x02030405, 0x06070809
, 0x0a0bffc4, 0x00b51000, 0x02010303, 0x02040305
, 0x05040400, 0x00017d01, 0x02030004, 0x11051221
, 0x31410613, 0x51610722, 0x71143281, 0x91a10823
, 0x42b1c115, 0x52d1f024, 0x33627282, 0x090a1617
, 0x18191a25, 0x26272829, 0x2a343536, 0x3738393a
, 0x43444546, 0x4748494a, 0x53545556, 0x5758595a
, 0x63646566, 0x6768696a, 0x73747576, 0x7778797a
, 0x83848586, 0x8788898a, 0x92939495, 0x96979899
, 0x9aa2a3a4, 0xa5a6a7a8, 0xa9aab2b3, 0xb4b5b6b7
, 0xb8b9bac2, 0xc3c4c5c6, 0xc7c8c9ca, 0xd2d3d4d5
, 0xd6d7d8d9, 0xdae1e2e3, 0xe4e5e6e7, 0xe8e9eaf1
, 0xf2f3f4f5, 0xf6f7f8f9, 0xfaffc400, 0x1f010003
, 0x01010101, 0x01010101, 0x01000000, 0x00000001
, 0x02030405, 0x06070809, 0x0a0bffc4, 0x00b51100
, 0x02010204, 0x04030407, 0x05040400, 0x01027700
, 0x01020311, 0x04052131, 0x06124151, 0x07617113
, 0x22328108, 0x144291a1, 0xb1c10923, 0x3352f015
, 0x6272d10a, 0x162434e1, 0x25f11718, 0x191a2627
, 0x28292a35, 0x36373839, 0x3a434445, 0x46474849
, 0x4a535455, 0x56575859, 0x5a636465, 0x66676869
, 0x6a737475, 0x76777879, 0x7a828384, 0x85868788
, 0x898a9293, 0x94959697, 0x98999aa2, 0xa3a4a5a6
, 0xa7a8a9aa, 0xb2b3b4b5, 0xb6b7b8b9, 0xbac2c3c4
, 0xc5c6c7c8, 0xc9cad2d3, 0xd4d5d6d7, 0xd8d9dae2
, 0xe3e4e5e6, 0xe7e8e9ea, 0xf2f3f4f5, 0xf6f7f8f9
, 0xfaffda00, 0x0c030100, 0x02110311, 0x003f00f8
, 0xe7e37fc6, 0xff00197f, 0xc142fc65, 0x17ed5bfb
, 0x56db699e, 0x27f14f89, 0xf4cb7b85, 0x5bcd3924
, 0xb5d1ed5d, 0x3cc8b4db, 0x08a4ddf6, 0x6b387cc6
, 0x09182599, 0x99e595e5, 0x9e69a693, 0xbaf0dffc
, 0x1c9dff00, 0x050aff00, 0x82637837, 0x44fd94be
, 0x11e89f0f, 0xfc61e16d, 0x334c5b8f, 0x0f37c45d
, 0x26fef2eb, 0x46b56778, 0xd34db796, 0xd6fadbfd
, 0x0e2f2898, 0xa3903b42, 0xb21891d6, 0x08e08623
, 0xfe0e4ef0, 0xdf837fe0, 0x98dff050, 0xbb2f847f
, 0xb2978274, 0xcd33c2de, 0x30f87f69, 0xe2e6f0f5
, 0xe44ef6ba, 0x35d5c5fe, 0xa16b2dad, 0x8246d1f9
, 0x167fe84b, 0x2a40772c, 0x2d33c717, 0x9702c304
, 0x5fb0dff0, 0x4abff825, 0x5ffc13d7, 0xc55ff04f
, 0x5f845f17, 0x3e2e7ec8, 0xbf0ffe21, 0xf8a7e21f
, 0xc3fd1fc5, 0xde21f10f, 0xc45f0758, 0x6b774b75
, 0xa9584174, 0xf6b6ef75, 0x0b7d9ace, 0x1f304514
, 0x11ed50a8, 0x647f3279, 0x679e5fcf, 0x720cbb37
, 0xc3f1257a, 0x95eb7343, 0xdebabc9d, 0xeef4d1ab
, 0x2b7e1b2d, 0x0fec0f16, 0xb8c7c3cc, 0xdbc15caf
, 0x0795e59e, 0xc710fd97, 0x2cfd9d38, 0xf2f241aa
, 0x9efc64e5, 0x2e67dd7b, 0xdf14acd1, 0xffd90000

};

    static private byte[] wordsToBytes(int[] wordArray) {
        byte[] bytes = new byte[wordArray.length * 4];
        int inx = bytes.length;
        for (int i = wordArray.length-1; i >= 0; --i) {
            int word = wordArray[i];
            for (int j = 0; j < 4; ++j) {
                bytes[--inx] = (byte)(word & 0xff);
                word = word >>> 8;
            }
        }
        return bytes;
    }

    static Icon stopSignIcon = new ImageIcon(wordsToBytes(stopSignWords));
    static Icon blankIcon = new ImageIcon(wordsToBytes(blank));

    static Icon execIcon = new ImageIcon(wordsToBytes(exec));
}

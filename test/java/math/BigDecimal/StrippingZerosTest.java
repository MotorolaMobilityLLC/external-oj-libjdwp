/*
 * Copyright 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

/*
 * @test
 * @bug 4108852
 * @summary A few tests of stripTrailingZeros
 * @author Joseph D. Darcy
 */

import java.math.*;

public class StrippingZerosTest {
    public static void main(String argv[]) {
        BigDecimal [][] testCases = {
            {new BigDecimal("1.00000"),         new BigDecimal("1")},
            {new BigDecimal("1.000"),           new BigDecimal("1")},
            {new BigDecimal("1"),               new BigDecimal("1")},
            {new BigDecimal("0.1234"),          new BigDecimal("0.1234")},
            {new BigDecimal("0.12340"),         new BigDecimal("0.1234")},
            {new BigDecimal("0.12340000000"),   new BigDecimal("0.1234")},
            {new BigDecimal("1234.5678"),       new BigDecimal("1234.5678")},
            {new BigDecimal("1234.56780"),      new BigDecimal("1234.5678")},
            {new BigDecimal("1234.567800000"),  new BigDecimal("1234.5678")},
            {new BigDecimal("0"),               new BigDecimal("0")},
            {new BigDecimal("0e100"),           new BigDecimal("0e100")},
            {new BigDecimal("0e-100"),          new BigDecimal("0e-100")},
            {new BigDecimal("10"),              new BigDecimal("1e1")},
            {new BigDecimal("20"),              new BigDecimal("2e1")},
            {new BigDecimal("100"),             new BigDecimal("1e2")},
            {new BigDecimal("1000000000"),      new BigDecimal("1e9")},
            {new BigDecimal("100000000e1"),     new BigDecimal("1e9")},
            {new BigDecimal("10000000e2"),      new BigDecimal("1e9")},
            {new BigDecimal("1000000e3"),       new BigDecimal("1e9")},
            {new BigDecimal("100000e4"),        new BigDecimal("1e9")},
        };

        for(int i = 0; i < testCases.length; i++) {

            if (!(testCases[i][0]).stripTrailingZeros().equals(testCases[i][1])) {
                throw new RuntimeException("For input " + testCases[i][0].toString() +
                                           " did not received expected result " +
                                           testCases[i][1].toString() + ",  got " +
                                           testCases[i][0].stripTrailingZeros());
            }

            testCases[i][0] = testCases[i][0].negate();
            testCases[i][1] = testCases[i][1].negate();

            if (!(testCases[i][0]).stripTrailingZeros().equals(testCases[i][1])) {
                throw new RuntimeException("For input " + testCases[i][0].toString() +
                                           " did not received expected result " +
                                           testCases[i][1].toString() + ",  got " +
                                           testCases[i][0].stripTrailingZeros());
            }

        }
    }
}

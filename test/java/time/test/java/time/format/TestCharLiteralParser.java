/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package test.java.time.format;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.format.DateTimeBuilder;
import java.text.ParsePosition;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test CharLiteralPrinterParser.
 */
@Test(groups={"implementation"})
public class TestCharLiteralParser extends AbstractTestPrinterParser {

    @DataProvider(name="success")
    Object[][] data_success() {
        return new Object[][] {
            // match
            {'a', true, "a", 0, 1},
            {'a', true, "aOTHER", 0, 1},
            {'a', true, "OTHERaOTHER", 5, 6},
            {'a', true, "OTHERa", 5, 6},

            // no match
            {'a', true, "", 0, 0},
            {'a', true, "a", 1, 1},
            {'a', true, "A", 0, 0},
            {'a', true, "b", 0, 0},
            {'a', true, "OTHERbOTHER", 5, 5},
            {'a', true, "OTHERb", 5, 5},

            // case insensitive
            {'a', false, "a", 0, 1},
            {'a', false, "A", 0, 1},
        };
    }

    @Test(dataProvider="success")
    public void test_parse_success(char c, boolean caseSensitive,
                                   String text, int pos, int expectedPos) {
        setCaseSensitive(caseSensitive);
        ParsePosition ppos = new ParsePosition(pos);
        DateTimeBuilder result =
               getFormatter(c).parseToBuilder(text, ppos);
        if (ppos.getErrorIndex() != -1) {
            assertEquals(ppos.getIndex(), expectedPos);
        } else {
            assertEquals(ppos.getIndex(), expectedPos);
            assertEquals(result.getCalendricalList().size(), 0);
        }
    }

    //-----------------------------------------------------------------------
    @DataProvider(name="error")
    Object[][] data_error() {
        return new Object[][] {
            {'a', "a", -1, IndexOutOfBoundsException.class},
            {'a', "a", 2, IndexOutOfBoundsException.class},
        };
    }

    @Test(dataProvider="error")
    public void test_parse_error(char c, String text, int pos, Class<?> expected) {
        try {
            DateTimeBuilder result =
               getFormatter(c).parseToBuilder(text, new ParsePosition(pos));
            assertTrue(false);
        } catch (RuntimeException ex) {
            assertTrue(expected.isInstance(ex));
        }
    }
}

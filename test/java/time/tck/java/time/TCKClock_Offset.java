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
 * Copyright (c) 2008-2012 Stephen Colebourne & Michael Nascimento Santos
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
package tck.java.time;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.testng.annotations.Test;

/**
 * Test offset clock.
 */
@Test
public class TCKClock_Offset extends AbstractTCKTest {

    private static final ZoneId MOSCOW = ZoneId.of("Europe/Moscow");
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");
    private static final Instant INSTANT = LocalDateTime.of(2008, 6, 30, 11, 30, 10, 500).atZone(ZoneOffset.ofHours(2)).toInstant();
    private static final Duration OFFSET = Duration.ofSeconds(2);

    //-----------------------------------------------------------------------
    public void test_isSerializable() throws IOException, ClassNotFoundException {
        assertSerializable(Clock.offset(Clock.system(PARIS), OFFSET));
    }

    //-----------------------------------------------------------------------
    public void test_offset_ClockDuration() {
        Clock test = Clock.offset(Clock.fixed(INSTANT, PARIS), OFFSET);
        System.out.println(test.instant());
        System.out.println(INSTANT.plus(OFFSET));
        assertEquals(test.instant(), INSTANT.plus(OFFSET));
        assertEquals(test.getZone(), PARIS);
    }

    public void test_offset_ClockDuration_zeroDuration() {
        Clock underlying = Clock.system(PARIS);
        Clock test = Clock.offset(underlying, Duration.ZERO);
        assertSame(test, underlying);  // spec says same
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_offset_ClockDuration_nullClock() {
        Clock.offset(null, Duration.ZERO);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_offset_ClockDuration_nullDuration() {
        Clock.offset(Clock.systemUTC(), null);
    }

    //-------------------------------------------------------------------------
    public void test_withZone() {
        Clock test = Clock.offset(Clock.system(PARIS), OFFSET);
        Clock changed = test.withZone(MOSCOW);
        assertEquals(test.getZone(), PARIS);
        assertEquals(changed.getZone(), MOSCOW);
    }

    public void test_withZone_equal() {
        Clock test = Clock.offset(Clock.system(PARIS), OFFSET);
        Clock changed = test.withZone(PARIS);
        assertEquals(test, changed);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_withZone_null() {
        Clock.offset(Clock.system(PARIS), OFFSET).withZone(null);
    }

    //-----------------------------------------------------------------------
    public void test_equals() {
        Clock a = Clock.offset(Clock.system(PARIS), OFFSET);
        Clock b = Clock.offset(Clock.system(PARIS), OFFSET);
        assertEquals(a.equals(a), true);
        assertEquals(a.equals(b), true);
        assertEquals(b.equals(a), true);
        assertEquals(b.equals(b), true);

        Clock c = Clock.offset(Clock.system(MOSCOW), OFFSET);
        assertEquals(a.equals(c), false);

        Clock d = Clock.offset(Clock.system(PARIS), OFFSET.minusNanos(1));
        assertEquals(a.equals(d), false);

        assertEquals(a.equals(null), false);
        assertEquals(a.equals("other type"), false);
        assertEquals(a.equals(Clock.systemUTC()), false);
    }

    public void test_hashCode() {
        Clock a = Clock.offset(Clock.system(PARIS), OFFSET);
        Clock b = Clock.offset(Clock.system(PARIS), OFFSET);
        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(a.hashCode(), b.hashCode());

        Clock c = Clock.offset(Clock.system(MOSCOW), OFFSET);
        assertEquals(a.hashCode() == c.hashCode(), false);

        Clock d = Clock.offset(Clock.system(PARIS), OFFSET.minusNanos(1));
        assertEquals(a.hashCode() == d.hashCode(), false);
    }

    //-----------------------------------------------------------------------
    public void test_toString() {
        // spec requires "full state" in toString()
        Clock test = Clock.offset(Clock.system(PARIS), OFFSET);
        assertEquals(test.toString().contains("Europe/Paris"), true);
        assertEquals(test.toString().contains("PT2S"), true);
    }

}

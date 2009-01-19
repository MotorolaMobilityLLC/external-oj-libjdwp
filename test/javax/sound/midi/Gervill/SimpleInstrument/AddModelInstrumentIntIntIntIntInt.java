/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

/* @test
   @summary Test SimpleInstrument add(ModelInstrument,int,int,int,int,int) method */

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class AddModelInstrumentIntIntIntIntInt {

    private static void assertEquals(Object a, Object b) throws Exception
    {
        if(!a.equals(b))
            throw new RuntimeException("assertEquals fails!");
    }

    public static void main(String[] args) throws Exception {

        SimpleInstrument instrument = new SimpleInstrument();

        ModelPerformer[] performers = new ModelPerformer[2];

        performers[0] = new ModelPerformer();
        performers[0].setExclusiveClass(1);
        performers[0].setKeyFrom(36);
        performers[0].setKeyTo(48);
        performers[0].setVelFrom(16);
        performers[0].setVelTo(80);
        performers[0].setSelfNonExclusive(true);
        performers[0].setDefaultConnectionsEnabled(false);
        performers[0].getConnectionBlocks().add(new ModelConnectionBlock());
        performers[0].getOscillators().add(new ModelByteBufferWavetable(new ModelByteBuffer(new byte[] {1,2,3})));

        performers[1] = new ModelPerformer();
        performers[1].setExclusiveClass(0);
        performers[1].setKeyFrom(12);
        performers[1].setKeyTo(24);
        performers[1].setVelFrom(20);
        performers[1].setVelTo(90);
        performers[1].setSelfNonExclusive(false);
        performers[0].setDefaultConnectionsEnabled(true);
        performers[1].getConnectionBlocks().add(new ModelConnectionBlock());
        performers[1].getOscillators().add(new ModelByteBufferWavetable(new ModelByteBuffer(new byte[] {1,2,3})));

        SimpleInstrument subins = new SimpleInstrument();
        subins.add(performers[0]);
        instrument.add(subins,18,40,20,75,12);
        ModelPerformer[] performers2 = instrument.getPerformers();
        for (int i = 0; i < performers2.length; i++) {
            assertEquals(performers[i].getConnectionBlocks(), performers2[i].getConnectionBlocks());
            assertEquals(12, performers2[i].getExclusiveClass());
            if(performers[i].getKeyFrom() < 18)
                assertEquals(18, performers2[i].getKeyFrom());
            else
                assertEquals(performers[i].getKeyFrom(), performers2[i].getKeyFrom());
            if(performers[i].getKeyTo() > 40)
                assertEquals(40, performers2[i].getKeyTo());
            else
                assertEquals(performers[i].getKeyTo(), performers2[i].getKeyTo());
            if(performers[i].getVelFrom() < 20)
                assertEquals(20, performers2[i].getVelFrom());
            else
                assertEquals(performers[i].getVelFrom(), performers2[i].getVelFrom());
            if(performers[i].getVelTo() > 75)
                assertEquals(75, performers2[i].getVelTo());
            else
                assertEquals(performers[i].getVelTo(), performers2[i].getVelTo());
            assertEquals(performers[i].getOscillators(), performers2[i].getOscillators());
            assertEquals(performers[i].isSelfNonExclusive(), performers2[i].isSelfNonExclusive());
            assertEquals(performers[i].isDefaultConnectionsEnabled(), performers2[i].isDefaultConnectionsEnabled());
        }
    }
}

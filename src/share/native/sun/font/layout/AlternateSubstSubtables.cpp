/*
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
 *
 */

/*
 *
 * (C) Copyright IBM Corp. 1998-2004 - All Rights Reserved
 *
 */

#include "LETypes.h"
#include "LEGlyphFilter.h"
#include "OpenTypeTables.h"
#include "GlyphSubstitutionTables.h"
#include "AlternateSubstSubtables.h"
#include "GlyphIterator.h"
#include "LESwaps.h"

U_NAMESPACE_BEGIN

le_uint32 AlternateSubstitutionSubtable::process(GlyphIterator *glyphIterator, const LEGlyphFilter *filter) const
{
    // NOTE: For now, we'll just pick the first alternative...
    LEGlyphID glyph = glyphIterator->getCurrGlyphID();
    le_int32 coverageIndex = getGlyphCoverage(glyph);

    if (coverageIndex >= 0) {
        le_uint16 altSetCount = SWAPW(alternateSetCount);

        if (coverageIndex < altSetCount) {
            Offset alternateSetTableOffset = SWAPW(alternateSetTableOffsetArray[coverageIndex]);
            const AlternateSetTable *alternateSetTable =
                (const AlternateSetTable *) ((char *) this + alternateSetTableOffset);
            TTGlyphID alternate = SWAPW(alternateSetTable->alternateArray[0]);

            if (filter == NULL || filter->accept(LE_SET_GLYPH(glyph, alternate))) {
                glyphIterator->setCurrGlyphID(SWAPW(alternateSetTable->alternateArray[0]));
            }

            return 1;
        }

        // XXXX If we get here, the table's mal-formed...
    }

    return 0;
}

U_NAMESPACE_END

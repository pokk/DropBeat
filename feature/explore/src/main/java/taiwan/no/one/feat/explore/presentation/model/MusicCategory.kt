/*
 * MIT License
 *
 * Copyright (c) 2020 Jieyi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package taiwan.no.one.feat.explore.presentation.model

import androidx.annotation.DrawableRes
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import taiwan.no.one.feat.explore.R

internal enum class MusicCategory(val text: String, @DrawableRes val imageResId: Int) {
    ALTERNATIVE("alternative", R.drawable.bg_alt),
    ALTERNATIVE_ROCK("alternative rock", R.drawable.bg_alt_rock),
    CLASSIC("classic", R.drawable.bg_classic),
    DANCE("dance", R.drawable.bg_dance),
    ELECTRONIC("electronic", R.drawable.bg_ele),
    FEMALE_VOC("female vocalists", R.drawable.bg_vocal),
    INDIE("indie", R.drawable.bg_indie),
    SEEN_LIVE("seen live", R.drawable.bg_concert),
    JAZZ("jazz", R.drawable.bg_jazz),
    METAL("metal", R.drawable.bg_metal),
    POP("pop", R.drawable.bg_pop),
    ROCK("rock", R.drawable.bg_rock);

    companion object {
        fun getArrayMap(): ArrayMap<String, MusicCategory> {
            val map = arrayMapOf<String, MusicCategory>()
            values().forEach { map[it.text] = it }
            return map
        }
    }
}

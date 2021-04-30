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

package taiwan.no.one.dropbeat.presentation.models

import androidx.annotation.DrawableRes
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import taiwan.no.one.dropbeat.R

enum class PlaylistCategory(val text: String, @DrawableRes val imageResId: Int) {
    DOWNLOADED("downloaded", R.drawable.bg_downloaded),
    FAVORITE("favorite", R.drawable.bg_favorite),
    UNCATEGORIES("uncategory", R.drawable.bg_uncategory);

    companion object {
        fun getArrayMap(): ArrayMap<String, PlaylistCategory> {
            val map = arrayMapOf<String, PlaylistCategory>()
            values().forEach { map[it.text] = it }
            return map
        }
    }
}

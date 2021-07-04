/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
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
package taiwan.no.one.mediaplayer.lyric

import androidx.annotation.WorkerThread
import java.io.StringReader

/**
 * Parse the lyrics and get the collection of [LrcRowEntity].
 */
class DefaultLrcBuilder : ILrcBuilder {
    @WorkerThread
    override fun getLrcRows(rawLrc: String): List<LrcRowEntity> {
        if (rawLrc.isEmpty()) return emptyList()
        val rows = mutableListOf<LrcRowEntity>()
        // Read each line of the lyrics in a loop
        StringReader(rawLrc).useLines {
            /**
             * There is only one line of lyrics for example: "I miss you so much"
             * [01:15.33]I miss you so much
             * One line of lyrics has multiple times. For example: Grasshopper "Broken Love Front Alliance"
             * [02:34.14][01:07.00]When you and I accidentally think of her
             * [02:45.69][02:42.20][02:37.69][01:10.60]Just draw a cross in memory
             */
            it.forEach { lrcStr ->
                // Analyze each line of lyrics to get the collection of each line of lyrics, because some lyrics
                // are repeated for multiple times, you can parse out multiple lines of lyrics
                val lrcRow = LrcRowEntity.createRows(lrcStr)
                lrcRow.forEach(rows::add)
            }
            // Sort according to the time of the lyrics line
            if (rows.isNotEmpty()) rows.sort()
        }
        return rows
    }
}

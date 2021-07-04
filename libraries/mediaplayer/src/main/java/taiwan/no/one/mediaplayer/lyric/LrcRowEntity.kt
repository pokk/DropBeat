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
import taiwan.no.one.mediaplayer.exceptions.LyricFormatException

/**
 * Lyric line Including the time and content of the lyrics.
 */
data class LrcRowEntity(
    // The time when the lyrics of the line should start playing, in the following format: [02:34.14].
    val strTime: String?,
    // The time when the lyrics of this line are to be played, converted from [02:34.14] format to long type, The long
    // value obtained after turning 2 minutes, 34 seconds and 14 milliseconds into milliseconds:
    // time = 02 * 60 * 1000 + 34 * 1000 + 14.
    val time: Long,
    // The content of the line's lyrics.
    val content: String?
) : Comparable<LrcRowEntity> {
    companion object {
        private const val UNIT_OF_MINUTE = 1000 * 60
        private const val UNIT_OF_SECOND_UNIT = 1000
        private const val POS_OPENED_BRACKET = 0
        private const val POS_CLOSED_BRACKET = 9
        private const val OPENED_BRACKET = '['
        private const val CLOSED_BRACKET = ']'
        private const val OTHER_COMMON_SPLIT_CHAR = '-'
        private const val SEPARATED_MIN = ':'
        private const val SEPARATED_MS_MIN = '.'

        /**
         * Read each line of lyrics, convert it to [LrcRowEntity], and add it to the collection.
         * There is only one line of lyrics for example: "I miss you so much"
         * [01:15.33]I miss you so much
         * One line of lyrics has multiple times. For example: Grasshopper "Broken Love Front Alliance"
         * [02:34.14][01:07.00]When you and I accidentally think of her
         * [02:45.69][02:42.20][02:37.69][01:10.60]Just draw a cross in memory
         */
        @WorkerThread
        fun createRows(standardLrcLine: String) = try {
            if (standardLrcLine.indexOf(OPENED_BRACKET) != POS_OPENED_BRACKET ||
                standardLrcLine.indexOf(CLOSED_BRACKET) != POS_CLOSED_BRACKET) {
                throw LyricFormatException("The time format is wrong.")
            }
            // [02:34.14][01:07.00] When you and I accidentally think of her
            // Find the position of the last ‘]’
            val lastIndexOfRightBracket = standardLrcLine.lastIndexOf(CLOSED_BRACKET)
            // The content of the lyrics is the text after the position of ‘]’ eg: When you
            // and I accidentally think of her
            val content = standardLrcLine.substring(lastIndexOfRightBracket + 1, standardLrcLine.length)
            // Lyrics time is the text before the position of ‘]’ eg: [02:34.14][01:07.00]
            //
            // Convert the time format [mm:ss.SS][mm:ss.SS] to -mm:ss.SS--mm:ss.SS-
            // That is: [02:34.14][01:07.00] is converted to -02:34.14--01:07.00-
            standardLrcLine.substring(0, lastIndexOfRightBracket + 1)
                .replace(CLOSED_BRACKET, OTHER_COMMON_SPLIT_CHAR)
                .replace(OPENED_BRACKET, OTHER_COMMON_SPLIT_CHAR)
                // Use ‘-’ to split the string.
                .split(OTHER_COMMON_SPLIT_CHAR)
                .asSequence()
                // Filter redundant blank string.
                .filter { it.trim().isNotBlank() }
                // [02:34.14][01:07.00]When you and I accidentally think of her
                //
                // The lyrics above can be split into the following two lyrics
                // [02:34.14]When you and I accidentally think of her
                // [01:07.00]When you and I accidentally think of her
                .map { LrcRowEntity(it, timeConvert(it), content) }
                .toList()
        }
        catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

        /**
         * Convert the parsed character representing time into Long type.
         */
        private fun timeConvert(timeString: String): Long {
            // Because the time format of the string given to such is XX:XX.XX, the returned
            // long requirement is in milliseconds
            // Convert the string XX:XX.XX to XX:XX:XX
            // Split the string XX:XX:XX
            val times = timeString.replace(SEPARATED_MS_MIN, SEPARATED_MIN).split(SEPARATED_MIN)
            // mm:ss:SS
            return times[0].toLong() * UNIT_OF_MINUTE + // minute
                times[1].toLong() * UNIT_OF_SECOND_UNIT + // second
                times[2].toLong() // millisecond
        }
    }

    override fun toString() = "[$strTime] $content"

    /**
     * When sorting, sort according to the time of the lyrics.
     */
    override operator fun compareTo(other: LrcRowEntity) = (time - other.time).toInt()
}

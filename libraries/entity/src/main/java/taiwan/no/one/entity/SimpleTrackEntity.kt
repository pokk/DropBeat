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

package taiwan.no.one.entity

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * [SimpleTrackEntity] is for global usage and it only keeps brief information.
 */
@Keep
@Parcelize
data class SimpleTrackEntity(
    val id: Int,
    val name: String,
    val artist: String,
    val uri: String,
    val localUri: String,
    val thumbUri: String,
    val lyricUri: String,
    val duration: Int,
    var isFavorite: Boolean,
    val isOwn: Boolean,
    var refPath: String = "",
) : Parcelable {
    fun obtainTrackAndArtistName() = "$artist++$name"

    fun toSet() = mapOf(
        "id" to id,
        "name" to name,
        "artist_name" to artist,
        "uri" to uri,
        "thumb_uri" to thumbUri,
        "lyric_uri" to lyricUri,
        "duration" to duration,
    )
}

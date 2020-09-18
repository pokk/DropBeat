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

package taiwan.no.one.dropbeat.data.mappers

import taiwan.no.one.mediaplayer.MusicInfo

object TrackMapper {
    fun musicInfoToSongEntitiesJson(music: MusicInfo) = buildString {
        append("""[${musicInfoToSongEntityJson(music)}]""")
    }

    fun musicInfoListToSongEntitiesJson(musics: List<MusicInfo>) = buildString {
        musics.map { musicInfoToSongEntityJson(it) }.joinToString(",", "[", "]")
    }

    private fun musicInfoToSongEntityJson(music: MusicInfo) = buildString {
        append("""{
    "title" : "${music.title}",
    "artist" : "${music.artist}",
    "uri" : "${music.uri}",
    "cover_uri" : "${music.thumbUri}",
    "duration" : ${music.duration},
    "has_own" : false
}""".trimIndent())
    }
}

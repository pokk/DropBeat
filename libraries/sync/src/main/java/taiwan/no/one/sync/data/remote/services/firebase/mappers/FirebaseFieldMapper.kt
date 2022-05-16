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

package taiwan.no.one.sync.data.remote.services.firebase.mappers

import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ext.DEFAULT_INT
import taiwan.no.one.ext.DEFAULT_LONG
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.ext.extensions.now
import taiwan.no.one.sync.data.remote.services.firebase.castToDocList

object FirebaseFieldMapper {
    fun simplePlaylistToFieldMap(playlistEntity: SimplePlaylistEntity) = playlistEntity.run {
        mapOf(
            "id" to id,
            "name" to name,
            "thumb_url" to thumbUrl,
            "last_synced_time" to syncedStamp,
        )
    }

    fun fieldMapToSimplePlaylist(fieldMap: Map<String, Any>, refPath: String) = fieldMap.run {
        SimplePlaylistEntity(
            (get("id") as? Long)?.toInt() ?: DEFAULT_INT,
            get("name") as? String ?: DEFAULT_STR,
            emptyList(),
            get("thumb_url") as? String ?: DEFAULT_STR,
            now(),
            refPath,
            castToDocList(get("songs")).orEmpty().map { it.path },
            get("last_synced_time") as? Long ?: DEFAULT_LONG,
        )
    }

    fun simpleTrackToFieldMap(trackEntity: SimpleTrackEntity) = trackEntity.run {
        mapOf(
            "id" to id,
            "name" to name,
            "artist_name" to artist,
            "uri" to uri,
            "thumb_uri" to thumbUri,
            "lyric_uri" to lyricUri,
            "duration" to duration,
        )
    }
}

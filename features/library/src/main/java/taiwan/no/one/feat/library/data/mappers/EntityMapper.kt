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

package taiwan.no.one.feat.library.data.mappers

import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity

internal object EntityMapper {
    fun libraryToSimpleTrackEntity(entity: SongEntity) = entity.let {
        SimpleTrackEntity(
            it.id,
            it.title,
            it.artist,
            it.uri,
            it.localUri,
            it.coverUri,
            DEFAULT_STR,
            it.duration,
            it.isFavorite,
            it.hasOwn,
            it.refPath,
        )
    }

    fun simpleToSongEntity(entity: SimpleTrackEntity) = entity.let {
        SongEntity(
            it.id,
            it.name,
            it.artist,
            it.uri,
            it.thumbUri,
            it.duration,
            it.isFavorite,
            refPath = it.refPath,
        )
    }

    fun songToSimpleEntity(entity: SongEntity) = entity.let {
        SimpleTrackEntity(
            it.id,
            it.title,
            it.artist,
            it.uri,
            it.localUri,
            it.coverUri,
            DEFAULT_STR,
            it.duration,
            it.isFavorite,
            it.hasOwn,
            it.refPath,
        )
    }

    fun playlistToSimplePlaylistEntity(entity: PlayListEntity) = entity.let {
        SimplePlaylistEntity(it.id, it.name, it.songIds, it.coverUrl, it.refPath)
    }
}

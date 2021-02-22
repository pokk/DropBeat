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

package taiwan.no.one.feat.explore.data.mappers

import taiwan.no.one.dropbeat.data.entities.SimpleAlbumEntity
import taiwan.no.one.dropbeat.data.entities.SimpleArtistEntity
import taiwan.no.one.dropbeat.data.entities.SimpleArtistEntity.SimpleBioEntity
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.ext.DEFAULT_INT
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.explore.data.entities.remote.AlbumInfoEntity.AlbumWithArtistEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistInfoEntity.ArtistEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackWithStreamableEntity
import taiwan.no.one.feat.explore.domain.usecases.ArtistWithMoreDetailEntity

internal object EntityMapper {
    fun exploreToSimpleTrackEntity(entity: TrackEntity) = entity.let {
        SimpleTrackEntity(
            0,
            it.name.orEmpty(),
            it.artist?.name.orEmpty(),
            it.url.orEmpty(),
            DEFAULT_STR,
            (it.images?.find { it.size == "cover" }?.text ?: entity.images?.get(0)?.text).orEmpty(),
            DEFAULT_STR,
            it.duration?.toInt() ?: 0,
            entity.isFavorite ?: false,
            false,
        )
    }

    fun trackToSimpleTrackEntity(entity: TrackWithStreamableEntity) = entity.let {
        SimpleTrackEntity(
            0,
            it.name.orEmpty(),
            it.artist?.name.orEmpty(),
            it.url.orEmpty(),
            DEFAULT_STR,
            it.images?.firstOrNull()?.text.orEmpty(),
            DEFAULT_STR,
            it.duration?.toInt() ?: DEFAULT_INT,
            isFavorite = false,
            isOwn = false
        )
    }

    fun albumToSimpleAlbumEntity(entity: AlbumWithArtistEntity) = entity.let {
        SimpleAlbumEntity(
            it.mbid.orEmpty(),
            it.title.orEmpty(),
            it.images?.firstOrNull()?.text.orEmpty(),
            it.playCount?.toIntOrNull() ?: DEFAULT_INT,
            it.listeners?.toIntOrNull() ?: DEFAULT_INT,
        )
    }

    fun artistToSimpleTrackEntity(entity: ArtistWithMoreDetailEntity) = entity.let { (artist, album) ->
        SimpleTrackEntity(
            0,
            album?.popularTrackThisWeek?.name.orEmpty(),
            artist.name.orEmpty(),
            album?.popularTrackThisWeek?.url.orEmpty(),
            DEFAULT_STR,
            album?.coverPhotoUrl.orEmpty(),
            DEFAULT_STR,
            album?.popularTrackThisWeek?.duration?.toInt() ?: 0,
            album?.popularTrackThisWeek?.isFavorite ?: false,
            false
        )
    }

    fun artistToSimpleArtistEntity(entity: ArtistEntity) = entity.let {
        SimpleArtistEntity(
            0,
            it.name.orEmpty(),
            DEFAULT_STR,
            it.url.orEmpty(),
            it.stats?.listeners?.toInt() ?: DEFAULT_INT,
            it.stats?.playCount?.toInt() ?: DEFAULT_INT,
            emptyList(),
            emptyList(),
            it.bio?.let { bio ->
                SimpleBioEntity(bio.content.orEmpty(), bio.summary.orEmpty())
            } ?: SimpleBioEntity(DEFAULT_STR, DEFAULT_STR)
        )
    }
}

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

package taiwan.no.one.feat.explore.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.entity.SimpleArtistEntity
import taiwan.no.one.feat.explore.data.entities.local.ArtistWithImageAndBioEntityAndStats
import taiwan.no.one.feat.explore.data.entities.remote.ArtistMoreDetailEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistTopTrackInfoEntity.TracksWithStreamableEntity
import taiwan.no.one.feat.explore.data.entities.remote.CommonLastFmEntity.TopAlbumsEntity
import taiwan.no.one.feat.explore.data.mappers.EntityMapper
import taiwan.no.one.feat.explore.domain.repositories.LastFmExtraRepo
import taiwan.no.one.feat.explore.domain.repositories.LastFmRepo

internal class FetchArtistCompleteInfoOneShotCase(
    private val repository: LastFmRepo,
    private val extraRepository: LastFmExtraRepo,
) : FetchArtistCompleteInfoCase() {
    override suspend fun acquireCase(parameter: FetchArtistCompleteInfoReq?) = parameter.ensure {
        val artistInfo = repository.fetchArtist(name, null)
        val mbid = artistInfo.artist.mbid
        coroutineScope {
            val deferredAlbumOfArtist = async(Dispatchers.IO) { repository.fetchArtistTopAlbum(name, mbid) }
            val deferredTrackOfArtist = async(Dispatchers.IO) { repository.fetchArtistTopTrack(name, mbid) }
            val deferredDetailOfArtist = async(Dispatchers.IO) {
                extraRepository.fetchArtistMoreDetail(name?.replace(" ", "+").orEmpty())
            }
            populating(
                artistInfo,
                deferredAlbumOfArtist.await(),
                deferredTrackOfArtist.await(),
                deferredDetailOfArtist.await()
            )
        }
    }

    private fun populating(
        artistEntity: ArtistWithImageAndBioEntityAndStats,
        topAlbumsEntity: TopAlbumsEntity,
        tracksWithStreamableEntity: TracksWithStreamableEntity,
        extraInfoEntity: ArtistMoreDetailEntity,
    ): SimpleArtistEntity {
        val albums = topAlbumsEntity.albums.map(EntityMapper::albumToSimpleAlbumEntity)
        val tracks = tracksWithStreamableEntity.tracks.map(EntityMapper::trackToSimpleTrackEntity)
        return EntityMapper.artistToSimpleArtistEntity(artistEntity)
            .copy(thumbnail = extraInfoEntity.coverPhotoUrl, topAlbums = albums, topTracks = tracks)
    }

    internal data class Request(
        val name: String? = null,
    ) : RequestValues
}

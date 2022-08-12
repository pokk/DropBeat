/*
 * MIT License
 *
 * Copyright (c) 2022 Jieyi
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

package taiwan.no.one.feat.explore.domain.repositories

import taiwan.no.one.core.domain.repository.Repository
import taiwan.no.one.feat.explore.data.entities.local.ArtistWithImageAndBioEntityAndStats
import taiwan.no.one.feat.explore.data.entities.remote.NetworkAlbumInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkCommonLastFm
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTagInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo

/**
 * This interface will be the similar to [taiwan.no.one.feat.explore.data.contracts.DataStore].
 * Using prefix name (fetch), (add), (update), (delete), (keep)
 */
internal interface LastFmRepo : Repository {
    //region AlbumEntity
    suspend fun fetchAlbum(mbid: String): NetworkAlbumInfo.NetworkAlbum
    //endregion

    //region ArtistEntity
    suspend fun fetchArtist(name: String?, mbid: String?): ArtistWithImageAndBioEntityAndStats

    suspend fun fetchArtistTopAlbum(name: String?, mbid: String?): NetworkCommonLastFm.NetworkTopAlbums

    suspend fun fetchArtistTopTrack(name: String?, mbid: String?): NetworkArtistTopTrackInfo.NetworkTracksWithStreamable

    suspend fun fetchSimilarArtistInfo(mbid: String): NetworkTopArtistInfo.NetworkArtists
    //endregion

    //region TrackEntity
    suspend fun fetchTrack(mbid: String): NetworkTrackInfo.NetworkTrack

    suspend fun fetchSimilarTrackInfo(mbid: String): NetworkTopTrackInfo.NetworkTracks
    //endregion

    //region Chart
    suspend fun fetchChartTopTrack(page: Int, limit: Int): NetworkTopTrackInfo.NetworkTracks

    suspend fun addChartTopTrack(page: Int, limit: Int, entities: List<NetworkTrackInfo.NetworkTrack>)

    suspend fun fetchChartTopArtist(page: Int, limit: Int): NetworkTopArtistInfo.NetworkArtists

    suspend fun fetchChartTopTag(page: Int, limit: Int): NetworkCommonLastFm.NetworkTags
    //endregion

    //region TagEntity
    suspend fun fetchTag(mbid: String): NetworkTagInfo.NetworkTag

    suspend fun fetchTagTopAlbum(mbid: String): NetworkCommonLastFm.NetworkTopAlbums

    suspend fun fetchTagTopArtist(mbid: String): NetworkTopArtistInfo.NetworkArtists

    suspend fun fetchTagTopTrack(tagName: String): NetworkTopTrackInfo.NetworkTracks
    //endregion
}

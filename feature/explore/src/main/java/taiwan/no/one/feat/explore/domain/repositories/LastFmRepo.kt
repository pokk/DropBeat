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

package taiwan.no.one.feat.explore.domain.repositories

import taiwan.no.one.core.domain.repository.Repository
import taiwan.no.one.feat.explore.data.entities.remote.AlbumInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistTopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.CommonLastFmEntity
import taiwan.no.one.feat.explore.data.entities.remote.TagInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity

/**
 * This interface will be the similar to [taiwan.no.one.feat.explore.data.contracts.DataStore].
 * Using prefix name (fetch), (add), (update), (delete), (keep)
 */
internal interface LastFmRepo : Repository {
    //region AlbumEntity
    suspend fun fetchAlbum(mbid: String): AlbumInfoEntity.AlbumEntity
    //endregion

    //region ArtistEntity
    suspend fun fetchArtist(mbid: String): ArtistInfoEntity.ArtistEntity

    suspend fun fetchArtistTopAlbum(mbid: String): CommonLastFmEntity.TopAlbumsEntity

    suspend fun fetchArtistTopTrack(mbid: String): ArtistTopTrackInfoEntity.TracksWithStreamableEntity

    suspend fun fetchSimilarArtistInfo(mbid: String): TopArtistInfoEntity.ArtistsEntity
    //endregion

    //region TrackEntity
    suspend fun fetchTrack(mbid: String): TrackInfoEntity.TrackEntity

    suspend fun fetchSimilarTrackInfo(mbid: String): TopTrackInfoEntity.TracksEntity
    //endregion

    //region Chart
    suspend fun fetchChartTopTrack(page: Int, limit: Int): TopTrackInfoEntity.TracksEntity

    suspend fun fetchChartTopArtist(page: Int, limit: Int): TopArtistInfoEntity.ArtistsEntity

    suspend fun fetchChartTopTag(page: Int, limit: Int): CommonLastFmEntity.TagsEntity
    //endregion

    //region TagEntity
    suspend fun fetchTag(mbid: String): TagInfoEntity.TagEntity

    suspend fun fetchTagTopAlbum(mbid: String): CommonLastFmEntity.TopAlbumsEntity

    suspend fun fetchTagTopArtist(mbid: String): TopArtistInfoEntity.ArtistsEntity

    suspend fun fetchTagTopTrack(mbid: String): TopTrackInfoEntity.TracksEntity
    //endregion
}

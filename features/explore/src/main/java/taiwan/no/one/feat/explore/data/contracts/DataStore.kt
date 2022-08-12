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

package taiwan.no.one.feat.explore.data.contracts

import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.explore.data.entities.local.ArtistWithImageAndBioEntityAndStats
import taiwan.no.one.feat.explore.data.entities.remote.NetworkAlbumInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistMoreDetail
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistPhotos
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistSimilar
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistTopAlbumInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTagInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTagTopArtist
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopAlbumInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopTagInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo.NetworkTrack
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackSimilar

/**
 * This interface will common the all data stores.
 * Using prefix name (get), (create), (modify), (remove), (store)
 */
internal interface DataStore {
    //region Album Data
    suspend fun getAlbumInfo(mbid: String): NetworkAlbumInfo
    //endregion

    //region Artist Data
    suspend fun getArtistInfo(name: String?, mbid: String?): ArtistWithImageAndBioEntityAndStats

    suspend fun getArtistTopAlbum(name: String?, mbid: String?): NetworkArtistTopAlbumInfo

    suspend fun getArtistTopTrack(name: String?, mbid: String?): NetworkArtistTopTrackInfo

    suspend fun getSimilarArtistInfo(mbid: String): NetworkArtistSimilar

    suspend fun getArtistPhotosInfo(artistName: String, page: Int): NetworkArtistPhotos

    suspend fun getArtistMoreInfo(artistName: String): NetworkArtistMoreDetail

    suspend fun createArtistMoreInfo(artistName: String, entity: NetworkArtistMoreDetail): Boolean

    suspend fun createArtist(entity: ArtistWithImageAndBioEntityAndStats): Boolean

    suspend fun removeArtist(entity: ArtistWithImageAndBioEntityAndStats)
    //endregion

    //region Track Data
    suspend fun getTrackInfo(mbid: String): NetworkTrackInfo

    suspend fun getSimilarTrackInfo(mbid: String): NetworkTrackSimilar

    suspend fun getTrackCover(trackUrl: String, trackEntity: NetworkTrack): NetworkTrack

    suspend fun getTrackCover(trackUrl: String, simpleTrackEntity: SimpleTrackEntity): SimpleTrackEntity
    //endregion

    //region Chart
    suspend fun getChartTopTrack(page: Int, limit: Int): NetworkTopTrackInfo

    suspend fun createChartTopTrack(page: Int, limit: Int, entity: NetworkTopTrackInfo): Boolean

    suspend fun getChartTopArtist(page: Int, limit: Int): NetworkTopArtistInfo

    suspend fun createChartTopArtist(page: Int, limit: Int, entity: NetworkTopArtistInfo): Boolean

    suspend fun getChartTopTag(page: Int, limit: Int): NetworkTopTagInfo
    //endregion

    //region Tag Data
    suspend fun getTagInfo(mbid: String): NetworkTagInfo

    suspend fun getTagTopAlbum(mbid: String): NetworkTopAlbumInfo

    suspend fun getTagTopArtist(mbid: String): NetworkTagTopArtist

    suspend fun getTagTopTrack(tagName: String): NetworkTopTrackInfo
    //endregion
}

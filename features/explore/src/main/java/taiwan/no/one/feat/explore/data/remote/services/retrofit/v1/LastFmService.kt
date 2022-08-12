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

package taiwan.no.one.feat.explore.data.remote.services.retrofit.v1

import retrofit2.http.GET
import retrofit2.http.QueryMap
import taiwan.no.one.feat.explore.data.entities.remote.NetworkAlbumInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistInfo
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
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackSimilar
import taiwan.no.one.feat.explore.data.remote.configs.LastFmConfig

/**
 * Thru [retrofit2.Retrofit] we can just define the interfaces which we want to access for.
 * Using prefix name (retrieve), (insert), (replace), (release)
 */
internal interface LastFmService {
    //region AlbumEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveAlbumInfo(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkAlbumInfo
    //endregion

    //region ArtistEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveArtistInfo(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkArtistInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveArtistTopAlbum(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkArtistTopAlbumInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveArtistTopTrack(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkArtistTopTrackInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveSimilarArtistInfo(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkArtistSimilar
    //endregion

    //region TrackEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTrackInfo(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTrackInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveSimilarTrackInfo(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTrackSimilar
    //endregion

    //region Chart
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveChartTopTrack(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTopTrackInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveChartTopArtist(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTopArtistInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveChartTopTag(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTopTagInfo
    //endregion

    //region TagEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagInfo(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTagInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagTopAlbum(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTopAlbumInfo

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagTopArtist(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTagTopArtist

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagTopTrack(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkTopTrackInfo
    //endregion
}

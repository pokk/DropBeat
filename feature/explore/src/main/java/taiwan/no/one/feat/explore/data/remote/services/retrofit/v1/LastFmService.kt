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

package taiwan.no.one.feat.explore.data.remote.services.retrofit.v1

import retrofit2.http.GET
import retrofit2.http.QueryMap
import taiwan.no.one.feat.explore.data.entities.remote.AlbumInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistSimilarEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistTopAlbumInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistTopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TagInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TagTopArtistEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopAlbumInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTagInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackSimilarEntity
import taiwan.no.one.feat.explore.data.remote.configs.LastFmConfig

/**
 * Thru [retrofit2.Retrofit] we can just define the interfaces which we want to access for.
 * Using prefix name (retrieve), (insert), (replace), (release)
 */
internal interface LastFmService {
    //region AlbumEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveAlbumInfo(@QueryMap queries: Map<String, String>): AlbumInfoEntity
    //endregion

    //region ArtistEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveArtistInfo(@QueryMap queries: Map<String, String>): ArtistInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveArtistTopAlbum(@QueryMap queries: Map<String, String>): ArtistTopAlbumInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveArtistTopTrack(@QueryMap queries: Map<String, String>): ArtistTopTrackInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveSimilarArtistInfo(@QueryMap queries: Map<String, String>): ArtistSimilarEntity
    //endregion

    //region TrackEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTrackInfo(@QueryMap queries: Map<String, String>): TrackInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveSimilarTrackInfo(@QueryMap queries: Map<String, String>): TrackSimilarEntity
    //endregion

    //region Chart
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveChartTopTrack(@QueryMap queries: Map<String, String>): TopTrackInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveChartTopArtist(@QueryMap queries: Map<String, String>): TopArtistInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveChartTopTag(@QueryMap queries: Map<String, String>): TopTagInfoEntity
    //endregion

    //region TagEntity
    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagInfo(@QueryMap queries: Map<String, String>): TagInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagTopAlbum(@QueryMap queries: Map<String, String>): TopAlbumInfoEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagTopArtist(@QueryMap queries: Map<String, String>): TagTopArtistEntity

    @GET(LastFmConfig.API_REQUEST)
    suspend fun retrieveTagTopTrack(@QueryMap queries: Map<String, String>): TopTrackInfoEntity
    //endregion
}

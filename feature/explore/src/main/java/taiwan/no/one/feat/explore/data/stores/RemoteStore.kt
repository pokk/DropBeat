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

package taiwan.no.one.feat.explore.data.stores

import android.content.Context
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.entities.Constants
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmExtraService
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmService

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore(
    private val lastFmService: LastFmService,
    private val lastFmExtraService: LastFmExtraService,
    private val context: Context
) : DataStore {
    private val lastFmToken by lazy { context.getString(R.string.lastfm_api_key) }

    override suspend fun getAlbumInfo(mbid: String) =
        lastFmService.retrieveAlbumInfo(infoQuery(Constants.LASTFM_PARAM_ALBUM_GET_INFO, mbid))

    override suspend fun getArtistInfo(mbid: String) =
        lastFmService.retrieveArtistInfo(infoQuery(Constants.LASTFM_PARAM_ARTIST_GET_INFO, mbid))

    override suspend fun getArtistTopAlbum(mbid: String) =
        lastFmService.retrieveArtistTopAlbum(infoQuery(Constants.LASTFM_PARAM_ARTIST_GET_TOP_ALBUMS, mbid))

    override suspend fun getArtistTopTrack(mbid: String) =
        lastFmService.retrieveArtistTopTrack(infoQuery(Constants.LASTFM_PARAM_ARTIST_GET_TOP_TRACKS, mbid))

    override suspend fun getSimilarArtistInfo(mbid: String) =
        lastFmService.retrieveSimilarArtistInfo(infoQuery(Constants.LASTFM_PARAM_ARTIST_GET_SIMILAR, mbid))

    override suspend fun getArtistPhotosInfo(artistName: String, page: Int) =
        lastFmExtraService.retrieveArtistPhotosInfo(artistName, page)

    override suspend fun getArtistMoreInfo(artistName: String) =
        lastFmExtraService.retrieveArtistMoreDetail(artistName)

    override suspend fun getTrackInfo(mbid: String) =
        lastFmService.retrieveTrackInfo(infoQuery(Constants.LASTFM_PARAM_TRACK_INFO, mbid))

    override suspend fun getSimilarTrackInfo(mbid: String) =
        lastFmService.retrieveSimilarTrackInfo(infoQuery(Constants.LASTFM_PARAM_TRACK_GET_SIMILAR, mbid))

    override suspend fun getChartTopTrack(page: Int, limit: Int) =
        lastFmService.retrieveChartTopTrack(chartQuery(Constants.LASTFM_PARAM_CHART_GET_TOP_TRACKS, page, limit))

    override suspend fun getChartTopArtist(page: Int, limit: Int) =
        lastFmService.retrieveChartTopArtist(chartQuery(Constants.LASTFM_PARAM_CHART_GET_TOP_ARTISTS, page, limit))

    override suspend fun getChartTopTag(page: Int, limit: Int) =
        lastFmService.retrieveChartTopTag(chartQuery(Constants.LASTFM_PARAM_CHART_GET_TOP_TAGS, page, limit))

    override suspend fun getTagInfo(mbid: String) =
        lastFmService.retrieveTagInfo(infoQuery(Constants.LASTFM_PARAM_TAG_GET_INFO, mbid))

    override suspend fun getTagTopAlbum(mbid: String) =
        lastFmService.retrieveTagTopAlbum(infoQuery(Constants.LASTFM_PARAM_TAG_GET_TOP_ALBUMS, mbid))

    override suspend fun getTagTopArtist(mbid: String) =
        lastFmService.retrieveTagTopArtist(infoQuery(Constants.LASTFM_PARAM_TAG_GET_TOP_ARTISTS, mbid))

    override suspend fun getTagTopTrack(mbid: String) =
        lastFmService.retrieveTagTopTrack(infoQuery(Constants.LASTFM_PARAM_TAG_GET_TOP_TRACKS, mbid))

    private fun combineLastFmQuery(method: String, block: Map<String, String>.() -> Unit) = hashMapOf(
        Constants.LASTFM_QUERY_TOKEN to lastFmToken,
        Constants.LASTFM_QUERY_METHOD to method,
        Constants.LASTFM_QUERY_FORMAT to "json",
        Constants.LASTFM_QUERY_LANGUAGE to "en", // TODO(jieyiwu): 6/9/20 We can get from system.
    ).apply(block)

    private fun chartQuery(method: String, page: Int, limit: Int) = combineLastFmQuery(method) {
        Constants.LASTFM_QUERY_PAGE to page
        Constants.LASTFM_QUERY_LIMIT to limit
    }

    private fun infoQuery(method: String, mbid: String) = combineLastFmQuery(method) {
        Constants.LASTFM_QUERY_MBID to mbid
    }
}

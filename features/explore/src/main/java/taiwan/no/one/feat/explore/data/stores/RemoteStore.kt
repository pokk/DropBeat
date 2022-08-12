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

package taiwan.no.one.feat.explore.data.stores

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.data.DataModules
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.contracts.Mapper
import taiwan.no.one.feat.explore.data.entities.Constant
import taiwan.no.one.feat.explore.data.entities.local.ArtistEntity
import taiwan.no.one.feat.explore.data.entities.local.ArtistWithImageAndBioEntityAndStats
import taiwan.no.one.feat.explore.data.entities.local.BioEntity
import taiwan.no.one.feat.explore.data.entities.local.ImageEntity
import taiwan.no.one.feat.explore.data.entities.local.ImgQuality
import taiwan.no.one.feat.explore.data.entities.local.StatsEntity
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistMoreDetail
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo.NetworkTrack
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmExtraService
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmService

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore(
    private val lastFmService: LastFmService,
    private val lastFmExtraService: LastFmExtraService,
    private val context: Context,
) : DataStore, DIAware {
    override val di by DI.lazy { import(DataModules.mappersProvide()) }
    private val lastFmToken by lazy { context.getString(R.string.lastfm_api_key) }

    override suspend fun getAlbumInfo(mbid: String) =
        lastFmService.retrieveAlbumInfo(infoQuery(Constant.LASTFM_PARAM_ALBUM_GET_INFO, mbid))

    override suspend fun getArtistInfo(name: String?, mbid: String?): ArtistWithImageAndBioEntityAndStats {
        val artistMapper by instance<Mapper<NetworkArtistInfo.NetworkArtist, ArtistEntity>>()
        val bioMapper by instance<Mapper<NetworkArtistInfo.NetworkStats, StatsEntity>>()
        val stateMapper by instance<Mapper<NetworkArtistInfo.NetworkBio, BioEntity>>()
        val artist = lastFmService.retrieveArtistInfo(
            combineArtistName(Constant.LASTFM_PARAM_ARTIST_GET_INFO, name, mbid)
        ).artist ?: throw IllegalArgumentException()
        val moreInfo = getArtistMoreInfo(artist.name?.replace(" ", "+").orEmpty())
        return ArtistWithImageAndBioEntityAndStats(
            artistMapper.dtoToPo(artist),
            listOf(ImageEntity(0L, ImgQuality.COVER, moreInfo.coverPhotoUrl)),
            stateMapper.dtoToPo(artist.bio ?: throw IllegalArgumentException()),
            bioMapper.dtoToPo(artist.stats ?: throw IllegalArgumentException())
        )
    }

    override suspend fun getArtistTopAlbum(name: String?, mbid: String?) =
        lastFmService.retrieveArtistTopAlbum(combineArtistName(Constant.LASTFM_PARAM_ARTIST_GET_TOP_ALBUMS, name, mbid))

    override suspend fun getArtistTopTrack(name: String?, mbid: String?) =
        lastFmService.retrieveArtistTopTrack(combineArtistName(Constant.LASTFM_PARAM_ARTIST_GET_TOP_TRACKS, name, mbid))

    override suspend fun getSimilarArtistInfo(mbid: String) =
        lastFmService.retrieveSimilarArtistInfo(infoQuery(Constant.LASTFM_PARAM_ARTIST_GET_SIMILAR, mbid))

    override suspend fun getArtistPhotosInfo(artistName: String, page: Int) =
        lastFmExtraService.retrieveArtistPhotosInfo(artistName, page)

    override suspend fun getArtistMoreInfo(artistName: String) = lastFmExtraService.retrieveArtistMoreDetail(artistName)

    override suspend fun createArtistMoreInfo(artistName: String, entity: NetworkArtistMoreDetail) =
        UnsupportedOperation()

    override suspend fun createArtist(entity: ArtistWithImageAndBioEntityAndStats) = UnsupportedOperation()

    override suspend fun removeArtist(entity: ArtistWithImageAndBioEntityAndStats) = UnsupportedOperation()

    override suspend fun getTrackInfo(mbid: String) =
        lastFmService.retrieveTrackInfo(infoQuery(Constant.LASTFM_PARAM_TRACK_INFO, mbid))

    override suspend fun getSimilarTrackInfo(mbid: String) =
        lastFmService.retrieveSimilarTrackInfo(infoQuery(Constant.LASTFM_PARAM_TRACK_GET_SIMILAR, mbid))

    override suspend fun getTrackCover(trackUrl: String, trackEntity: NetworkTrack) =
        lastFmExtraService.retrieveTrackCover(trackUrl, trackEntity)

    override suspend fun getTrackCover(trackUrl: String, simpleTrackEntity: SimpleTrackEntity) =
        lastFmExtraService.retrieveTrackCover(trackUrl, simpleTrackEntity)

    override suspend fun getChartTopTrack(page: Int, limit: Int) =
        lastFmService.retrieveChartTopTrack(chartQuery(Constant.LASTFM_PARAM_CHART_GET_TOP_TRACKS, page, limit))

    override suspend fun createChartTopTrack(page: Int, limit: Int, entity: NetworkTopTrackInfo) =
        UnsupportedOperation()

    override suspend fun getChartTopArtist(page: Int, limit: Int) =
        lastFmService.retrieveChartTopArtist(chartQuery(Constant.LASTFM_PARAM_CHART_GET_TOP_ARTISTS, page, limit))

    override suspend fun createChartTopArtist(page: Int, limit: Int, entity: NetworkTopArtistInfo) =
        UnsupportedOperation()

    override suspend fun getChartTopTag(page: Int, limit: Int) =
        lastFmService.retrieveChartTopTag(chartQuery(Constant.LASTFM_PARAM_CHART_GET_TOP_TAGS, page, limit))

    override suspend fun getTagInfo(mbid: String) =
        lastFmService.retrieveTagInfo(infoQuery(Constant.LASTFM_PARAM_TAG_GET_INFO, mbid))

    override suspend fun getTagTopAlbum(mbid: String) =
        lastFmService.retrieveTagTopAlbum(infoQuery(Constant.LASTFM_PARAM_TAG_GET_TOP_ALBUMS, mbid))

    override suspend fun getTagTopArtist(mbid: String) =
        lastFmService.retrieveTagTopArtist(infoQuery(Constant.LASTFM_PARAM_TAG_GET_TOP_ARTISTS, mbid))

    override suspend fun getTagTopTrack(tagName: String) = lastFmService.retrieveTagTopTrack(
        combineLastFmQuery(Constant.LASTFM_PARAM_TAG_GET_TOP_TRACKS) {
            put("tag", tagName)
            put(Constant.LASTFM_QUERY_LIMIT, "20")
        }
    )

    private fun combineLastFmQuery(method: String, block: MutableMap<String, String>.() -> Unit) = hashMapOf(
        Constant.LASTFM_QUERY_TOKEN to lastFmToken,
        Constant.LASTFM_QUERY_METHOD to method,
        Constant.LASTFM_QUERY_FORMAT to "json",
        Constant.LASTFM_QUERY_LANGUAGE to "en", // TODO(jieyiwu): 6/9/20 We can get from system.
    ).apply(block)

    private fun combineArtistName(method: String, name: String?, mbid: String?) = combineLastFmQuery(method) {
        name.takeUnless(String?::isNullOrBlank)?.let { put(Constant.LASTFM_QUERY_ARTIST_NAME, it) }
        mbid.takeUnless(String?::isNullOrBlank)?.let { put(Constant.LASTFM_QUERY_MBID, it) }
    }

    private fun chartQuery(method: String, page: Int, limit: Int) = combineLastFmQuery(method) {
        put(Constant.LASTFM_QUERY_PAGE, page.toString())
        put(Constant.LASTFM_QUERY_LIMIT, limit.toString())
    }

    private fun infoQuery(method: String, mbid: String) = combineLastFmQuery(method) {
        put(Constant.LASTFM_QUERY_MBID, mbid)
    }
}

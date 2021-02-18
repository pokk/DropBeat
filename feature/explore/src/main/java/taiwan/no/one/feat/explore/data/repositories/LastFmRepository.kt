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

package taiwan.no.one.feat.explore.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import taiwan.no.one.core.data.repostory.cache.LayerCaching
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.core.domain.repository.Repository
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.entities.remote.TopArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity.TracksEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity
import taiwan.no.one.feat.explore.data.stores.LocalStore.Constant.TYPE_CHART_TOP_ARTIST
import taiwan.no.one.feat.explore.data.stores.LocalStore.Constant.TYPE_CHART_TOP_TRACK
import taiwan.no.one.feat.explore.domain.repositories.LastFmRepo
import java.util.Date

internal class LastFmRepository(
    private val local: DataStore,
    private val remote: DataStore,
    private val sp: SharedPreferences,
) : LastFmRepo {
    override suspend fun fetchAlbum(mbid: String) = remote.getAlbumInfo(mbid).album ?: throw Exception()

    override suspend fun fetchArtist(name: String?, mbid: String?) =
        remote.getArtistInfo(name, mbid).artist ?: throw Exception()

    override suspend fun fetchArtistTopAlbum(mbid: String) = remote.getArtistTopAlbum(mbid).topAlbums

    override suspend fun fetchArtistTopTrack(mbid: String) = remote.getArtistTopTrack(mbid).topTracks

    override suspend fun fetchSimilarArtistInfo(mbid: String) = remote.getSimilarArtistInfo(mbid).similarArtist

    override suspend fun fetchTrack(mbid: String) = remote.getTrackInfo(mbid).track ?: throw Exception()

    override suspend fun fetchSimilarTrackInfo(mbid: String) = remote.getSimilarTrackInfo(mbid).similarTracks

    override suspend fun fetchChartTopTrack(page: Int, limit: Int) = object : LayerCaching<TopTrackInfoEntity>() {
        override var timestamp
            get() = sp.getLong(convertToKey(page, limit, TYPE_CHART_TOP_TRACK), 0L)
            set(value) {
                sp.edit { putLong(convertToKey(page, limit, TYPE_CHART_TOP_TRACK), value) }
            }

        override suspend fun saveCallResult(data: TopTrackInfoEntity) {
            local.createChartTopTrack(page, limit, data)
        }

        override suspend fun shouldFetch(data: TopTrackInfoEntity) =
            Date().time - timestamp > Repository.EXPIRED_DURATION

        override suspend fun loadFromLocal() = local.getChartTopTrack(page, limit)

        override suspend fun createCall() = remote.getChartTopTrack(page, limit)
    }.value().track

    override suspend fun addChartTopTrack(page: Int, limit: Int, entities: List<TrackEntity>) {
        local.createChartTopTrack(page, limit, TopTrackInfoEntity(TracksEntity(entities, null)))
    }

    override suspend fun fetchChartTopArtist(page: Int, limit: Int) = object : LayerCaching<TopArtistInfoEntity>() {
        override var timestamp
            get() = sp.getLong(convertToKey(page, limit, TYPE_CHART_TOP_ARTIST), 0L)
            set(value) {
                sp.edit { putLong(convertToKey(page, limit, TYPE_CHART_TOP_ARTIST), value) }
            }

        override suspend fun saveCallResult(data: TopArtistInfoEntity) {
            local.createChartTopArtist(page, limit, data)
        }

        override suspend fun shouldFetch(data: TopArtistInfoEntity) =
            Date().time - timestamp > Repository.EXPIRED_DURATION

        override suspend fun loadFromLocal() = local.getChartTopArtist(page, limit)

        override suspend fun createCall() = remote.getChartTopArtist(page, limit)
    }.value().artists

    override suspend fun fetchChartTopTag(page: Int, limit: Int) = remote.getChartTopTag(page, limit).tag

    override suspend fun fetchTag(mbid: String) = remote.getTagInfo(mbid).tag

    override suspend fun fetchTagTopAlbum(mbid: String) = remote.getTagTopAlbum(mbid).albums

    override suspend fun fetchTagTopArtist(mbid: String) = remote.getTagTopArtist(mbid).topArtists

    override suspend fun fetchTagTopTrack(tagName: String) = remote.getTagTopTrack(tagName).track
}

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

package taiwan.no.one.feat.explore.data.stores

import taiwan.no.one.core.data.repostory.cache.local.DiskCache
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.core.data.store.tryWrapper
import taiwan.no.one.core.exceptions.NotFoundException
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.entities.remote.ArtistMoreDetailEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val mmkvCache: DiskCache,
) : DataStore {
    companion object Constant {
        const val TYPE_CHART_TOP_TRACK = "top_track"
        const val TYPE_CHART_TOP_ARTIST = "top_artist"
        const val TYPE_TAG_TRACK = "tag_track"
    }

    override suspend fun getAlbumInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistInfo(name: String?, mbid: String?) = UnsupportedOperation()

    override suspend fun getArtistTopAlbum(name: String?, mbid: String?) = UnsupportedOperation()

    override suspend fun getArtistTopTrack(name: String?, mbid: String?) = UnsupportedOperation()

    override suspend fun getSimilarArtistInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistPhotosInfo(artistName: String, page: Int) = UnsupportedOperation()

    override suspend fun getArtistMoreInfo(artistName: String) =
        mmkvCache.get(convertToKey(artistName), ArtistMoreDetailEntity::class.java)?.second ?: throw NotFoundException()

    override suspend fun createArtistMoreInfo(artistName: String, entity: ArtistMoreDetailEntity) = tryWrapper {
        mmkvCache.put(convertToKey(artistName), entity)
    }

    override suspend fun getTrackInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getSimilarTrackInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getTrackCover(trackUrl: String, trackEntity: TrackEntity) = UnsupportedOperation()

    override suspend fun getTrackCover(trackUrl: String, simpleTrackEntity: SimpleTrackEntity) = UnsupportedOperation()

    override suspend fun getChartTopTrack(page: Int, limit: Int) =
        mmkvCache.get(
            convertToKey(page, limit, TYPE_CHART_TOP_TRACK),
            TopTrackInfoEntity::class.java
        )?.second ?: throw NotFoundException()

    override suspend fun createChartTopTrack(page: Int, limit: Int, entity: TopTrackInfoEntity) = tryWrapper {
        mmkvCache.put(convertToKey(page, limit, TYPE_CHART_TOP_TRACK), entity)
    }

    override suspend fun getChartTopArtist(page: Int, limit: Int) =
        mmkvCache.get(
            convertToKey(page, limit, TYPE_CHART_TOP_ARTIST),
            TopArtistInfoEntity::class.java
        )?.second ?: throw NotFoundException()

    override suspend fun createChartTopArtist(page: Int, limit: Int, entity: TopArtistInfoEntity) = tryWrapper {
        mmkvCache.put(convertToKey(page, limit, TYPE_CHART_TOP_ARTIST), entity)
    }

    override suspend fun getChartTopTag(page: Int, limit: Int) = UnsupportedOperation()

    override suspend fun getTagInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopAlbum(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopArtist(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopTrack(mbid: String) = UnsupportedOperation()

    override suspend fun createTagTopTrack(tagName: String) = true
}

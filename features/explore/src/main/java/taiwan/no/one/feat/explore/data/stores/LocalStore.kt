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

import taiwan.no.one.core.data.repostory.cache.local.DiskCache
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.core.data.store.tryWrapper
import taiwan.no.one.core.exceptions.NotFoundException
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.entities.local.ArtistWithImageAndBioEntityAndStats
import taiwan.no.one.feat.explore.data.entities.local.ImageEntity
import taiwan.no.one.feat.explore.data.entities.local.enums.ImgQuality
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistMoreDetail
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo.NetworkTrack
import taiwan.no.one.feat.explore.data.local.services.database.v1.ArtistDao
import taiwan.no.one.feat.explore.data.local.services.database.v1.BioDao
import taiwan.no.one.feat.explore.data.local.services.database.v1.ImageDao
import taiwan.no.one.feat.explore.data.local.services.database.v1.StatsDao

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val mmkvCache: DiskCache,
    private val artistDao: ArtistDao,
    private val imageDao: ImageDao,
    private val bioDao: BioDao,
    private val statsDao: StatsDao,
) : DataStore {
    companion object Constant {
        const val TYPE_CHART_TOP_TRACK = "top_track"
        const val TYPE_CHART_TOP_ARTIST = "top_artist"
    }

    override suspend fun getAlbumInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistInfo(name: String?, mbid: String?) = artistDao.getArtistBy(name.orEmpty())

    override suspend fun getArtistTopAlbum(name: String?, mbid: String?) = UnsupportedOperation()

    override suspend fun getArtistTopTrack(name: String?, mbid: String?) = UnsupportedOperation()

    override suspend fun getSimilarArtistInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistPhotosInfo(artistName: String, page: Int) = UnsupportedOperation()

    override suspend fun getArtistMoreInfo(artistName: String) =
        mmkvCache.get(
            convertToKey(artistName),
            NetworkArtistMoreDetail::class.java
        )?.second ?: throw NotFoundException()

    override suspend fun createArtistMoreInfo(artistName: String, entity: NetworkArtistMoreDetail) = tryWrapper {
        artistDao.getArtistBy(artistName.replace("+", " ")).artist.artistId.also { id ->
            imageDao.insert(ImageEntity(0L, ImgQuality.HIGH, entity.coverPhotoUrl, id))
        }
    }

    override suspend fun createArtist(entity: ArtistWithImageAndBioEntityAndStats): Boolean {
        artistDao.insert(entity.artist).also { id ->
            entity.images.forEach { imageDao.insert(it.copy(artistId = id)) }
            bioDao.insert(entity.bio.copy(artistId = id))
            statsDao.insert(entity.stats.copy(artistId = id))
        }
        return true
    }

    override suspend fun removeArtist(entity: ArtistWithImageAndBioEntityAndStats) {
        val artistId = entity.artist.artistId.toInt()
        artistDao.delete(entity.artist)
        imageDao.deleteBy(artistId)
        bioDao.deleteBy(artistId)
        statsDao.deleteBy(artistId)
    }

    override suspend fun getTrackInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getSimilarTrackInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getTrackCover(trackUrl: String, trackEntity: NetworkTrack) = UnsupportedOperation()

    override suspend fun getTrackCover(trackUrl: String, simpleTrackEntity: SimpleTrackEntity) = UnsupportedOperation()

    override suspend fun getChartTopTrack(page: Int, limit: Int) =
        mmkvCache.get(
            convertToKey(page, limit, TYPE_CHART_TOP_TRACK),
            NetworkTopTrackInfo::class.java
        )?.second ?: throw NotFoundException()

    override suspend fun createChartTopTrack(page: Int, limit: Int, entity: NetworkTopTrackInfo) = tryWrapper {
        mmkvCache.put(convertToKey(page, limit, TYPE_CHART_TOP_TRACK), entity, NetworkTopTrackInfo::class.java)
    }

    override suspend fun getChartTopArtist(page: Int, limit: Int) =
        mmkvCache.get(
            convertToKey(page, limit, TYPE_CHART_TOP_ARTIST),
            NetworkTopArtistInfo::class.java
        )?.second ?: throw NotFoundException()

    override suspend fun createChartTopArtist(page: Int, limit: Int, entity: NetworkTopArtistInfo) = tryWrapper {
        mmkvCache.put(convertToKey(page, limit, TYPE_CHART_TOP_ARTIST), entity, NetworkTopArtistInfo::class.java)
    }

    override suspend fun getChartTopTag(page: Int, limit: Int) = UnsupportedOperation()

    override suspend fun getTagInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopAlbum(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopArtist(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopTrack(mbid: String) = UnsupportedOperation()
}

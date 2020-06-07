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

import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmExtraService
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmService

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore(
    private val lastFmService: LastFmService,
    private val lastFmExtraService: LastFmExtraService
) : DataStore {
    override suspend fun getAlbumInfo() = lastFmService.retrieveAlbumInfo(TODO())

    override suspend fun getArtistInfo() = lastFmService.retrieveArtistInfo(TODO())

    override suspend fun getArtistTopAlbum() = lastFmService.retrieveArtistTopAlbum(TODO())

    override suspend fun getArtistTopTrack() = lastFmService.retrieveArtistTopTrack(TODO())

    override suspend fun getSimilarArtistInfo() = lastFmService.retrieveSimilarArtistInfo(TODO())

    override suspend fun getArtistPhotosInfo() = lastFmExtraService.retrieveArtistPhotosInfo(TODO(), TODO())

    override suspend fun getTrackInfo() = lastFmService.retrieveTrackInfo(TODO())

    override suspend fun getSimilarTrackInfo() = lastFmService.retrieveSimilarTrackInfo(TODO())

    override suspend fun getChartTopTrack() = lastFmService.retrieveChartTopTrack(TODO())

    override suspend fun getChartTopArtist() = lastFmService.retrieveChartTopArtist(TODO())

    override suspend fun getChartTopTag() = lastFmService.retrieveChartTopTag(TODO())

    override suspend fun getTagInfo() = lastFmService.retrieveTagInfo(TODO())

    override suspend fun getTagTopAlbum() = lastFmService.retrieveTagTopAlbum(TODO())

    override suspend fun getTagTopArtist() = lastFmService.retrieveTagTopArtist(TODO())

    override suspend fun getTagTopTrack() = lastFmService.retrieveTagTopTrack(TODO())
}

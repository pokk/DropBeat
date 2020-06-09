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

import com.tencent.mmkv.MMKV
import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.explore.data.contracts.DataStore

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val mmkv: MMKV,
) : DataStore {
    override suspend fun getAlbumInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistTopAlbum(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistTopTrack(mbid: String) = UnsupportedOperation()

    override suspend fun getSimilarArtistInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getArtistPhotosInfo(artistName: String, page: Int) = UnsupportedOperation()

    override suspend fun getTrackInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getSimilarTrackInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getChartTopTrack(page: Int, limit: Int) = UnsupportedOperation()

    override suspend fun getChartTopArtist(page: Int, limit: Int) = UnsupportedOperation()

    override suspend fun getChartTopTag(page: Int, limit: Int) = UnsupportedOperation()

    override suspend fun getTagInfo(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopAlbum(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopArtist(mbid: String) = UnsupportedOperation()

    override suspend fun getTagTopTrack(mbid: String) = UnsupportedOperation()
}

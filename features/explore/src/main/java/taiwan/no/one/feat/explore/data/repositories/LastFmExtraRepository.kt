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

package taiwan.no.one.feat.explore.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.datetime.Instant
import taiwan.no.one.core.data.repostory.cache.LayerCaching
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ext.extensions.now
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistMoreDetail
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo.NetworkTrack
import taiwan.no.one.feat.explore.domain.repositories.LastFmExtraRepo

internal class LastFmExtraRepository(
    private val local: DataStore,
    private val remote: DataStore,
    private val sp: SharedPreferences,
) : LastFmExtraRepo {
    override suspend fun fetchArtistPhotoInfo(artistName: String, page: Int) =
        remote.getArtistPhotosInfo(artistName, page).photos

    override suspend fun fetchArtistMoreDetail(artistName: String) = object : LayerCaching<NetworkArtistMoreDetail>() {
        override var timestamp
            get() = sp.getLong(convertToKey(artistName), 0L)
            set(value) {
                sp.edit { putLong(convertToKey(artistName), value) }
            }

        override suspend fun saveCallResult(data: NetworkArtistMoreDetail) {
            local.createArtistMoreInfo(artistName, data)
        }

        override suspend fun shouldFetch(data: NetworkArtistMoreDetail) =
            Instant.fromEpochMilliseconds(timestamp) + expired > now()

        override suspend fun loadFromLocal() = local.getArtistMoreInfo(artistName)

        override suspend fun createCall() = remote.getArtistMoreInfo(artistName)
    }.value()

    override suspend fun fetchTrackCover(trackUrl: String, trackEntity: NetworkTrack) =
        remote.getTrackCover(trackUrl, trackEntity)

    override suspend fun fetchTrackCover(trackUrl: String, simpleTrackEntity: SimpleTrackEntity) =
        remote.getTrackCover(trackUrl, simpleTrackEntity)
}

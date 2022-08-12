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

package taiwan.no.one.feat.search.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.datetime.Instant
import taiwan.no.one.core.data.repostory.cache.LayerCaching
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.ext.extensions.now
import taiwan.no.one.feat.search.data.contracts.DataStore
import taiwan.no.one.feat.search.data.entities.remote.NetworkMusicInfo
import taiwan.no.one.feat.search.domain.repositories.SearchMusicRepo

internal class SearchMusicRepository(
    private val remote: DataStore,
    private val local: DataStore,
    private val sp: SharedPreferences,
) : SearchMusicRepo {
    override suspend fun fetchMusic(keyword: String, page: Int) = object : LayerCaching<NetworkMusicInfo>() {
        override var timestamp
            get() = sp.getLong(convertToKey(keyword, page), 0L)
            set(value) {
                sp.edit { putLong(convertToKey(keyword, page), value) }
            }

        override suspend fun saveCallResult(data: NetworkMusicInfo) {
            local.createMusic(keyword, page, data)
        }

        override suspend fun shouldFetch(data: NetworkMusicInfo) =
            Instant.fromEpochMilliseconds(timestamp) + expired >= now()

        override suspend fun loadFromLocal() = local.getMusic(keyword, page)

        override suspend fun createCall() = remote.getMusic(keyword, page)
    }.value().entity.items
}

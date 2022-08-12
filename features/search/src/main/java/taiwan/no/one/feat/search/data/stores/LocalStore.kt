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

package taiwan.no.one.feat.search.data.stores

import kotlinx.coroutines.flow.distinctUntilChanged
import taiwan.no.one.core.data.repostory.cache.local.DiskCache
import taiwan.no.one.core.data.repostory.cache.local.LocalCaching
import taiwan.no.one.core.data.repostory.cache.local.MemoryCache
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.core.data.store.tryWrapper
import taiwan.no.one.feat.search.data.contracts.DataStore
import taiwan.no.one.feat.search.data.entities.local.SearchHistoryEntity
import taiwan.no.one.feat.search.data.entities.remote.NetworkMusicInfo
import taiwan.no.one.feat.search.data.local.services.database.v1.SearchHistoryDao

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val searchHistoryDao: SearchHistoryDao,
    private val mmkvCache: DiskCache,
    private val lruMemoryCache: MemoryCache,
) : DataStore {
    override suspend fun getMusic(keyword: String, page: Int) =
        object : LocalCaching<NetworkMusicInfo>(lruMemoryCache, mmkvCache) {
            override val key get() = convertToKey(keyword, page)
        }.value()

    override suspend fun createMusic(keyword: String, page: Int, music: NetworkMusicInfo): Boolean {
        mmkvCache.put(convertToKey(keyword, page), music, NetworkMusicInfo::class.java)
        lruMemoryCache.put(convertToKey(keyword, page), music, NetworkMusicInfo::class.java)
        return true
    }

    override fun getSearchHistories(count: Int) = searchHistoryDao.getHistories(count).distinctUntilChanged()

    override suspend fun createOrModifySearchHistory(keyword: String) = tryWrapper {
        searchHistoryDao.insertBy(keyword)
    }

    override suspend fun removeSearchHistory(
        keyword: String?,
        entity: SearchHistoryEntity?,
    ) = tryWrapper {
        if (entity != null) {
            searchHistoryDao.delete(entity)
        }
        else {
            searchHistoryDao.deleteBy(keyword.orEmpty())
        }
    }
}

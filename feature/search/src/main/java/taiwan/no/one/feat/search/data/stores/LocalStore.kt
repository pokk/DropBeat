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

package taiwan.no.one.feat.search.data.stores

import kotlinx.coroutines.flow.distinctUntilChanged
import taiwan.no.one.feat.search.BuildConfig
import taiwan.no.one.feat.search.data.contracts.DataStore
import taiwan.no.one.feat.search.data.entities.local.SearchHistoryEntity
import taiwan.no.one.feat.search.data.local.services.database.v1.SearchHistoryDao

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val searchHistoryDao: SearchHistoryDao,
) : DataStore {
    override suspend fun getMusic(keyword: String, page: Int) = TODO()

    override suspend fun createOrModifySearchHistory(keyword: String) = tryWrapper {
        searchHistoryDao.insertBy(keyword)
    }

    override fun getSearchHistories(count: Int) = searchHistoryDao.retrieveHistories(count).distinctUntilChanged()

    override suspend fun removeSearchHistory(
        keyword: String?,
        entity: SearchHistoryEntity?
    ) = tryWrapper {
        if (entity != null)
            searchHistoryDao.update(entity)
        else {
            searchHistoryDao.releaseBy(keyword.orEmpty())
        }
    }

    private suspend fun tryWrapper(tryBlock: suspend () -> Unit): Boolean {
        try {
            tryBlock()
        }
        catch (e: Exception) {
            if (BuildConfig.DEBUG)
                e.printStackTrace()
            return false
        }
        return true
    }
}

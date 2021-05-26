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

package taiwan.no.one.feat.search.data.contracts

import kotlinx.coroutines.flow.Flow
import taiwan.no.one.feat.search.data.entities.local.SearchHistoryEntity
import taiwan.no.one.feat.search.data.entities.remote.MusicInfoEntity

/**
 * This interface will common the all data stores.
 * Using prefix name (get), (create), (modify), (remove), (store)
 */
internal interface DataStore {
    suspend fun getMusic(keyword: String, page: Int): MusicInfoEntity

    suspend fun createMusic(keyword: String, page: Int, music: MusicInfoEntity): Boolean

    fun getSearchHistories(count: Int): Flow<List<SearchHistoryEntity>>

    suspend fun createOrModifySearchHistory(keyword: String): Boolean

    suspend fun removeSearchHistory(keyword: String?, entity: SearchHistoryEntity?): Boolean
}

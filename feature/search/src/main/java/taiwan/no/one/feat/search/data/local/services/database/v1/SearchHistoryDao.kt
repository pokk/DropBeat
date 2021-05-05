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

package taiwan.no.one.feat.search.data.local.services.database.v1

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import java.util.Date
import kotlinx.coroutines.flow.Flow
import taiwan.no.one.core.data.local.room.BaseDao
import taiwan.no.one.feat.search.data.entities.local.SearchHistoryEntity

/**
 * Integrated the base [androidx.room.Room] database operations.
 * Thru [androidx.room.Room] we can just define the interfaces that we want to
 * access for from a local database.
 * Using prefix name (get), (insert), (update), (delete)
 */
@Dao
internal abstract class SearchHistoryDao : BaseDao<SearchHistoryEntity> {
    /**
     * Insert a history data into the database and check the keyword if they are the same,
     * just updated the date; otherwise, insert a new data.
     *
     * @param keyword a keyword about artist, track, album, ...etc.
     */
    @Transaction
    open suspend fun insertBy(keyword: String) {
        val history = getHistory(keyword)
        if (history == null) {
            insert(SearchHistoryEntity(0, keyword, Date()))
        }
        else {
            update(history.copy(updated = Date()))
        }
    }

    /**
     * Get all data from the History table.
     */
    @Query("SELECT * FROM table_history ORDER BY updated DESC LIMIT :limit")
    abstract fun getHistories(limit: Int = 30): Flow<List<SearchHistoryEntity>>

    /**
     * Get a data with specific [keyword] from the History table
     *
     * @param keyword a keyword about artist, track, album, ...etc.
     * @return SearchHistoryData
     */
    @Query("SELECT * FROM table_history WHERE keyword=:keyword")
    abstract suspend fun getHistory(keyword: String): SearchHistoryEntity?

    /**
     * Delete a specific data by [keyword] from the History table.
     *
     * @param keyword a keyword about artist, track, album, ...etc.
     */
    @Query("DELETE FROM table_history WHERE keyword=:keyword")
    abstract suspend fun deleteBy(keyword: String)
}

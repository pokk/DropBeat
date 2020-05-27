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

package taiwan.no.one.feat.ranking.data.local.services.database.v1

import androidx.room.Dao
import androidx.room.Query
import taiwan.no.one.core.data.local.room.BaseDao
import taiwan.no.one.feat.ranking.data.entities.local.RankingIdEntity

@Dao
internal abstract class RankingDao : BaseDao<RankingIdEntity> {
    /**
     * Get all data from the Ranking table.
     */
    @Query("SELECT * FROM table_ranking")
    abstract fun retrieveRankings(): List<RankingIdEntity>

    @Query("UPDATE table_ranking SET top_track_uri=:uri, track_number=:numOfTracks WHERE id=:rankId")
    abstract fun replaceBy(rankId: Int, uri: String, numOfTracks: Int)
}

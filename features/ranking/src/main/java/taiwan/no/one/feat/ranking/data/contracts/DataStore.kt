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

package taiwan.no.one.feat.ranking.data.contracts

import taiwan.no.one.feat.ranking.data.entities.local.RankingIdEntity
import taiwan.no.one.feat.ranking.data.entities.remote.NetworkMusicInfo
import taiwan.no.one.feat.ranking.data.entities.remote.NetworkMusicRankList

/**
 * This interface will common the all data stores.
 * Using prefix name (get), (create), (modify), (remove), (store)
 */
internal interface DataStore {
    suspend fun getMusicRanking(rankId: String): NetworkMusicInfo

    suspend fun createMusicRanking(rankId: String, entity: NetworkMusicInfo): Boolean

    suspend fun getDetailOfRankings(): NetworkMusicRankList

    suspend fun createDetailOfRankings(entity: NetworkMusicRankList): Boolean

    suspend fun createRankingEntity(entities: List<RankingIdEntity>): Boolean

    suspend fun getRankingEntity(): List<RankingIdEntity>

    suspend fun modifyRankingEntity(id: Int, uri: String, number: Int): Boolean
}

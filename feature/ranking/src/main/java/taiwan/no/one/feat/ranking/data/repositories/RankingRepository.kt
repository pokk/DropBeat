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

package taiwan.no.one.feat.ranking.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import taiwan.no.one.core.data.repostory.cache.LayerCaching
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.feat.ranking.data.contracts.DataStore
import taiwan.no.one.feat.ranking.data.entities.local.RankingIdEntity
import taiwan.no.one.feat.ranking.data.entities.remote.MusicInfoEntity
import taiwan.no.one.feat.ranking.domain.repositories.RankingRepo
import java.util.Date

internal class RankingRepository(
    private val local: DataStore,
    private val remote: DataStore,
    private val sp: SharedPreferences,
) : RankingRepo {
    companion object Constant {
        private const val EXPIRED_DURATION = 360000 // 60 * 60 * 1000 = an hour
    }

    override suspend fun fetchMusicRanking(rankId: String) = object : LayerCaching<MusicInfoEntity>() {
        override var timestamp
            get() = sp.getLong(convertToKey(rankId), 0L)
            set(value) {
                sp.edit { putLong(convertToKey(rankId), value) }
            }

        override suspend fun saveCallResult(data: MusicInfoEntity) {
            local.createMusicRanking(rankId, data)
        }

        override suspend fun shouldFetch(data: MusicInfoEntity) = Date().time - timestamp > EXPIRED_DURATION

        override suspend fun loadFromLocal() = local.getMusicRanking(rankId)

        override suspend fun createCall() = remote.getMusicRanking(rankId)
    }.value().entity.songs

    override suspend fun fetchDetailOfRankings() = remote.getDetailOfRankings().briefRankEntities

    override suspend fun fetchRankings() = local.getRankingEntity()

    override suspend fun addRankings(params: List<RankingIdEntity>) = local.createRankingEntity(params)

    override suspend fun updateRanking(id: Int, uri: String, number: Int) = local.modifyRankingEntity(id, uri, number)
}

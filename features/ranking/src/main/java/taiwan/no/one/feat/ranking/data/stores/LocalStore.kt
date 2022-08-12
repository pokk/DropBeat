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

package taiwan.no.one.feat.ranking.data.stores

import taiwan.no.one.core.data.repostory.cache.local.DiskCache
import taiwan.no.one.core.data.repostory.cache.local.LocalCaching
import taiwan.no.one.core.data.repostory.cache.local.MemoryCache
import taiwan.no.one.core.data.repostory.cache.local.convertToKey
import taiwan.no.one.core.data.store.tryWrapper
import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.ranking.data.contracts.DataStore
import taiwan.no.one.feat.ranking.data.entities.local.RankingIdEntity
import taiwan.no.one.feat.ranking.data.entities.remote.NetworkMusicInfo
import taiwan.no.one.feat.ranking.data.entities.remote.NetworkMusicRankList
import taiwan.no.one.feat.ranking.data.local.services.database.v1.RankingDao

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val rankingDao: RankingDao,
    private val mmkvCache: DiskCache,
    private val lruMemoryCache: MemoryCache,
) : DataStore {
    override suspend fun getMusicRanking(rankId: String) =
        object : LocalCaching<NetworkMusicInfo>(lruMemoryCache, mmkvCache) {
            override val key get() = convertToKey(rankId)
        }.value()

    override suspend fun createMusicRanking(rankId: String, entity: NetworkMusicInfo): Boolean {
        mmkvCache.put(convertToKey(rankId), entity, NetworkMusicInfo::class.java)
        lruMemoryCache.put(convertToKey(rankId), entity, NetworkMusicInfo::class.java)
        return true
    }

    override suspend fun getDetailOfRankings() = UnsupportedOperation()

    override suspend fun createDetailOfRankings(entity: NetworkMusicRankList) = TODO()

    override suspend fun createRankingEntity(entities: List<RankingIdEntity>) = tryWrapper {
        rankingDao.insert(*entities.toTypedArray())
    }

    override suspend fun getRankingEntity() = rankingDao.getRankings()

    override suspend fun modifyRankingEntity(id: Int, uri: String, number: Int) = tryWrapper {
        rankingDao.updateBy(id, uri, number)
    }
}

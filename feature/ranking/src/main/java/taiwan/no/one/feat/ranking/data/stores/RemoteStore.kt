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

package taiwan.no.one.feat.ranking.data.stores

import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.ranking.BuildConfig
import taiwan.no.one.feat.ranking.data.contracts.DataStore
import taiwan.no.one.feat.ranking.data.entities.local.RankingIdEntity
import taiwan.no.one.feat.ranking.data.remote.services.retrofit.v1.RankingMusicService

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore(
    private val rankingMusicService: RankingMusicService
) : DataStore {
    private val basicQuery
        get() = hashMapOf(
            BuildConfig.SearchMusicQuery1 to BuildConfig.SearchMusicParameter1,
        )

    override suspend fun getMusicRanking(rankId: String) = rankingMusicService.retrieveMusicRanking(rankId, basicQuery)

    override suspend fun getDetailOfRankings() = rankingMusicService.retrieveDetailOfRankings(basicQuery)

    override suspend fun createRankingEntity(entities: List<RankingIdEntity>) = UnsupportedOperation()

    override suspend fun getRankingEntity() = UnsupportedOperation()

    override suspend fun modifyRankingEntity(id: Int, uri: String, number: Int) = UnsupportedOperation()
}

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

package taiwan.no.one.feat.ranking.domain.usecases

import taiwan.no.one.core.domain.parameter.NonRequest
import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.feat.ranking.data.entities.local.RankingIdEntity
import taiwan.no.one.feat.ranking.data.entities.remote.CommonMusicEntity.NetworkSong
import taiwan.no.one.feat.ranking.data.entities.remote.NetworkMusicRankList.NetworkBriefRank

internal typealias FetchMusicRankCase = OneShotUsecase<List<NetworkSong>, FetchMusicRankReq>
internal typealias FetchMusicRankReq = FetchMusicRankOneShotCase.Request
internal typealias FetchDetailOfRankingsCase = OneShotUsecase<List<NetworkBriefRank>, NonRequest>

internal typealias FetchRankIdsCase = OneShotUsecase<List<RankingIdEntity>, NonRequest>
internal typealias AddRankIdsCase = OneShotUsecase<Boolean, AddRankIdsReq>
internal typealias AddRankIdsReq = AddRankIdsOneShotCase.Request
internal typealias UpdateRankItemCase = OneShotUsecase<Boolean, UpdateRankItemReq>
internal typealias UpdateRankItemReq = UpdateRankItemOneShotCase.Request

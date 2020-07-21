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

package taiwan.no.one.feat.ranking.data.remote.services.retrofit.v1

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import taiwan.no.one.core.data.remote.interceptor.Constant
import taiwan.no.one.feat.ranking.data.entities.remote.MusicInfoEntity
import taiwan.no.one.feat.ranking.data.entities.remote.MusicRankListEntity
import taiwan.no.one.feat.ranking.data.remote.configs.RankingConfig

/**
 * Thru [retrofit2.Retrofit] we can just define the interfaces which we want to access for.
 * Using prefix name (retrieve), (insert), (replace), (release)
 */
internal interface RankingMusicService {
    @Headers(Constant.HEADER_MOCK_DATA, "User-Agent: Paw/3.1.7 (Macintosh; OS X/10.14.5) GCDHTTPRequest")
    @GET("${RankingConfig.API_REQUEST}/{rank_id}")
    suspend fun retrieveMusicRanking(
        @Path("rank_id") rankId: String,
        @QueryMap queries: Map<String, String>
    ): MusicInfoEntity

    @Headers(Constant.HEADER_MOCK_DATA, "User-Agent: Paw/3.1.7 (Macintosh; OS X/10.14.5) GCDHTTPRequest")
    @GET("${RankingConfig.API_REQUEST}/detail")
    suspend fun retrieveDetailOfRankings(@QueryMap queries: Map<String, String>): MusicRankListEntity
}

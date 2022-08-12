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

package taiwan.no.one.feat.search.data.remote.services.retrofit.v1

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import taiwan.no.one.core.data.remote.interceptor.Constant
import taiwan.no.one.feat.search.data.entities.remote.NetworkMusicInfo
import taiwan.no.one.feat.search.data.remote.configs.SeekerConfig

/**
 * Thru [retrofit2.Retrofit] we can just define the interfaces which we want to access for.
 * Using prefix name (retrieve), (insert), (replace), (release)
 */
internal interface SeekerBankService {
    @Headers(
        Constant.HEADER_MOCK_DATA,
        "User-Agent: Paw/3.1.7 (Macintosh; OS X/10.14.5) GCDHTTPRequest"
    )
    @GET(SeekerConfig.API_REQUEST)
    suspend fun retrieveSearchMusic(
        @QueryMap
        queries: Map<String, String>,
    ): NetworkMusicInfo
}

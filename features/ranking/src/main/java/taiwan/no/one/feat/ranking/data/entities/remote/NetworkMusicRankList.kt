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

package taiwan.no.one.feat.ranking.data.entities.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import taiwan.no.one.ext.DEFAULT_DOUBLE
import taiwan.no.one.ext.DEFAULT_INT
import taiwan.no.one.ext.DEFAULT_STR

@JsonClass(generateAdapter = true)
internal data class NetworkMusicRankList(
    @Json(name = "status")
    val status: String = DEFAULT_STR,
    @Json(name = "data")
    val briefRankEntities: List<NetworkBriefRank> = emptyList(),
) {
    @JsonClass(generateAdapter = true)
    internal data class NetworkBriefRank(
        @Json(name = "title")
        val title: String = DEFAULT_STR,
        @Json(name = "timestamp")
        val timestamp: Double = DEFAULT_DOUBLE,
        @Json(name = "sub_title")
        val subTitle: String = DEFAULT_STR,
        @Json(name = "cover_url")
        val coverUrl: String = DEFAULT_STR,
        @Json(name = "source_tip")
        val sourceTip: String = DEFAULT_STR,
        @Json(name = "type")
        val type: Int = DEFAULT_INT,
        @Json(name = "rank_id")
        val rankId: Int = DEFAULT_INT,
        val numberOfSongs: Int = DEFAULT_INT,
    )
}

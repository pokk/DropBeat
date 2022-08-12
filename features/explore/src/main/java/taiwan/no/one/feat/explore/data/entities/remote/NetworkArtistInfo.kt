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

package taiwan.no.one.feat.explore.data.entities.remote

import androidx.room.Embedded
import androidx.room.Ignore
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import taiwan.no.one.core.data.local.room.TimeEntity
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.explore.data.contracts.Dto

@JsonClass(generateAdapter = true)
internal data class NetworkArtistInfo(
    val artist: NetworkArtist?,
) : Dto {
    @JsonClass(generateAdapter = true)
    internal data class NetworkArtist(
        val name: String? = DEFAULT_STR,
        val mbid: String? = DEFAULT_STR,
        val match: String? = DEFAULT_STR,
        val url: String? = DEFAULT_STR,
        @Json(name = "image")
        val images: List<NetworkCommonLastFm.NetworkImage>? = emptyList(),
        val streamable: String? = DEFAULT_STR,
        val listeners: String? = DEFAULT_STR,
        @Json(name = "ontour")
        val onTour: String? = DEFAULT_STR,
        @Json(name = "playcount")
        val playCount: String? = DEFAULT_STR,
        @Ignore
        val stats: NetworkStats? = null,
        @Ignore
        val similar: NetworkSimilar? = null,
        @Ignore
        val tags: NetworkCommonLastFm.NetworkTags? = null,
        @Ignore
        val bio: NetworkBio? = null,
        // For database searching
        val paging: Int? = null,
        @Embedded
        val time: TimeEntity = TimeEntity(),
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkBio(
        val links: NetworkLinks?,
        val published: String?,
        val summary: String?,
        val content: String?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkLinks(
        val link: NetworkLink?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkLink(
        @Json(name = "#text")
        val text: String?,
        val rel: String?,
        val href: String?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkStats(
        val listeners: String?,
        @Json(name = "playcount")
        val playCount: String?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkSimilar(
        @Json(name = "artist")
        val artists: List<NetworkArtist>?,
    ) : Dto
}

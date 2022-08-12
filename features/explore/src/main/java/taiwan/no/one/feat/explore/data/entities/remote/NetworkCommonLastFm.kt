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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import taiwan.no.one.feat.explore.data.contracts.Dto

object NetworkCommonLastFm {
    @JsonClass(generateAdapter = true)
    internal data class NetworkTopAlbums(
        @Json(name = "album")
        val albums: List<NetworkAlbumInfo.NetworkAlbumWithArtist>,
        @Json(name = "@attr")
        val attr: NetworkAttr?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkTags(
        @Json(name = "tag")
        val tags: List<NetworkTagInfo.NetworkTag>?,
        @Json(name = "@attr")
        val attr: NetworkAttr?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkImage(
        @Json(name = "#text")
        val text: String?,
        val size: String?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkStreamable(
        @Json(name = "#text")
        val text: String?,
        val fulltrack: String?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkWiki(
        val published: String?,
        val summary: String?,
        val content: String?,
    ) : Dto

    @JsonClass(generateAdapter = true)
    internal data class NetworkAttr(
        val artist: String?,
        @Json(name = "totalPages")
        val totalPages: String?,
        val total: String?,
        val rank: String?,
        val position: String?,
        @Json(name = "perPage")
        val perPage: String?,
        val page: String?,
    ) : Dto
}

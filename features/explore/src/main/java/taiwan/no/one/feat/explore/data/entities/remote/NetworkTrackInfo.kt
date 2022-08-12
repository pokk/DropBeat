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

import com.devrapid.kotlinshaver.trimMarginAndNewLine
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NetworkTrackInfo(
    val track: NetworkTrack?,
) {
    @JsonClass(generateAdapter = true)
    internal data class NetworkTrack(
        val streamable: NetworkCommonLastFm.NetworkStreamable?,
        // Common part from [BaseTrackEntity]
        val album: NetworkAlbumInfo.NetworkAlbum? = null,
        @Json(name = "@attr")
        val attr: NetworkCommonLastFm.NetworkAttr? = null,
        val artist: NetworkArtistInfo.NetworkArtist? = null,
        val duration: String? = null,
        @Json(name = "image")
        var images: List<NetworkCommonLastFm.NetworkImage>? = emptyList(),
        var listeners: String? = null,
        val match: Double? = null,
        val mbid: String? = null,
        var name: String? = null,
        @Json(name = "playcount")
        val playcount: String? = null,
        @Json(name = "toptags")
        val topTag: NetworkCommonLastFm.NetworkTags? = null,
        var url: String? = null,
        val realUrl: String? = null,
        val wiki: NetworkCommonLastFm.NetworkWiki? = null,
        // Other
        var isFavorite: Boolean? = null,
    )

    @JsonClass(generateAdapter = true)
    internal data class NetworkTrackWithStreamable(
        val streamable: String?,
        // Common part from [BaseTrackEntity]
        val album: NetworkAlbumInfo.NetworkAlbum? = null,
        @Json(name = "@attr")
        val attr: NetworkCommonLastFm.NetworkAttr? = null,
        val artist: NetworkArtistInfo.NetworkArtist? = null,
        val duration: String? = null,
        @Json(name = "image")
        var images: List<NetworkCommonLastFm.NetworkImage>? = emptyList(),
        var listeners: String? = null,
        val match: Double? = null,
        val mbid: String? = null,
        var name: String? = null,
        @Json(name = "playcount")
        val playcount: String? = null,
        @Json(name = "toptags")
        val topTag: NetworkCommonLastFm.NetworkTags? = null,
        var url: String? = null,
        val realUrl: String? = null,
        val wiki: NetworkCommonLastFm.NetworkWiki? = null,
        // Other
        var isFavorite: Boolean? = null,
    )

    @Deprecated("Moshi is difficult to use")
    internal open class BaseTrackEntity(
        var album: NetworkAlbumInfo.NetworkAlbum? = null,
        @Json(name = "@attr")
        var attr: NetworkCommonLastFm.NetworkAttr? = null,
        var artist: NetworkArtistInfo.NetworkArtist? = null,
        var duration: String? = null,
        @Json(name = "image")
        var images: List<NetworkCommonLastFm.NetworkImage>? = emptyList(),
        var listeners: String? = null,
        var match: Double? = null,
        var mbid: String? = null,
        var name: String? = null,
        @Json(name = "playcount")
        var playcount: String? = null,
        @Json(name = "toptags")
        var topTag: NetworkCommonLastFm.NetworkTags? = null,
        var url: String? = null,
        var realUrl: String? = null,
        var wiki: NetworkCommonLastFm.NetworkWiki? = null,
        // Other
        var isFavorite: Boolean? = null,
    ) {
        override fun toString() = """
            |album: $album,
            | attr: $attr,
            | artist: $artist,
            | duration: $duration,
            | images: $images,
            | listeners: $listeners,
            | match: $match,
            | mbid: $mbid,
            | name: $name,
            | playcount: $playcount,
            | topTag: $topTag,
            | url: $url,
            | realUrl: $realUrl,
            | wiki: $wiki,
            | isFavorite: $isFavorite
            |""".trimMarginAndNewLine()
    }
}

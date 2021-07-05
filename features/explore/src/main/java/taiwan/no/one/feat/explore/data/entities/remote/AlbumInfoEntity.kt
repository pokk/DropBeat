/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
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
import taiwan.no.one.ext.DEFAULT_INT

@JsonClass(generateAdapter = true)
internal data class AlbumInfoEntity(
    val album: AlbumEntity?,
) {
    @JsonClass(generateAdapter = true)
    internal data class AlbumEntity(
        val artist: String?,
        @Json(name = "playcount")
        val playCount: String? = null,
    ) : BaseAlbumEntity() {
        override fun toString() = """
            |${this::class.java.simpleName}(
            |artist=$artist
            |playCount=$playCount
            |${super.toString()})
            |""".trimMarginAndNewLine()
    }

    @JsonClass(generateAdapter = true)
    internal data class AlbumWithArtistEntity(
        val artist: ArtistInfoEntity.ArtistEntity?,
        @Json(name = "playcount")
        val playCount: String? = null,
        val index: Int = DEFAULT_INT,
    ) : BaseAlbumEntity() {
        override fun toString() = """
            |${this::class.java.simpleName}(
            |artist=$artist
            |playCount=$playCount
            |index=$index
            |${super.toString()})
            |""".trimMarginAndNewLine()
    }

    @JsonClass(generateAdapter = true)
    internal open class BaseAlbumEntity(
        @Json(name = "@attr")
        var attr: CommonLastFmEntity.AttrEntity? = null,
        @Json(name = "image")
        var images: List<CommonLastFmEntity.ImageEntity>? = null,
        var listeners: String? = null,
        var mbid: String? = null,
        var name: String? = null,
        var tags: CommonLastFmEntity.TagsEntity? = null,
        var title: String? = null,
        @Json(name = "tracks")
        var tracks: TopTrackInfoEntity.TracksEntity? = null,
        var url: String? = null,
        var wiki: CommonLastFmEntity.WikiEntity? = null,
    ) {
        override fun toString() = """
            |attr=$attr,
            |images=$images,
            |listeners=$listeners,
            |mbid=$mbid,
            |name=$name,
            |tags=$tags,
            |title=$title,
            |tracks=$tracks,
            |url=$url,
            |wiki=$wiki
            |""".trimMarginAndNewLine()
    }
}

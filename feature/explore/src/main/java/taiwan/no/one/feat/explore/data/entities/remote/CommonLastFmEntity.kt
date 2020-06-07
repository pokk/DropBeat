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

package taiwan.no.one.feat.explore.data.entities.remote

import com.google.gson.annotations.SerializedName

object CommonLastFmEntity {
    internal data class TopAlbumsEntity(
        @SerializedName("album")
        val albums: List<AlbumInfoEntity.AlbumWithArtistEntity>,
        @SerializedName("@attr")
        val attr: CommonLastFmEntity.AttrEntity?
    )

    internal data class TagsEntity(
        @SerializedName("tag")
        val tags: List<TagInfoEntity.TagEntity>?,
        @SerializedName("@attr")
        val attr: AttrEntity?
    )

    internal data class ImageEntity(
        @SerializedName("#text")
        val text: String?,
        val size: String?
    )

    internal data class StreamableEntity(
        @SerializedName("#text")
        val text: String?,
        val fulltrack: String?
    )

    internal data class WikiEntity(
        val published: String?,
        val summary: String?,
        val content: String?
    )

    internal data class AttrEntity(
        val artist: String?,
        @SerializedName("totalPages")
        val totalPages: String?,
        val total: String?,
        val rank: String?,
        val position: String?,
        @SerializedName("perPage")
        val perPage: String?,
        val page: String?
    )
}

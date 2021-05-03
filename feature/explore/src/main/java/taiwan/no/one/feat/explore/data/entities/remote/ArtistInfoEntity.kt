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

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.util.Date
import taiwan.no.one.ext.DEFAULT_STR

internal data class ArtistInfoEntity(
    val artist: ArtistEntity?,
) {
    internal data class ArtistEntity(
        val name: String? = DEFAULT_STR,
        val mbid: String? = DEFAULT_STR,
        val match: String? = DEFAULT_STR,
        val url: String? = DEFAULT_STR,
        @SerializedName("image")
        val images: List<CommonLastFmEntity.ImageEntity>? = emptyList(),
        val streamable: String? = DEFAULT_STR,
        val listeners: String? = DEFAULT_STR,
        @SerializedName("ontour")
        val onTour: String? = DEFAULT_STR,
        @SerializedName("playcount")
        val playCount: String? = DEFAULT_STR,
        @Ignore
        val stats: StatsEntity? = null,
        @Ignore
        val similar: SimilarEntity? = null,
        @Ignore
        val tags: CommonLastFmEntity.TagsEntity? = null,
        @Ignore
        val bio: BioEntity? = null,
        // For database searching
        val paging: Int? = null,
        @ColumnInfo(name = "created_at")
        val createdAt: Date = Date(),
        @ColumnInfo(name = "updated_at")
        val updatedAt: Date = Date(),
    )

    internal data class BioEntity(
        val links: LinksEntity?,
        val published: String?,
        val summary: String?,
        val content: String?,
    )

    internal data class LinksEntity(
        val link: LinkEntity?,
    )

    internal data class LinkEntity(
        @SerializedName("#text")
        val text: String?,
        val rel: String?,
        val href: String?,
    )

    internal data class StatsEntity(
        val listeners: String?,
        @SerializedName("playcount")
        val playCount: String?,
    )

    internal data class SimilarEntity(
        @SerializedName("artist")
        val artists: List<ArtistEntity>?,
    )
}

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

package taiwan.no.one.feat.ranking.data.entities.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import taiwan.no.one.ext.DEFAULT_STR

@JsonClass(generateAdapter = true)
internal object CommonMusicEntity {
    internal data class UserEntity(
        val address: String = DEFAULT_STR,
        @Json(name = "avatar_url")
        val avatarUrl: String = DEFAULT_STR,
        val birthday: Int = 0,
        val email: String = DEFAULT_STR,
        val gender: Int = 0,
        val phone: String = DEFAULT_STR,
        val platform: Int = 0,
        @Json(name = "platform_uid")
        val platformUid: String = DEFAULT_STR,
        @Json(name = "screen_name")
        val screenName: String = DEFAULT_STR,
        val uid: String = DEFAULT_STR,
    )

    @JsonClass(generateAdapter = true)
    internal data class SongEntity(
        val artist: String = DEFAULT_STR,
        @Json(name = "cdn_coverURL")
        val cdnCoverUrl: String = DEFAULT_STR,
        @Json(name = "copyright_type")
        val copyrightType: Int = 0,
        val coverURL: String = DEFAULT_STR,
        val flag: Int = 0,
        val length: Int = 0,
        val lyricURL: String = DEFAULT_STR,
        val mv: MvEntity = MvEntity(),
        @Json(name = "ori_coverURL")
        val oriCoverUrl: String = DEFAULT_STR,
        @Json(name = "other_sources")
        val otherSources: List<Any> = emptyList(),
        @Json(name = "share_uri")
        val shareUri: String = DEFAULT_STR,
        val sid: Int = 0,
        @Json(name = "song_id_ext")
        val songIdExt: String = DEFAULT_STR,
        val source: SourceEntity = SourceEntity(),
        val title: String = DEFAULT_STR,
        val uploader: String = DEFAULT_STR,
        val url: String = DEFAULT_STR,
    )

    @JsonClass(generateAdapter = true)
    internal data class PlayListEntity(
        @Json(name = "comment_count")
        val commentCount: Int = 0,
        @Json(name = "fav_count")
        val favCount: Int = 0,
        @Json(name = "has_fav")
        val hasFav: Boolean = false,
        @Json(name = "is_cover_modified")
        val isCoverModified: Boolean = false,
        val permission: Int = 0,
        @Json(name = "played_count")
        val playedCount: String = DEFAULT_STR,
        @Json(name = "share_count")
        val shareCount: Int = 0,
        @Json(name = "share_link")
        val shareLink: String = DEFAULT_STR,
        @Json(name = "share_uri")
        val shareUri: String = DEFAULT_STR,
        @Json(name = "song_list_cover")
        val songListCover: String = DEFAULT_STR,
        @Json(name = "song_list_desc")
        val songListDesc: String = DEFAULT_STR,
        @Json(name = "song_list_id")
        val songListId: String = DEFAULT_STR,
        @Json(name = "song_list_name")
        val songListName: String = DEFAULT_STR,
        @Json(name = "song_list_type")
        val songListType: Int = 0,
        @Json(name = "song_num")
        val songNum: Int = 0,
        val songs: List<SongEntity> = emptyList(),
        @Json(name = "tag_ids")
        val tagIds: List<Any> = emptyList(),
        val tags: List<Any> = emptyList(),
        val user: UserEntity = UserEntity(),
    )

    @JsonClass(generateAdapter = true)
    internal data class MvEntity(
        val comments: Int = 0,
        @Json(name = "cover_image")
        val coverImage: String = DEFAULT_STR,
        val ctime: String = DEFAULT_STR,
        val description: String = DEFAULT_STR,
        val dislikes: Int = 0,
        val duration: String = DEFAULT_STR,
        val embeddable: Int = 0,
        @Json(name = "fm_mv_active")
        val fmMvActive: Int = 0,
        val id: Int = 0,
        @Json(name = "is_active")
        val isActive: Int = 0,
        @Json(name = "is_public")
        val isPublic: Int = 0,
        @Json(name = "language_id")
        val languageId: Int = 0,
        val likes: Int = 0,
        val mtime: String = DEFAULT_STR,
        @Json(name = "published_at")
        val publishedAt: String = DEFAULT_STR,
        val rate: Int = 0,
        @Json(name = "region_allowed")
        val regionAllowed: String = DEFAULT_STR,
        @Json(name = "region_blocked")
        val regionBlocked: String = DEFAULT_STR,
        @Json(name = "review_info")
        val reviewInfo: String = DEFAULT_STR,
        val source: Int = 0,
        val title: String = DEFAULT_STR,
        val views: Long = 0,
        @Json(name = "y_video_id")
        val yVideoId: String = DEFAULT_STR,
    )

    @JsonClass(generateAdapter = true)
    internal data class SourceEntity(
        val unknown: Any? = null,
    )
}

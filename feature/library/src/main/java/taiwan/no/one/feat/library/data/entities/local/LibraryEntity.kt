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

package taiwan.no.one.feat.library.data.entities.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import taiwan.no.one.core.data.local.room.TimeEntity
import taiwan.no.one.ext.DEFAULT_STR

internal data class LibraryEntity(
    val count: Int,
    val playlists: List<PlayListEntity>,
) {
    @Entity(tableName = "table_playlist", indices = [Index("name", unique = true)])
    internal data class PlayListEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0, // For the room database
        val name: String = DEFAULT_STR,
        @ColumnInfo(name = "song_ids")
        val songIds: List<Int> = emptyList(),
        val count: Int = 0,
        @ColumnInfo(name = "cover_url")
        val coverUrl: String = DEFAULT_STR,
        @Embedded
        val time: TimeEntity = TimeEntity(),
    ) {
        @Ignore
        var songs: List<SongEntity> = emptyList()

        override fun toString() = """
            id: $id,
            name: $name,
            songIds: $songIds,
            count: $count,
            doverUrl: $coverUrl,
            time: $time,
            songs: $songs
        """.trimIndent()
    }

    @Entity(tableName = "table_song", indices = [Index("title", "artist", unique = true)])
    internal data class SongEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0, // For the room database
        val title: String = DEFAULT_STR,
        val artist: String = DEFAULT_STR,
        val uri: String = DEFAULT_STR,
        @ColumnInfo(name = "cover_uri")
        val coverUri: String = DEFAULT_STR,
        val duration: Int = 0,
        @ColumnInfo(name = "is_favorite")
        val isFavorite: Boolean = false,
        @Embedded
        val time: TimeEntity = TimeEntity(),
        // ↓↓↓ If downloaded ↓↓↓
        @ColumnInfo(name = "has_own")
        val hasOwn: Boolean = false,
        @ColumnInfo(name = "local_uri")
        val localUri: String = DEFAULT_STR,
        @ColumnInfo(name = "last_listen_at")
        val lastListenAt: Date = Date(),
        @ColumnInfo(name = "downloaded_at")
        val downloadedAt: Date = Date(),
    )
}

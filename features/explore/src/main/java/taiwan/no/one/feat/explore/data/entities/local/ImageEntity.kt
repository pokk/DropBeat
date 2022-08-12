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

package taiwan.no.one.feat.explore.data.entities.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import taiwan.no.one.core.data.local.room.TimeEntity
import taiwan.no.one.ext.DEFAULT_LONG
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.explore.data.contracts.Po
import taiwan.no.one.feat.explore.data.entities.local.enums.ImgQuality

@Entity(tableName = "table_image", indices = [Index("url", unique = true)])
internal data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0L, // For the room database
    val quality: ImgQuality = ImgQuality.ELSE,
    val url: String = DEFAULT_STR,
    // related connection id
    @ColumnInfo(name = "artist_id")
    val artistId: Long = DEFAULT_LONG,
    @ColumnInfo(name = "album_id")
    val albumId: Long = DEFAULT_LONG,
    @ColumnInfo(name = "track_id")
    val trackId: Long = DEFAULT_LONG,
    @Embedded
    val time: TimeEntity = TimeEntity(),
) : Po

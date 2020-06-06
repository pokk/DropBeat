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

package taiwan.no.one.feat.login.data.local.services.database.convert

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import taiwan.no.one.feat.login.data.entities.local.LibraryEntity

internal class SongListConvert {
    @TypeConverter
    fun fromSongsToStr(songs: List<LibraryEntity.SongEntity>?): String? {
        val type = object : TypeToken<List<LibraryEntity.SongEntity>>() {}.type
        return Gson().newBuilder().create().toJson(songs, type)
    }

    @TypeConverter
    fun fromStrToSongs(songsString: String?): List<LibraryEntity.SongEntity>? {
        val type = object : TypeToken<List<LibraryEntity.SongEntity>>() {}.type
        return Gson().newBuilder().create().fromJson(songsString, type)
    }
}

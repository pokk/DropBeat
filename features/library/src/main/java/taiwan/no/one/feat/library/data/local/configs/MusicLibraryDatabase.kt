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

package taiwan.no.one.feat.library.data.local.configs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import taiwan.no.one.core.data.local.room.convert.DateConvert
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity
import taiwan.no.one.feat.library.data.local.services.database.converts.IdListConvert
import taiwan.no.one.feat.library.data.local.services.database.v1.PlaylistDao
import taiwan.no.one.feat.library.data.local.services.database.v1.SongDao

/**
 * The access operations to a database.
 */
@Database(
    entities = [LibraryEntity.PlayListEntity::class, LibraryEntity.SongEntity::class],
    version = 1,
)
@TypeConverters(DateConvert::class, IdListConvert::class)
internal abstract class MusicLibraryDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MusicLibraryDatabase? = null
        private const val DATABASE_NAME = "music_library.db"

        fun getDatabase(context: Context): MusicLibraryDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicLibraryDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }

    abstract fun createSongDao(): SongDao

    abstract fun createPlaylistDao(): PlaylistDao
}

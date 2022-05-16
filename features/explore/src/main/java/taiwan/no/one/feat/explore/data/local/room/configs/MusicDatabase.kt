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

package taiwan.no.one.feat.explore.data.local.room.configs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import taiwan.no.one.core.data.local.room.convert.DateConvert
import taiwan.no.one.feat.explore.data.entities.local.ArtistEntity
import taiwan.no.one.feat.explore.data.entities.local.BioEntity
import taiwan.no.one.feat.explore.data.entities.local.ImageEntity
import taiwan.no.one.feat.explore.data.local.room.converts.QualityConvert
import taiwan.no.one.feat.explore.data.local.services.database.v1.ArtistDao
import taiwan.no.one.feat.explore.data.local.services.database.v1.BioDao
import taiwan.no.one.feat.explore.data.local.services.database.v1.ImageDao

/**
 * The access operations to a database.
 */
@Database(
    entities = [ArtistEntity::class, ImageEntity::class, BioEntity::class],
    version = 1,
)
@TypeConverters(DateConvert::class, QualityConvert::class)
internal abstract class MusicDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MusicDatabase? = null
        private const val DATABASE_NAME = "fm_music_cache.db"

        fun getDatabase(context: Context): MusicDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }

    abstract fun createArtistDao(): ArtistDao

    abstract fun createImageDao(): ImageDao

    abstract fun createBioDao(): BioDao
}

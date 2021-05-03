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

package taiwan.no.one.feat.library.data.local.services.database.v1

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import java.util.Date
import taiwan.no.one.core.data.local.room.BaseDao
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity

/**
 * Integrated the base [androidx.room.Room] database operations.
 * Thru [androidx.room.Room] we can just define the interfaces that we want to
 * access for from a local database.
 * Using prefix name (get), (insert), (update), (delete)
 */
@Dao
internal abstract class SongDao : BaseDao<LibraryEntity.SongEntity> {
    @Query("SELECT * FROM table_song WHERE local_uri=:path")
    abstract suspend fun getMusicByLocal(path: String): LibraryEntity.SongEntity?

    @Query("SELECT * FROM table_song WHERE uri=:path")
    abstract suspend fun getMusicByRemote(path: String): LibraryEntity.SongEntity?

    @Query("SELECT * FROM table_song WHERE id=:id")
    abstract suspend fun getMusicBy(id: Int): LibraryEntity.SongEntity

    /**
     * Get all data from the local music table.
     */
    @Query("SELECT * FROM table_song")
    abstract suspend fun getMusics(): List<LibraryEntity.SongEntity>

    /**
     * Get one data from the local music table.
     *
     * @param ids List<Int>
     */
    @Query("SELECT * FROM table_song WHERE id IN (:ids)")
    abstract suspend fun getMusics(ids: List<Int>): List<LibraryEntity.SongEntity>

    /**
     * Get one data from the local music table.
     *
     * @param track
     * @param artist
     */
    @Query("SELECT * FROM table_song WHERE title=:track AND artist=:artist")
    abstract suspend fun getMusics(track: String, artist: String): LibraryEntity.SongEntity?

    /**
     * Insert a data if there's the same data inside the database, it will be replaced.
     *
     * @param newData
     */
    @Transaction
    open suspend fun insertBy(newData: LibraryEntity.SongEntity, addOrMinus: Boolean) {
        val existMusic = getMusics(newData.title, newData.artist)
        var updatedData = newData
        // Update the download date if the data isn't download information.
        if (!newData.hasOwn || newData.hasOwn && existMusic?.hasOwn == true) {
            updatedData = newData.copy(downloadedAt = Date(0),
                                       lastListenAt = if (!updatedData.hasOwn) {
                                           updatedData.lastListenAt
                                       }
                                       else {
                                           existMusic?.lastListenAt ?: updatedData.lastListenAt
                                       })
        }
    }

    @Query("UPDATE table_song SET is_favorite=:isFavorite WHERE id=:id")
    abstract suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    /**
     * Remove a music from local music table.
     *
     * @param id local music's id.
     */
    @Query("DELETE FROM table_song WHERE id=:id")
    abstract suspend fun deleteBy(id: Int)
}

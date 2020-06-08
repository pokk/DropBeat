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
import taiwan.no.one.core.data.local.room.BaseDao
import taiwan.no.one.ext.DEFAULT_INT
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity

/**
 * Integrated the base [androidx.room.Room] database operations.
 * Thru [androidx.room.Room] we can just define the interfaces that we want to
 * access for from a local database.
 * Using prefix name (get), (insert), (update), (delete)
 */
@Dao
internal abstract class PlaylistDao : BaseDao<LibraryEntity.PlayListEntity> {
    /**
     * Get all data from the playlist table.
     */
    @Query("SELECT * FROM table_playlist")
    abstract suspend fun getPlaylists(): List<LibraryEntity.PlayListEntity>

    @Query("SELECT * FROM table_playlist ORDER BY id DESC LIMIT 1")
    abstract suspend fun getLatestPlaylist(): LibraryEntity.PlayListEntity

    /**
     * Get a data from the playlist table.
     *
     * @param id playlist's id.
     */
    @Query("SELECT * FROM table_playlist WHERE id=:id")
    abstract suspend fun getPlaylist(id: Int): LibraryEntity.PlayListEntity

    @Query("UPDATE table_playlist SET count=count+1 WHERE id=:id")
    abstract suspend fun increaseCountBy(id: Int)

    @Query("UPDATE table_playlist SET count=count-1 WHERE id=:id")
    abstract suspend fun decreaseCountBy(id: Int)

    /**
     * Update a playlist by [id].
     *
     * @param id
     * @param name
     * @param trackNumber
     */
    @Transaction open suspend fun updateBy(id: Int, name: String = DEFAULT_STR, trackNumber: Int = DEFAULT_INT) {
        if (name == DEFAULT_STR && trackNumber == DEFAULT_INT) return
        val playlist = getPlaylist(id)
        val newPlaylist = playlist.copy(name = name.takeIf { it != DEFAULT_STR } ?: playlist.name,
                                        count = trackNumber.takeIf { it >= 0 } ?: playlist.count)
        update(newPlaylist)
    }

    /**
     * Delete a playlist by [id] from the database.
     *
     * @param id
     */
    @Query("DELETE FROM table_playlist WHERE id=:id")
    abstract suspend fun deleteBy(id: Int)
}

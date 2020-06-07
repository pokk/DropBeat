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
    /**
     * Insert a data if there's the same data inside the database, it will be replaced.
     *
     * @param newData
     */
    @Transaction
    open suspend fun insertBy(newData: LibraryEntity.SongEntity, addOrMinus: Boolean) {
        val existMusic = getMusic(newData.title, newData.artist)
        var updatedData = newData
        // Update the download date if the data isn't download information.
        if (!newData.hasOwn || (newData.hasOwn && existMusic?.hasOwn == true))
            updatedData = newData.copy(downloadedAt = Date(0),
                                       lastListenAt = if (!updatedData.hasOwn)
                                           updatedData.lastListenAt
                                       else
                                           existMusic?.lastListenAt ?: updatedData.lastListenAt)
//        if (existMusic == null)
//            insert(updatedData)
//        else {
//            val playlist = updatedData.playlistList.takeIf { it != DEFAULT_STR }
//                               ?.let {
//                                   // If there's no playlist, in the other words → the first adding.
//                                   if (existMusic.playlistList.isBlank())
//                                       it
//                                   else {
//                                       // To be a list of origin playlist from database.
//                                       val splitFromOrigin = existMusic.playlistList.split(",")
//                                       // To be a list of new updated playlist from user new adding.
//                                       val splitFromUpdated = updatedData.playlistList.split(",")
//                                       (if (addOrMinus) { // Add a new playlist into original playlist.
//                                           if (splitFromUpdated.size == 1 && splitFromUpdated.first() in splitFromOrigin) {
//                                               throw Exception("Insert the a data fail because duplicate!!")
//                                           }
//                                           (splitFromOrigin + splitFromUpdated).distinct()
//                                       }
//                                       else // Remove the playlist from original playlist.
//                                           splitFromOrigin.subtract(splitFromUpdated)).joinToString(
//                                           ",")
//                                   }
//                               } ?: existMusic.playlistList // There's no new playlist inside of parameters, just put original playlist.
//            // If remove a track from playlist and the track was downloaded, we should clean the [LocalTrackUri] to be blank.
//            val localTrackUri =
//                if (!addOrMinus && PlaylistIndex.DOWNLOADED.ordinal.toString() in existMusic.playlistList) {
//                    DEFAULT_STR
//                }
//                else
//                    updatedData.localUri.takeIf { it != DEFAULT_STR } ?: existMusic.localUri
//            update(existMusic.copy(hasOwn = updatedData.hasOwn.takeIf { it } ?: existMusic.hasOwn,
//                                    uri = updatedData.uri.takeIf { it != DEFAULT_STR } ?: existMusic.uri,
//                                    localUri = localTrackUri,
//                                    coverUri = updatedData.coverUri.takeIf { it != DEFAULT_STR } ?: existMusic.coverUri,
//                                    playlistList = playlist,
//                                   lastListenAt = updatedData.lastListenAt))
//        }
    }

    /**
     * Get all data from the local music table.
     */
    @Query("SELECT * FROM table_song")
    abstract suspend fun getMusics(): List<LibraryEntity.SongEntity>

    /**
     * Get one data from the local music table.
     *
     * @param track
     * @param artist
     */
    @Query("SELECT * FROM table_song WHERE title=:track AND artist=:artist")
    abstract suspend fun getMusic(track: String, artist: String): LibraryEntity.SongEntity?

    /**
     * Remove a music from local music table.
     *
     * @param id local music's id.
     */
    @Query("DELETE FROM table_song WHERE id=:id")
    abstract suspend fun deleteBy(id: Int)
}

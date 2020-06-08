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

package taiwan.no.one.feat.library.data.stores

import com.tencent.mmkv.MMKV
import taiwan.no.one.feat.library.data.contracts.DataStore
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.data.local.services.database.v1.PlaylistDao
import taiwan.no.one.feat.library.data.local.services.database.v1.SongDao

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val mmkv: MMKV,
    private val playlistDao: PlaylistDao,
    private val songDao: SongDao,
) : DataStore {
    override suspend fun getMusics(playlistId: Int) = songDao.getMusics()

    override suspend fun createMusics(songs: List<SongEntity>) = songDao.insert(*songs.toTypedArray())

    override suspend fun createMusicToPlaylist(song: SongEntity, playlistId: Int) = TODO()

    override suspend fun removeMusic(id: Int) = songDao.deleteBy(id)

    override suspend fun getPlaylists() = playlistDao.getPlaylists().apply {
        map { it.songs = it.songIds.let { songDao.getMusics(it) } }
    }

    override suspend fun getPlaylist(playlistId: Int) = playlistDao.getPlaylist(playlistId)

    override suspend fun getTheNewestPlaylist() = playlistDao.getLatestPlaylist()

    override suspend fun createPlaylist(playlist: PlayListEntity) = playlistDao.insert(playlist)

    override suspend fun modifyPlaylist(playlistId: Int) = TODO()

    override suspend fun modifyCountOfPlaylist() = TODO()

    override suspend fun removePlaylist(playlistId: Int) = playlistDao.deleteBy(playlistId)
}

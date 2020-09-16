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

import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.library.data.contracts.DataStore
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore : DataStore {
    override suspend fun getMusic(remoteUri: String?, localUri: String?) = UnsupportedOperation()

    override suspend fun getMusics(playlistId: Int) = UnsupportedOperation()

    override suspend fun createMusics(songs: List<SongEntity>) = UnsupportedOperation()

    override suspend fun createMusicToPlaylist(song: SongEntity, playlistId: Int) = UnsupportedOperation()

    override suspend fun removeMusic(id: Int) = UnsupportedOperation()

    override suspend fun getPlaylists() = UnsupportedOperation()

    override suspend fun getPlaylist(playlistId: Int) = UnsupportedOperation()

    override suspend fun getTheNewestPlaylist() = UnsupportedOperation()

    override suspend fun createPlaylist(playlist: PlayListEntity): Unit = UnsupportedOperation()

    override suspend fun modifyPlaylist(playlist: PlayListEntity) = UnsupportedOperation()

    override suspend fun removePlaylist(playlistId: Int?, playlist: PlayListEntity?) = UnsupportedOperation()
}

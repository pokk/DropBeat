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

package taiwan.no.one.sync.data.repositories

import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.sync.data.contracts.DataStore
import taiwan.no.one.sync.domain.repositories.SyncRepo

internal class SyncRepository(
    private val remote: DataStore,
) : SyncRepo {
    override suspend fun addAccount(userInfo: UserInfoEntity) = remote.createAccount(userInfo)

    override suspend fun fetchPlaylists(userInfo: UserInfoEntity) = remote.getPlaylists(userInfo)

    override suspend fun updatePlaylist(playlist: SimplePlaylistEntity) = remote.modifyPlaylist(playlist)

    override suspend fun addPlaylist(playlist: SimplePlaylistEntity) = remote.createPlaylist(playlist)

    override suspend fun deletePlaylist() = TODO()

    override suspend fun fetchSongs() = TODO()

    override suspend fun updateSong() = TODO()

    override suspend fun addSong(song: SimpleTrackEntity) = remote.createSong(song)

    override suspend fun addPlaylistRefToAccount(userInfo: UserInfoEntity, refPlaylistPaths: List<String>) =
        remote.createPlaylistRefToAccount(userInfo, refPlaylistPaths)

    override suspend fun addSongRefToPlaylist(refPlaylistPath: String, refSongsPath: List<String>) =
        remote.createSongRefToPlaylist(refPlaylistPath, refSongsPath)
}

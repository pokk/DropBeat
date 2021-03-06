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

package taiwan.no.one.sync.domain.usecases

import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.core.domain.usecase.Usecase
import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.sync.domain.repositories.SyncRepo

internal class AddPlaylistOneShotCase(
    private val repo: SyncRepo,
) : OneShotUsecase<Boolean, AddPlaylistRequest>() {
    // TODO(jieyi): 6/2/21 This bunch of the operations should be done in a transaction.
    override suspend fun acquireCase(parameter: AddPlaylistRequest?) = parameter.ensure {
        // If there the playlist has been sync(i.e. has [refPath] value), will be filtered.
        val playlistRefs = playlists.filter { it.refPath.isEmpty() }
            .map { repo.addPlaylist(it).apply { it.refPath = this } }
        // Add the songs to the remote.
        val refOfSongs = songs.map { it.map { repo.addSong(it).apply { it.refPath = this } } }
            .onEachIndexed { index, refOfSongs -> playlists[index].refOfSongs = refOfSongs }
        // Attach the songs ref to the each playlist.
        playlists.map(SimplePlaylistEntity::refPath)
            .zip(refOfSongs)
            .forEach { (refOfPlaylist, refOfSongs) -> repo.addSongRefToPlaylist(refOfPlaylist, refOfSongs) }
        // Attach all playlists to the account.
        repo.addPlaylistRefToAccount(userInfo, playlistRefs)
    }
}

data class AddPlaylistRequest(
    val userInfo: UserInfoEntity,
    val playlists: List<SimplePlaylistEntity>,
    val songs: List<List<SimpleTrackEntity>>,
) : Usecase.RequestValues

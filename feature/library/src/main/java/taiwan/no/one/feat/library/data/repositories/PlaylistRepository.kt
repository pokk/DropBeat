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

package taiwan.no.one.feat.library.data.repositories

import taiwan.no.one.feat.library.data.contracts.DataStore
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo

internal class PlaylistRepository(
    private val local: DataStore,
    private val remote: DataStore,
) : PlaylistRepo {
    override suspend fun fetchMusics(playlistId: Int) = local.getMusics(playlistId)

    override suspend fun addMusicToPlaylist(song: SongEntity, playlistId: Int) =
        local.createMusicToPlaylist(song, playlistId)

    override suspend fun deleteMusic(playlistId: Int?, playlist: PlayListEntity?) =
        local.removePlaylist(playlistId, playlist)

    override suspend fun fetchPlaylist(playlistId: Int) = local.getPlaylist(playlistId)

    override suspend fun fetchPlaylists() = local.getPlaylists()

    override suspend fun fetchTheNewestPlaylist() = local.getTheNewestPlaylist()

    override suspend fun addPlaylist(playlist: PlayListEntity, ifExistAndIgnore: Boolean) =
        local.createPlaylist(playlist, ifExistAndIgnore)

    override suspend fun updatePlaylist(playlist: PlayListEntity) = local.modifyPlaylist(playlist)

    override suspend fun deletePlaylist(playlistId: Int?, playlist: PlayListEntity?) =
        local.removePlaylist(playlistId, playlist)
}

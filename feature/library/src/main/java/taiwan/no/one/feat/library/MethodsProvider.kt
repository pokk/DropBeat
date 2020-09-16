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

package taiwan.no.one.feat.library

import com.google.auto.service.AutoService
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsReq
import taiwan.no.one.feat.library.domain.usecases.CreateDefaultPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchSongCase
import taiwan.no.one.feat.library.domain.usecases.FetchSongReq
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistReq

@AutoService(LibraryMethodsProvider::class)
class MethodsProvider : LibraryMethodsProvider, DIAware {
    override val di by lazy { (DropBeatApp.appContext as DropBeatApp).di }
    private val createDefaultPlaylistCase by instance<CreateDefaultPlaylistCase>()
    private val addSongsCase by instance<AddSongsCase>()
    private val updatePlaylistCase by instance<UpdatePlaylistCase>()
    private val fetchSongCase by instance<FetchSongCase>()

    override suspend fun createDefaultPlaylists(): Boolean {
        createDefaultPlaylistCase.execute()
        return true
    }

    override suspend fun addSongToPlaylist(songId: Int, playlistId: Int): Boolean {
        updatePlaylistCase.execute(UpdatePlaylistReq(playlistId, songIds = listOf(songId), isAddSongs = true))
        return true
    }

    override suspend fun addSongToPlaylist(songLocalPath: String, playlistId: Int): Boolean {
        updatePlaylistCase.execute(UpdatePlaylistReq(playlistId, songsPaths = listOf(songLocalPath), isAddSongs = true))
        return true
    }

    override suspend fun downloadTrack(songsStream: String): Boolean {
        addSongsCase.execute(AddSongsReq(songsStream = songsStream))
        return true
    }

    override suspend fun hasOwnTrack(uri: String) = fetchSongCase.execute(FetchSongReq(uri)).map(SongEntity::hasOwn)

    override suspend fun isFavoriteTrack(uri: String) = TODO()
}

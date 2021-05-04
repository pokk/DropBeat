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
import taiwan.no.one.dropbeat.data.entities.SimplePlaylistEntity
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.data.mappers.TrackMapper
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.AddSongsAndPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsAndPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsReq
import taiwan.no.one.feat.library.domain.usecases.CreateDefaultPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchAllPlaylistsCase
import taiwan.no.one.feat.library.domain.usecases.FetchIsInThePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchIsInThePlaylistReq
import taiwan.no.one.feat.library.domain.usecases.FetchSongCase
import taiwan.no.one.feat.library.domain.usecases.FetchSongReq
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistReq
import taiwan.no.one.feat.library.domain.usecases.UpdateSongCase
import taiwan.no.one.feat.library.domain.usecases.UpdateSongReq
import taiwan.no.one.mediaplayer.MusicInfo

@AutoService(LibraryMethodsProvider::class)
class MethodsProvider : LibraryMethodsProvider, DIAware {
    override val di by lazy { (DropBeatApp.appContext as DropBeatApp).di }
    private val createDefaultPlaylistCase by instance<CreateDefaultPlaylistCase>()
    private val addSongsCase by instance<AddSongsCase>()
    private val addSongsAndPlaylistCase by instance<AddSongsAndPlaylistCase>()
    private val addPlaylistCase by instance<AddPlaylistCase>()
    private val updatePlaylistCase by instance<UpdatePlaylistCase>()
    private val updateSongCase by instance<UpdateSongCase>()
    private val fetchAllPlaylistsCase by instance<FetchAllPlaylistsCase>()
    private val fetchSongCase by instance<FetchSongCase>()
    private val fetchIsInThePlaylistCase by instance<FetchIsInThePlaylistCase>()

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

    override suspend fun addSongToPlaylist(musicInfo: MusicInfo, playlistId: Int): Boolean {
        addSongsAndPlaylistCase.execute(
            AddSongsAndPlaylistReq(TrackMapper.musicInfoToSongEntitiesJson(musicInfo), playlistId))
        return true
    }

    override suspend fun removeSongFromPlaylist(songId: Int, playlistId: Int): Boolean {
        updatePlaylistCase.execute(UpdatePlaylistReq(playlistId, songIds = listOf(songId), isAddSongs = false))
        return true
    }

    override suspend fun downloadTrack(songsStream: String): Boolean {
        addSongsCase.execute(AddSongsReq(songsStream = songsStream))
        return true
    }

    override suspend fun hasOwnTrack(uri: String) = runCatching { fetchSongCase.execute(FetchSongReq(uri)) }
        .map(SongEntity::hasOwn)

    override suspend fun isFavoriteTrack(uri: String, playlistId: Int) =
        runCatching { fetchIsInThePlaylistCase.execute(FetchIsInThePlaylistReq(null, uri, playlistId)) }

    override suspend fun getPlaylists() = runCatching {
        fetchAllPlaylistsCase.execute()
    }.map { list ->
        // TODO(jieyi): 5/4/21 Create a mapper for [SimplePlaylistEntity].
        list.map { SimplePlaylistEntity(it.id, it.name, it.songIds, it.coverUrl) }
    }

    override suspend fun createPlaylist(name: String): Result<Boolean> = runCatching {
        addPlaylistCase.execute(AddPlaylistReq(PlayListEntity(name = name)))
    }

    override suspend fun updateSongWithFavorite(song: SimpleTrackEntity, isFavorite: Boolean): Boolean {
        updateSongCase.execute(UpdateSongReq(song, isFavorite = isFavorite))
        return true
    }
}

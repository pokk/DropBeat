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

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.core.domain.usecase.Usecase
import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.sync.domain.repositories.SyncRepo
import taiwan.no.one.sync.domain.repositories.TimeRepo

internal class UpdatePlaylistsOneShotCase(
    private val syncRepo: SyncRepo,
    private val timestampRepo: TimeRepo,
) : OneShotUsecase<Boolean, UpdatePlaylistsRequest>() {
    override suspend fun acquireCase(parameter: UpdatePlaylistsRequest?) = parameter.ensure {
        val currentTime = Clock.System.now()
        val lastSyncedTime = timestampRepo.fetchSyncStamp()
        // The first, adding songs to the remote if a song without ref path.
        val refOfSongs = songs.map {
            it.map { track ->
                // If the track has refPath, just return it. Otherwise, add the song to the remote.
                track.refPath.takeIf(String::isNotEmpty) ?: syncRepo.addSong(track).apply { track.refPath = this }
            }
        }.onEachIndexed { index, refOfSongs -> playlists[index].refOfSongs = refOfSongs }

        // Get all playlists from the remote server.
        val remotePlaylists =
            syncRepo.fetchPlaylists(userInfo).associateBy(SimplePlaylistEntity::refPath).toMutableMap()
        // Local playlist
        syncFromLocalToRemote(playlists, currentTime, refOfSongs, remotePlaylists)
        // Attach all playlists to the account.
        val refOfPlaylists = playlists.map(SimplePlaylistEntity::refPath)
        syncRepo.addPlaylistRefToAccount(userInfo, refOfPlaylists)
        // The remote has a playlist but the local doesn't have.
        syncFromRemoteToLocal(remotePlaylists)

        // Update the local sync time.
        timestampRepo.updateSyncStamp(currentTime.toEpochMilliseconds())
    }

    private suspend fun syncFromLocalToRemote(
        playlists: List<SimplePlaylistEntity>,
        currentTime: Instant,
        refOfSongs: List<List<String>>,
        remotePlaylists: MutableMap<String, SimplePlaylistEntity>,
    ) {
        playlists.forEachIndexed { index, playlist ->
            if (playlist.refPath.isEmpty()) {
                // Create a new playlist from the local to the remote.
                createOrUpdatePlaylistProcess(playlist, currentTime, refOfSongs[index])
            }
            else {
                // Sync time is bigger means newer, and remove the synced playlist after it finished.
                val remotePlaylist = remotePlaylists.remove(playlist.refPath) ?: return@forEachIndexed
                // The remote playlist is newer than the local's.
                when {
                    // The situation is that stays the same device and syncs it all the time.
                    remotePlaylist.syncedStamp == playlist.syncedStamp -> {
                        // only situation (update time > remote sync time)
                        val playlistEpoch = playlist.updatedAt.toEpochMilliseconds()
                        if (playlistEpoch > remotePlaylist.syncedStamp) {
                            createOrUpdatePlaylistProcess(playlist, currentTime, refOfSongs[index])
                        }
                    }
                    // The situation is that other devices synced.
                    remotePlaylist.syncedStamp > playlist.syncedStamp -> {
                        // TODO(jieyi): 6/6/21 change the playlist.name too
                        //                        playlist.name = remotePlaylist.name
                        playlist.refOfSongs = remotePlaylist.refOfSongs
                    }
                    remotePlaylist.syncedStamp < playlist.syncedStamp ->
                        createOrUpdatePlaylistProcess(playlist, currentTime, refOfSongs[index])
                }
            }
        }
    }

    private fun syncFromRemoteToLocal(remotePlaylists: MutableMap<String, SimplePlaylistEntity>) {
        // TODO(jieyi): 6/8/21 Add the case when only the remote has.
        remotePlaylists.forEach { (ref, song) ->
            println("=================================================")
            println(ref)
            println("=================================================")
        }
    }

    private suspend fun createOrUpdatePlaylistProcess(
        playlist: SimplePlaylistEntity,
        currentTime: Instant,
        refOfSongs: List<String>,
    ): Boolean {
        // Update the sync time.
        playlist.syncedStamp = currentTime.toEpochMilliseconds()
        // If the playlist doesn't have [refPath], create a new playlist. Otherwise,just update
        // the playlist on the remote side.
        if (playlist.refPath.isBlank()) {
            syncRepo.addPlaylist(playlist).apply { playlist.refPath = this }
        }
        else {
            syncRepo.updatePlaylist(playlist)
        }
        return syncRepo.addSongRefToPlaylist(playlist.refPath, refOfSongs)
    }
}

data class UpdatePlaylistsRequest(
    val userInfo: UserInfoEntity,
    val playlists: List<SimplePlaylistEntity>,
    val songs: List<List<SimpleTrackEntity>>,
) : Usecase.RequestValues

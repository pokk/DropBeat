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

package taiwan.no.one.feat.library.domain.usecases

import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.core.exceptions.NotFoundException
import taiwan.no.one.dropbeat.core.PlaylistConstant
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.data.mappers.EntityMapper
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class UpdateSongOneShotCase(
    private val playlistRepo: PlaylistRepo,
    private val songRepo: SongRepo,
) : UpdateSongCase() {
    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        if (song == null && songId == null) return@ensure false
        if (song != null && isFavorite != null) {
            // If there is no song id, will go to get and update process. The case will
            // happen on as the following below
            // 1. Local database doesn't have it.
            // 2. Do this operation which is not on the favorite playlist screen.
            if (song.id == 0) {
                updateSongAndPlaylistProcess(song, isFavorite)
            }
            else {
                updateSongAndPlaylistProcess(song.id, isFavorite)
            }
        }
        else if (songId != null && isFavorite != null) {
            updateSongAndPlaylistProcess(songId, isFavorite)
        }
        // Simply update the song entity.
        else if (song != null && isFavorite == null) {
            songRepo.updateMusic(EntityMapper.simpleToSongEntity(song))
        }
        true
    }

    private suspend fun updateSongAndPlaylistProcess(entity: SimpleTrackEntity, isFavorite: Boolean) {
        val addedSong = try {
            songRepo.run {
                getMusic(entity.uri).apply { updateMusic(copy(isFavorite = isFavorite)) }
            }
        }
        catch (e: NotFoundException) {
            // When the song is not in our playlist, will add.
            songRepo.run {
                addMusic(EntityMapper.simpleToSongEntity(entity.copy(isFavorite = isFavorite)))
                getMusic(entity.uri)
            }
        }
        updateFavoritePlaylist(addedSong, isFavorite)
    }

    private suspend fun updateSongAndPlaylistProcess(songId: Int, isFavorite: Boolean) {
        songRepo.updateMusic(songId, isFavorite)
        val song = songRepo.getMusic(songId)
        updateFavoritePlaylist(song, isFavorite)
    }

    private suspend fun updateFavoritePlaylist(song: SongEntity, isFavorite: Boolean) {
        if (isFavorite) {
            // Add into the favorite playlist.
            playlistRepo.addMusic(song, PlaylistConstant.FAVORITE)
        }
        else {
            // Remove the song from the favorite playlist.
            playlistRepo.deleteMusic(song.id, PlaylistConstant.FAVORITE)
        }
    }

    internal data class Request(
        val song: SimpleTrackEntity? = null,
        val songId: Int? = null,
        val isFavorite: Boolean? = null,
    ) : RequestValues
}

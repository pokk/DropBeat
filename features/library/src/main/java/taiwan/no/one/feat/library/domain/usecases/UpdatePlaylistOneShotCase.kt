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
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class UpdatePlaylistOneShotCase(
    private val playlistRepository: PlaylistRepo,
    private val songRepository: SongRepo,
) : UpdatePlaylistCase() {
    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        val oldPlaylist = playlistRepository.fetchPlaylist(playlistId!!)
        val newPlaylist = if (songIds == null && songsPaths == null) {
            oldPlaylist.copy(name = name ?: oldPlaylist.name, refPath = refPath)
        }
        else {
            val set = oldPlaylist.songIds.toSet()
            val songIds = getSongIds(this)
            val ids = when {
                // Add new song ids.
                isAddSongs == true && isRemoveSongs != true -> oldPlaylist.songIds.toMutableList()
                    .apply {
                        // Check the id has already been in the list.
                        songIds.forEach {
                            // If the song id is not in the list, add id into the new list.
                            if (!set.contains(it)) {
                                add(it)
                            }
                        }
                    }
                // Remove song ids.
                isAddSongs != true && isRemoveSongs == true -> oldPlaylist.songIds.toMutableList()
                    .apply {
                        // Check the id has already been in the list.
                        songIds.forEach {
                            // If the song id is in the list, remove id into the new list.
                            if (set.contains(it)) {
                                remove(it)
                            }
                        }
                    }
                else -> oldPlaylist.songIds
            }
            oldPlaylist.copy(
                name = name ?: oldPlaylist.name,
                songIds = ids,
                count = ids.size,
                refPath = refPath,
            )
        }
        playlistRepository.updatePlaylist(
            if (syncStamp != null) newPlaylist.copy(syncedAt = syncStamp) else newPlaylist
        )
        true
    }

    private suspend fun getSongIds(request: UpdatePlaylistReq): List<Int> {
        if (request.songIds != null) {
            return request.songIds
        }
        return request.songsPaths?.map { songRepository.fetchMusic(null, it).id } ?: throw NullPointerException()
    }

    internal data class Request(
        val playlistId: Int?,
        val name: String? = null,
        val songIds: List<Int>? = null,
        val songsPaths: List<String>? = null,
        val isAddSongs: Boolean? = null,
        val isRemoveSongs: Boolean? = null,
        val refPath: String = DEFAULT_STR,
        val syncStamp: Long? = null,
    ) : RequestValues
}

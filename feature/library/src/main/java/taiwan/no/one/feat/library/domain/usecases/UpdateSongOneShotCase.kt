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

package taiwan.no.one.feat.library.domain.usecases

import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class UpdateSongOneShotCase(
    private val playlistRepo: PlaylistRepo,
    private val songRepo: SongRepo,
) : UpdateSongCase() {
    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        if (songId != null && isFavorite != null) {
            songRepo.updateMusic(songId, isFavorite)
            val song = songRepo.getMusic(songId)
            if (isFavorite) {
                // Add into the favorite playlist.
                playlistRepo.addMusic(song, 2)
            }
            else {
                // Remove the song from the favorite playlist.
                playlistRepo.deleteMusic(songId, 2)
            }
        }
        true
    }

    internal class Request(
        val song: SimpleTrackEntity? = null,
        val songId: Int? = null,
        val isFavorite: Boolean? = null,
    ) : RequestValues
}

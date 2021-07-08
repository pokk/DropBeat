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
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity
import taiwan.no.one.feat.library.domain.repositories.LyricRepo
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class FetchLyricOneShotCase(
    private val lyricRepo: LyricRepo,
    private val songRepo: SongRepo,
) : FetchLyricCase() {
    /**
     * @param parameter Request? song's id.
     * @return String The whole lyric text.
     * @see <a href="https://whimsical.com/lyric-flow-WvoNC1qMDQUv6WNAK3hCDC">Fetch Lyric Flow Diagram</a>
     */
    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        val song = songRepo.fetchMusic(songId)
        val uri = if (song.lyricLocalUri.isEmpty()) obtainLyricFromRemote(song) else song.lyricLocalUri
        // 4. Get the whole text from the local storage.
        lyricRepo.fetchStorageLyric(uri).toString()
    }

    /**
     * Fetch the remote lyric process when the lyric is not kept in the local storage.
     *
     * @param song SongEntity
     * @return String
     */
    private suspend fun obtainLyricFromRemote(song: LibraryEntity.SongEntity): String {
        // 1. Fetch the lyric from the remote server.
        val bytes = lyricRepo.fetchLyric(song.lyricUrl)
        // 2. Save the lyric to the local storage
        val filename = song.lyricLocalUri.split("/").last()
        val filePath = lyricRepo.addStorageLyric(bytes, filename)
        // 3. Update the database information of the song.
        songRepo.updateMusic(song.copy(localUri = filePath))
        return filePath
    }

    data class Request(val songId: Int) : RequestValues
}

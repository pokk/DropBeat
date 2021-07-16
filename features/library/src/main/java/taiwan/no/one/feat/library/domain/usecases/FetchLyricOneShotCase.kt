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
    private val fakeData = """
[ti:One Life]
[ar:Helena Paparizou]
[al:One Life]
[by:]
[offset:0]
[00:00.92]One Life - Helena Paparizou
[00:06.20]Here i sit and cry for nothing
[00:08.71]Thinking about true love
[00:10.02]That changes everything everything
[00:16.94]What we had was false romancing
[00:18.48]
[00:19.55]Though i tried a million times
[00:21.77]To let you in let you in
[00:27.87]Every time i'm felling sad
[00:32.20]I know it must be you
[00:38.51]One life and what you gonna do about it
[00:41.88]Two hearts that love was blinded
[00:45.31]Count three times that we tried and what for
[00:50.07]One life and that's about it
[00:52.63]To bad we could not make it
[00:56.07]And this time you're not mine anymore
[01:00.67]
[01:10.99]Though i try to be so patient
[01:13.63]But it still keeps hurting me
[01:15.38]So deep inside deep inside
[01:21.64]Here i sit alone in silence
[01:24.26]Wishing you to lay again here
[01:26.37]By my side by my side
[01:32.16]Every time my phone don't ring
[01:36.87]I knowing must be you
[01:43.26]One life and what you gonna do about it
[01:46.54]Two hearts that love was blinded
[01:48.64]
[01:50.03]Count three times that we tried and what for
[01:54.76]One life and that's about it
[01:57.42]To bad we could not make it
[02:00.86]And this time you're not mine anymore
[02:04.75]One life two hearts
[02:07.37]Who counts these stars
[02:10.17]There's no love anymore
[02:15.56]One life two hearts
[02:18.14]Who counts these stars
[02:20.66]There's no love anymore
[02:24.76]
[02:26.45]Every time my phone don't ring
[02:30.85]I knowing must be you
[02:40.00]One life and what you gonna do about it
[02:43.16]Two hearts that love was blinded
[02:46.72]Count three times that we tried and what for
[02:50.98]One life and that's about it
[02:54.02]To bad we could not make it
[02:57.44]And this time you're not mine anymore
[03:01.46]One life and what you gonna do about it
[03:04.86]Two hearts that love was blinded
[03:08.20]Count three times that we tried and what for
[03:12.86]One life and that's about it
[03:15.55]To bad we could not make it
[03:19.02]And this time you're not mine anymore
[03:23.47]
                """.trimIndent()

    /**
     * @param parameter Request? song's id.
     * @return String The whole lyric text.
     * @see <a href="https://whimsical.com/lyric-flow-WvoNC1qMDQUv6WNAK3hCDC">Fetch Lyric Flow Diagram</a>
     */
    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        return@ensure fakeData
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

    internal data class Request(val songId: Int) : RequestValues
}

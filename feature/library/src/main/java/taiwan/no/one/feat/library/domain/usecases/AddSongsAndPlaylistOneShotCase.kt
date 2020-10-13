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

import com.devrapid.kotlinknifer.loge
import com.google.gson.Gson
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance
import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.core.exceptions.internet.InternetException.ParameterNotMatchException
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class AddSongsAndPlaylistOneShotCase(
    private val songRepository: SongRepo,
    private val playlistRepository: PlaylistRepo,
) : AddSongsAndPlaylistCase(), DIAware {
    override val di by di(DropBeatApp.appContext)

    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        val gson by instance<Gson>()
        val list = try {
            gson.fromJson(songsStream, Array<SongEntity>::class.java).toList()
        }
        catch (e: Exception) {
            loge(e)
            return@ensure false
        }
        try {
            songRepository.addMusics(list)
        }
        catch (e: Exception) {
            // The error happens because the music has been in the database.
        }
        finally {
            list.forEach {
                val track = when {
                    it.uri.isNotEmpty() -> songRepository.getMusic(remoteUri = it.uri)
                    it.localUri.isNotEmpty() -> songRepository.getMusic(localUri = it.localUri)
                    else -> throw ParameterNotMatchException()
                }
                playlistRepository.addMusicToPlaylist(track, playlistId)
            }
        }
        true
    }

    internal data class Request(
        val songsStream: String? = null,
        val playlistId: Int,
    ) : RequestValues
}

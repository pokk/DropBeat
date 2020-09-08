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
import com.google.gson.Gson
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsReq
import java.io.BufferedReader

@AutoService(LibraryMethodsProvider::class)
class MethodsProvider : LibraryMethodsProvider, DIAware {
    /**
     * A DI Aware class must be within reach of a [DI] object.
     */
    override val di by lazy { (DropBeatApp.appContext as DropBeatApp).di }
    private val gson by instance<Gson>()
    private val addPlaylistCase by instance<AddPlaylistCase>()
    private val addSongsCase by instance<AddSongsCase>()

    override suspend fun createDefaultPlaylists(): Boolean {
        // TODO(jieyi): 9/8/20 it should be only provide a method.
        val json = DropBeatApp.appContext.assets.open("json/default_playlist.json").use {
            it.bufferedReader().use(BufferedReader::readText)
        }
        val list = gson.fromJson(json, Array<PlayListEntity>::class.java).toList()
        val ids = list.map(PlayListEntity::id)
        val names = list.map(PlayListEntity::name)
        ids.zip(names)
            .map { (id, name) -> PlayListEntity(id, name) }
            .forEach {
                addPlaylistCase.execute(AddPlaylistReq(it))
            }
        return true
    }

    override suspend fun addSongToPlaylist(songId: Int, playlistId: Int): Boolean {
        TODO()
    }

    override suspend fun downloadTrack(songsStream: String): Boolean {
        addSongsCase.execute(AddSongsReq(songsStream = songsStream))
        return true
    }
}

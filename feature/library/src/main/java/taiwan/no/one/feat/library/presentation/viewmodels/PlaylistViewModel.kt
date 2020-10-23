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

package taiwan.no.one.feat.library.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsReq
import taiwan.no.one.feat.library.domain.usecases.FetchPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistReq
import taiwan.no.one.feat.library.domain.usecases.UpdateSongCase
import taiwan.no.one.feat.library.domain.usecases.UpdateSongReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class PlaylistViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val addPlaylistCase by instance<AddPlaylistCase>()
    private val fetchPlaylistCase by instance<FetchPlaylistCase>()
    private val updatePlaylistCase by instance<UpdatePlaylistCase>()
    private val addSongsCase by instance<AddSongsCase>()
    private val updateSongCase by instance<UpdateSongCase>()
    private val _result by lazy { ResultLiveData<Boolean>() }
    val result = _result.toLiveData()
    private val _playlist by lazy { ResultLiveData<PlayListEntity>() }
    val playlist = _playlist.toLiveData()

    fun getSongs(playlistId: Int) = viewModelScope.launch {
        _playlist.value = runCatching { fetchPlaylistCase.execute(FetchPlaylistReq(playlistId)) }
    }

    fun getPlaylist(playlistId: Int) = getSongs(playlistId)

    fun createPlaylist(playlist: PlayListEntity) = viewModelScope.launch {
        _result.value = runCatching { addPlaylistCase.execute(AddPlaylistReq(playlist)) }
    }

    fun updatePlaylist(playlistId: Int, playlistName: String) = viewModelScope.launch {
        _result.value = runCatching { updatePlaylistCase.execute(UpdatePlaylistReq(playlistId, playlistName)) }
    }

    fun createSongs(songs: List<SongEntity>) = viewModelScope.launch {
        _result.value = runCatching { addSongsCase.execute(AddSongsReq(songs)) }
    }

    fun updateSong(songId: Int, isFavorite: Boolean) = viewModelScope.launch {
        _result.value = runCatching { updateSongCase.execute(UpdateSongReq(songId = songId, isFavorite = isFavorite)) }
    }
}

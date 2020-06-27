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

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taiwan.no.one.core.presentation.viewmodel.BehindViewModel
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.AddSongReq
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.FetchPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistCase
import taiwan.no.one.ktx.livedata.toLiveData

internal class PlaylistViewModel(
    private val addPlaylistCase: AddPlaylistCase,
    private val fetchPlaylistCase: FetchPlaylistCase,
    private val updatePlaylistCase: UpdatePlaylistCase,
    private val addSongsCase: AddSongsCase,
) : BehindViewModel() {
    val playlist = liveData(viewModelScope.coroutineContext) {
        emit(fetchPlaylistCase.execute())
    }
    private val _resPlaylist by lazy { ResultLiveData<Boolean>() }
    val resPlaylist = _resPlaylist.toLiveData()

    fun createPlaylist(playlist: PlayListEntity) = viewModelScope.launch {
        _resPlaylist.value = addPlaylistCase.execute(AddPlaylistReq(playlist))
    }

    fun createSongs(songs: List<SongEntity>) = viewModelScope.launch {
        _resPlaylist.value = addSongsCase.execute(AddSongReq(songs))
    }
}
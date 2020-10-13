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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.domain.usecases.FetchAllPlaylistsCase
import taiwan.no.one.ktx.livedata.toLiveData

internal class MyHomeViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val fetchAllPlaylistsCase by instance<FetchAllPlaylistsCase>()

    private val _playlists by lazy { ResultLiveData<List<PlayListEntity>>() }
    val playlists = _playlists.toLiveData()
    private val _favorites by lazy { MutableLiveData<PlayListEntity>() }
    val favorites = _favorites.toLiveData()
    private val _downloaded by lazy { MutableLiveData<PlayListEntity>() }
    val downloaded = _downloaded.toLiveData()
    private val _histories by lazy { MutableLiveData<PlayListEntity>() }
    val histories = _histories.toLiveData()

    fun getAllPlaylists() = viewModelScope.launch {
        _playlists.value = runCatching { fetchAllPlaylistsCase.execute() }
    }

    fun extractMainPlaylist(list: List<PlayListEntity>) = launchBehind {
        _downloaded.postValue(list[0])
        _favorites.postValue(list[1])
    }
}

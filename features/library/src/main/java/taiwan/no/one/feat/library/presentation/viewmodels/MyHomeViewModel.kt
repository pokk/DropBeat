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

package taiwan.no.one.feat.library.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.domain.usecases.AddAccountCase
import taiwan.no.one.dropbeat.domain.usecases.AddAccountReq
import taiwan.no.one.dropbeat.domain.usecases.AddPlaylistCase
import taiwan.no.one.dropbeat.domain.usecases.AddPlaylistReq
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.mappers.EntityMapper
import taiwan.no.one.feat.library.domain.usecases.FetchAllPlaylistsCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistReq
import taiwan.no.one.feat.library.domain.usecases.UpdateSongCase
import taiwan.no.one.feat.library.domain.usecases.UpdateSongReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class MyHomeViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val fetchAllPlaylistsCase by instance<FetchAllPlaylistsCase>()
    private val addAccountCase by instance<AddAccountCase>()
    private val addPlaylistCase by instance<AddPlaylistCase>()
    private val updatePlaylistCase by instance<UpdatePlaylistCase>()
    private val updateSongCase by instance<UpdateSongCase>()
    private val libraryProvider by instance<LibraryMethodsProvider>()
    private val _playlists by lazy { ResultLiveData<List<PlayListEntity>>() }
    val playlists get() = _playlists.toLiveData()
    private val _favorites by lazy { MutableLiveData<PlayListEntity>() }
    val favorites get() = _favorites.toLiveData()
    private val _downloaded by lazy { MutableLiveData<PlayListEntity>() }
    val downloaded get() = _downloaded.toLiveData()
    private val _histories by lazy { MutableLiveData<PlayListEntity>() }
    val histories get() = _histories.toLiveData()
    private val _resultOfFavorite by lazy { MutableLiveData<Boolean>() }
    val resultOfFavorite get() = _resultOfFavorite.toLiveData()
    private val _resultOfAddAccount by lazy { MutableLiveData<Boolean>() }
    val resultOfAddAccount get() = _resultOfAddAccount.toLiveData()

    fun getAllPlaylists() = viewModelScope.launch {
        _playlists.value = runCatching { fetchAllPlaylistsCase.execute() }
    }

    fun extractMainPlaylist(list: List<PlayListEntity>) = launchBehind {
        _downloaded.postValue(list[0])
        _favorites.postValue(list[1])
    }

    fun updateSong(song: SimpleTrackEntity, isFavorite: Boolean) = viewModelScope.launch {
        _resultOfFavorite.value = libraryProvider.updateSongWithFavorite(song, isFavorite)
    }

    fun createAccountOnRemote(entity: UserInfoEntity) = viewModelScope.launch {
        _resultOfAddAccount.value = addAccountCase.execute(AddAccountReq(entity))
    }

    fun createPlaylistOnRemote(userInfo: UserInfoEntity) = viewModelScope.launch {
        val playlists = _playlists.value?.getOrNull().orEmpty()
        // NOTE(jieyi): Will have a list of pair <playlist <-> songs>.
        val simplePlaylists = playlists.map(EntityMapper::playlistToSimplePlaylistEntity)
        val songsOfPlaylists = playlists.map { it.songs.map(EntityMapper::songToSimpleEntity) }
        runCatching { addPlaylistCase.execute(AddPlaylistReq(userInfo, simplePlaylists, songsOfPlaylists)) }.onSuccess {
            // Update the local database.
            simplePlaylists.filter { it.refPath.isNotEmpty() } // for playlists
                .forEach { updatePlaylistCase.execute(UpdatePlaylistReq(it.id, refPath = it.refPath)) }
            // for songs
            simplePlaylists.forEachIndexed { index, playlist ->
                playlist.refOfSongs.zip(playlists[index].songs).forEach { (ref, song) ->
                    val track = EntityMapper.songToSimpleEntity(song.copy(isFavorite = song.isFavorite, refPath = ref))
                    updateSongCase.execute(UpdateSongReq(track))
                }
            }
            // Refresh the view's data.
            getAllPlaylists()
        }
    }

    fun syncPlaylistOnRemote(userInfo: UserInfoEntity) = viewModelScope.launch {
    }
}

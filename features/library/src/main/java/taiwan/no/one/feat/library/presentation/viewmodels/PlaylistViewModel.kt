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
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.AppResDrawable
import taiwan.no.one.dropbeat.core.PlaylistConstant
import taiwan.no.one.dropbeat.core.helpers.ResourceHelper
import taiwan.no.one.dropbeat.core.utils.StringUtil
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.provider.RankingMethodsProvider
import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistReq
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsReq
import taiwan.no.one.feat.library.domain.usecases.FetchIsInThePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchIsInThePlaylistReq
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
    private val fetchIsInThePlaylistCase by instance<FetchIsInThePlaylistCase>()
    private val rankingMethodsProvider by instance<RankingMethodsProvider>()
    private val _result by lazy { ResultLiveData<Boolean>() }
    val result get() = _result.toLiveData()
    private val _resultOfFavorite by lazy { ResultLiveData<Boolean>() }
    val resultOfFavorite get() = _resultOfFavorite.toLiveData()
    private val _playlist by lazy { ResultLiveData<PlayListEntity>() }
    val playlist get() = _playlist.toLiveData()
    private val _playlistDuration by lazy { MutableLiveData<String>() }
    val playlistDuration get() = _playlistDuration.toLiveData()
    private val _tracks by lazy { ResultLiveData<List<SimpleTrackEntity>>() }
    val tracks get() = _tracks.toLiveData()

    fun getSongs(playlistId: Int) = viewModelScope.launch {
        _playlist.value = kotlin.runCatching { fetchPlaylistCase.execute(FetchPlaylistReq(playlistId)) }
    }

    fun getPlaylist(playlistId: Int) = getSongs(playlistId)

    @WorkerThread
    fun getRankSongs(rankId: Int) = launchBehind {
        _tracks.postValue(rankingMethodsProvider.getRankingSongs(rankId))
    }

    @WorkerThread
    fun attachFavoriteFlag(songs: List<SimpleTrackEntity>) = launchBehind {
        val res = kotlin.runCatching {
            songs.onEach {
                it.isFavorite = fetchIsInThePlaylistCase.execute(
                    FetchIsInThePlaylistReq(null, it.uri, PlaylistConstant.FAVORITE)
                )
            }
        }
        _tracks.postValue(res)
    }

    @WorkerThread
    fun createPlaylist(playlist: SimplePlaylistEntity?, name: String) = launchBehind(Dispatchers.Default) {
        _result.postValue(kotlin.runCatching {
            val param = playlist?.let {
                PlayListEntity(
                    name = name,
                    songIds = it.songIds,
                    count = it.songIds.size,
                    coverUrl = ResourceHelper.getUriForDrawableResource(AppResDrawable.bg_default).toString()
                )
            } ?: PlayListEntity(
                name = name,
                coverUrl = ResourceHelper.getUriForDrawableResource(AppResDrawable.bg_default).toString()
            )
            addPlaylistCase.execute(AddPlaylistReq(param))
        })
    }

    fun updatePlaylist(playlistId: Int, playlistName: String) = viewModelScope.launch {
        _result.value = kotlin.runCatching { updatePlaylistCase.execute(UpdatePlaylistReq(playlistId, playlistName)) }
    }

    fun createSongs(songs: List<SongEntity>) = viewModelScope.launch {
        _result.value = kotlin.runCatching { addSongsCase.execute(AddSongsReq(songs)) }
    }

    fun updateSong(songId: Int, isFavorite: Boolean) = viewModelScope.launch {
        _resultOfFavorite.value =
            kotlin.runCatching { updateSongCase.execute(UpdateSongReq(songId = songId, isFavorite = isFavorite)) }
    }

    fun updateSong(song: SimpleTrackEntity, isFavorite: Boolean) = viewModelScope.launch {
        _resultOfFavorite.value =
            kotlin.runCatching { updateSongCase.execute(UpdateSongReq(song = song, isFavorite = isFavorite)) }
    }

    @WorkerThread
    fun cumulateDuration(songs: List<SimpleTrackEntity>) = launchBehind(Dispatchers.Default) {
        val duration = songs.fold(0) { acc, song -> acc + song.duration }
        // Set the visibility for this fragment.
        _playlistDuration.postValue(
            "${songs.size} Songs・${StringUtil.buildDurationToTime(duration.toLong())}・30 mins ago played"
        )
    }
}

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

package taiwan.no.one.feat.explore.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.data.entities.SimplePlaylistEntity
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity.TracksEntity
import taiwan.no.one.feat.explore.domain.usecases.ArtistWithMoreDetailEntities
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopArtistCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopArtistReq
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTagCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTagReq
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTrackReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class ExploreViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val fetchChartTopTagCase by instance<FetchChartTopTagCase>()
    private val fetchChartTopTrackCase by instance<FetchChartTopTrackCase>()
    private val fetchChartTopArtistCase by instance<FetchChartTopArtistCase>()
    private val libraryProvider by instance<LibraryMethodsProvider>()
    private val _playlists by lazy { ResultLiveData<List<SimplePlaylistEntity>>() }
    val playlists get() = _playlists.toLiveData()
    private val _topTracks by lazy { ResultLiveData<TracksEntity>() }
    val topTracks get() = _topTracks.toLiveData()
    private val _topArtists by lazy { ResultLiveData<ArtistWithMoreDetailEntities>() }
    val topArtists get() = _topArtists.toLiveData()
    val topTags = liveData {
        emit(runCatching { fetchChartTopTagCase.execute(FetchChartTopTagReq(1, 10)) })
    }
    private val _resultOfFavorite by lazy { MutableLiveData<Boolean>() }
    val resultOfFavorite get() = _resultOfFavorite.toLiveData()

    fun getPlaylists() = viewModelScope.launch {
        _playlists.value = libraryProvider.getPlaylists()
    }

    fun getTopTracks() = launchBehind {
        _topTracks.postValue(runCatching {
            fetchChartTopTrackCase.execute(FetchChartTopTrackReq(1, 10, 4)).apply {
                tracks.onEach {
                    val url = it.url ?: return@onEach
                    val isFavorite = try {
                        libraryProvider.isFavoriteTrack(url, 2)
                    }
                    catch (e: Exception) {
                        return@onEach
                    }
                    it.isFavorite = isFavorite.getOrNull()
                }
            }
        })
    }

    fun getTopArtists() = launchBehind {
        _topArtists.postValue(runCatching {
            fetchChartTopArtistCase.execute(FetchChartTopArtistReq(1, 10, 4)).onEach {
                val url = it.second?.popularTrackThisWeek?.url ?: return@onEach
                val isFavorite = try {
                    libraryProvider.isFavoriteTrack(url, 2)
                }
                catch (e: Exception) {
                    return@onEach
                }
                it.second?.popularTrackThisWeek?.isFavorite = isFavorite.getOrNull()
            }
        })
    }

    fun updateSong(song: SimpleTrackEntity, isFavorite: Boolean) = viewModelScope.launch {
        _resultOfFavorite.value = libraryProvider.updateSongWithFavorite(song, isFavorite)
    }
}

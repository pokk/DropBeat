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
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.PlaylistConstant
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.provider.ExploreMethodsProvider
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.entity.SimpleArtistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.ktx.livedata.toLiveData

internal class SongOfArtistViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val exploreMethodsProvider by instance<ExploreMethodsProvider>()
    private val libraryMethodsProvider by instance<LibraryMethodsProvider>()
    private val _artistInfo by lazy { ResultLiveData<SimpleArtistEntity>() }
    val artistInfo get() = _artistInfo.toLiveData()
    val artistTrack
        get() = _artistInfo.switchMap {
            liveData { emit(attachFavorite(it.getOrNull()?.topTracks.orEmpty())) }
        }
    val artistAlbums
        get() = _artistInfo.map { it.getOrNull()?.topAlbums.orEmpty() }

    @UiThread
    fun getArtistInfo(name: String) = launchBehind {
        _artistInfo.postValue(runCatching { exploreMethodsProvider.getArticleInfo(name) })
    }

    @WorkerThread
    private suspend fun attachFavorite(tracks: List<SimpleTrackEntity>) = tracks.onEach {
        val isFavorite = try {
            libraryMethodsProvider.isFavoriteTrack(it.uri, PlaylistConstant.FAVORITE).getOrNull() ?: false
        }
        catch (e: Exception) {
            false
        }
        it.isFavorite = isFavorite
    }
}

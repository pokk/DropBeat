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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import org.kodein.di.instance
import taiwan.no.one.dropbeat.core.viewmodel.BehindSavedStateViewModel
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopArtistCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopArtistReq
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTagCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTagReq
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTrackReq

internal class ExploreViewModel(
    override val handle: SavedStateHandle,
) : BehindSavedStateViewModel() {
    private val fetchChartTopTagCase by instance<FetchChartTopTagCase>()
    private val fetchChartTopTrackCase by instance<FetchChartTopTrackCase>()
    private val fetchChartTopArtistCase by instance<FetchChartTopArtistCase>()
    private val libraryProvider by instance<LibraryMethodsProvider>()
    val playlists = liveData { emit(libraryProvider.getPlaylists()) }

    val topTags = liveData {
        emit(fetchChartTopTagCase.execute(FetchChartTopTagReq(0, 10)))
    }
    val topTracks = liveData {
        emit(fetchChartTopTrackCase.execute(FetchChartTopTrackReq(0, 10, 4)))
    }
    val topArtists = liveData {
        emit(fetchChartTopArtistCase.execute(FetchChartTopArtistReq(0, 10, 4)))
    }
}

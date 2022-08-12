/*
 * MIT License
 *
 * Copyright (c) 2022 Jieyi
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

package taiwan.no.one.feat.ranking.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.feat.ranking.data.entities.remote.CommonMusicEntity.NetworkSong
import taiwan.no.one.feat.ranking.domain.usecases.FetchDetailOfRankingsCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchMusicRankCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchMusicRankReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class RankViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val fetchDetailOfRankingsCase by instance<FetchDetailOfRankingsCase>()
    private val fetchMusicRankCase by instance<FetchMusicRankCase>()
    private val libraryProvider by instance<LibraryMethodsProvider>()

    val rankings = liveData { emit(kotlin.runCatching { fetchDetailOfRankingsCase.execute() }) }
    private val _musics by lazy { ResultLiveData<List<NetworkSong>>() }
    val musics get() = _musics.toLiveData()

    fun getMusics(rankId: String) = viewModelScope.launch {
        _musics.value = kotlin.runCatching { fetchMusicRankCase.execute(FetchMusicRankReq(rankId)) }
    }
}

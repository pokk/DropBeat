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

package taiwan.no.one.feat.search.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.feat.search.data.entities.remote.CommonMusicEntity.SongEntity
import taiwan.no.one.feat.search.domain.usecases.AddOrUpdateHistoryCase
import taiwan.no.one.feat.search.domain.usecases.AddOrUpdateHistoryReq
import taiwan.no.one.feat.search.domain.usecases.FetchMusicCase
import taiwan.no.one.feat.search.domain.usecases.FetchMusicReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class ResultViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(
    application) {
    private val fetchMusicCase by instance<FetchMusicCase>()
    private val addOrUpdateHistoryCase by instance<AddOrUpdateHistoryCase>()
    private val _musics by lazy { ResultLiveData<List<SongEntity>>() }
    val musics = _musics.toLiveData()
    private val _addOrUpdateResult by lazy { ResultLiveData<Boolean>() }
    val addOrUpdateResult = _addOrUpdateResult.toLiveData()
    private var curPage = 1
    var curKeyword = ""
        private set

    fun search(keyword: String? = null, page: Int = -1) = viewModelScope.launch {
        if (keyword != null) {
            curKeyword = keyword
        }
        if (page > 0) {
            curPage = page
        }
        if (curKeyword.isBlank()) return@launch
        _musics.value = runCatching { fetchMusicCase.execute(FetchMusicReq(curKeyword, curPage)) }
    }

    fun add(keyword: String) = viewModelScope.launch {
        _addOrUpdateResult.value = runCatching { addOrUpdateHistoryCase.execute(AddOrUpdateHistoryReq(keyword)) }
    }

    fun goNextPage() = curPage++
}

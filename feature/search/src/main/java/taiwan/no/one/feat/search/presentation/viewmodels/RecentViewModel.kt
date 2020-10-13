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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.feat.search.data.entities.local.SearchHistoryEntity
import taiwan.no.one.feat.search.domain.usecases.AddOrUpdateHistoryCase
import taiwan.no.one.feat.search.domain.usecases.AddOrUpdateHistoryReq
import taiwan.no.one.feat.search.domain.usecases.DeleteHistoryCase
import taiwan.no.one.feat.search.domain.usecases.DeleteHistoryReq
import taiwan.no.one.feat.search.domain.usecases.FetchHistoryCase
import taiwan.no.one.feat.search.domain.usecases.FetchHistoryReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class RecentViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val fetchHistoryCase by instance<FetchHistoryCase>()
    private val addOrUpdateHistoryCase by instance<AddOrUpdateHistoryCase>()
    private val deleteHistoryCase by instance<DeleteHistoryCase>()
    private val _deleteResult by lazy { ResultLiveData<Boolean>() }
    private val _addOrUpdateResult by lazy { ResultLiveData<Boolean>() }
    val deleteResult = _deleteResult.toLiveData()
    val addOrUpdateResult = _addOrUpdateResult.toLiveData()
    val histories = fetchHistoryCase.execute(FetchHistoryReq(50)).asLiveData(viewModelScope.coroutineContext)

    fun delete(keyword: String?, entity: SearchHistoryEntity?) = viewModelScope.launch {
        _deleteResult.value = runCatching { deleteHistoryCase.execute(DeleteHistoryReq(keyword, entity)) }
    }

    fun add(keyword: String) = viewModelScope.launch {
        _addOrUpdateResult.value = runCatching { addOrUpdateHistoryCase.execute(AddOrUpdateHistoryReq(keyword)) }
    }
}

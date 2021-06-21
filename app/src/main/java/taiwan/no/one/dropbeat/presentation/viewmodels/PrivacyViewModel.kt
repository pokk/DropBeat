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

package taiwan.no.one.dropbeat.presentation.viewmodels

import android.app.Application
import androidx.annotation.UiThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.domain.usecases.FetchLoginInfoCase
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.ktx.livedata.toLiveData

class PrivacyViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    var shouldDisplaySyncDialog = false
    private val fetchLoginInfoCase by instance<FetchLoginInfoCase>()
    private val _userInfo by lazy { ResultLiveData<UserInfoEntity>() }
    val userInfo get() = _userInfo.toLiveData()

    @UiThread
    fun getUserInfo() = viewModelScope.launch {
        _userInfo.value = kotlin.runCatching { fetchLoginInfoCase.execute() }
    }

    @UiThread
    fun clearUseInfo() {
        _userInfo.value = Result.failure(NullPointerException("There is no user information."))
    }
}

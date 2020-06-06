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

package taiwan.no.one.feat.login.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taiwan.no.one.core.presentation.viewmodel.BehindViewModel
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.feat.login.data.entities.remote.UserInfoEntity
import taiwan.no.one.feat.login.data.remote.services.firebase.Credential
import taiwan.no.one.feat.login.domain.usecases.CreateUserCase
import taiwan.no.one.feat.login.domain.usecases.CreateUserReq
import taiwan.no.one.feat.login.domain.usecases.FetchLoginInfoCase
import taiwan.no.one.feat.login.domain.usecases.FetchLoginInfoReq
import taiwan.no.one.feat.login.domain.usecases.ModifyPasswordCase
import taiwan.no.one.feat.login.domain.usecases.ModifyPasswordReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class LoginViewModel(
    private val fetchLoginInfoCase: FetchLoginInfoCase,
    private val createUserCase: CreateUserCase,
    private val modifyPasswordCase: ModifyPasswordCase
) : BehindViewModel() {
    private val _userInfo by lazy { ResultLiveData<UserInfoEntity>() }
    val userInfo = _userInfo.toLiveData()
    private val _resetResp by lazy { ResultLiveData<Boolean>() }
    val resetResp = _resetResp.toLiveData()

    fun login(email: String, password: String) = viewModelScope.launch {
        _userInfo.value = fetchLoginInfoCase.execute(FetchLoginInfoReq(email, password))
    }

    fun login(credential: Credential) = viewModelScope.launch {
        _userInfo.value = fetchLoginInfoCase.execute(FetchLoginInfoReq(credential = credential))
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _userInfo.value = createUserCase.execute(CreateUserReq(email, password))
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _resetResp.value = modifyPasswordCase.execute(ModifyPasswordReq(email))
    }
}

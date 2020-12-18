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

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.data.entities.UserInfoEntity
import taiwan.no.one.feat.login.data.remote.services.firebase.Credential
import taiwan.no.one.feat.login.domain.usecases.CreateUserCase
import taiwan.no.one.feat.login.domain.usecases.CreateUserReq
import taiwan.no.one.feat.login.domain.usecases.LoginCase
import taiwan.no.one.feat.login.domain.usecases.LoginReq
import taiwan.no.one.feat.login.domain.usecases.ModifyPasswordCase
import taiwan.no.one.feat.login.domain.usecases.ModifyPasswordReq
import taiwan.no.one.ktx.livedata.toLiveData

internal class LoginViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val loginCase by instance<LoginCase>()
    private val createUserCase by instance<CreateUserCase>()
    private val modifyPasswordCase by instance<ModifyPasswordCase>()
    private val _userInfo by lazy { ResultLiveData<UserInfoEntity>() }
    val userInfo get() = _userInfo.toLiveData()
    private val _resetResp by lazy { ResultLiveData<Boolean>() }
    val resetResp get() = _resetResp.toLiveData()
    private val _isValidEmail by lazy { MutableLiveData<Boolean>() }
    val isValidEmail get() = _isValidEmail.toLiveData()

    fun login(email: String, password: String) = viewModelScope.launch {
        _userInfo.value = runCatching { loginCase.execute(LoginReq(email, password)) }
    }

    fun login(credential: Credential) = viewModelScope.launch {
        _userInfo.value = runCatching { loginCase.execute(LoginReq(credential = credential)) }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _userInfo.value = runCatching { createUserCase.execute(CreateUserReq(email, password)) }
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _resetResp.value = runCatching { modifyPasswordCase.execute(ModifyPasswordReq(email)) }
    }

    fun validEmailFormat(email: String) = viewModelScope.launch {
        _isValidEmail.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

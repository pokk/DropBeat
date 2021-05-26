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

package taiwan.no.one.feat.setting.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.provider.LoginMethodsProvider
import taiwan.no.one.feat.setting.domain.DomainModules
import taiwan.no.one.feat.setting.domain.usecases.AddAutoDisplayMvCase
import taiwan.no.one.feat.setting.domain.usecases.AddAutoDisplayMvReq
import taiwan.no.one.feat.setting.domain.usecases.AddLockScreenPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.AddLockScreenPlayerReq
import taiwan.no.one.feat.setting.domain.usecases.AddNotificationPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.AddNotificationPlayerReq
import taiwan.no.one.feat.setting.domain.usecases.AddPlayOfflineOnlyCase
import taiwan.no.one.feat.setting.domain.usecases.AddPlayOfflineOnlyReq
import taiwan.no.one.feat.setting.domain.usecases.AddSleepingTimerCase
import taiwan.no.one.feat.setting.domain.usecases.AddSleepingTimerReq
import taiwan.no.one.feat.setting.domain.usecases.FetchAutoDisplayMvCase
import taiwan.no.one.feat.setting.domain.usecases.FetchLockScreenPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchNotificationPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchPlayOfflineOnlyCase
import taiwan.no.one.feat.setting.domain.usecases.FetchSleepingTimerCase
import taiwan.no.one.ktx.livedata.toLiveData

internal class SettingViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val loginProvider by instance<LoginMethodsProvider>()
    private val fetchSleepingTimerCase by instance<FetchSleepingTimerCase>(DomainModules.TAG_SLEEPING_TIMER)
    private val addSleepingTimerCase by instance<AddSleepingTimerCase>()
    private val fetchLockScreenPlayerCase by instance<FetchLockScreenPlayerCase>(DomainModules.TAG_LOCK_SCREEN_PLAYER)
    private val addLockScreenPlayerCase by instance<AddLockScreenPlayerCase>()
    private val fetchPlayOfflineOnlyCase by instance<FetchPlayOfflineOnlyCase>(DomainModules.TAG_PLAY_OFFLINE_ONLY)
    private val addPlayOfflineOnlyCase by instance<AddPlayOfflineOnlyCase>()
    private val fetchNotificationPlayerCase
        by instance<FetchNotificationPlayerCase>(DomainModules.TAG_NOTIFICATION_PLAYER)
    private val addNotificationPlayerCase by instance<AddNotificationPlayerCase>()
    private val fetchAutoDisplayMvCase by instance<FetchAutoDisplayMvCase>(DomainModules.TAG_AUTO_DISPLAY_MV)
    private val addAutoDisplayMvCase by instance<AddAutoDisplayMvCase>()
    private val _logoutRes by lazy { ResultLiveData<Boolean>() }
    val logoutRes get() = _logoutRes.toLiveData()
    val sleepTimerChecked =
        liveData(viewModelScope.coroutineContext) { emit(runCatching { fetchSleepingTimerCase.execute() }) }
    private val _sleepingTimerCheckRes by lazy { ResultLiveData<Boolean>() }
    val sleepTimerCheckRes get() = _sleepingTimerCheckRes.toLiveData()
    val lockScreenChecked =
        liveData(viewModelScope.coroutineContext) { emit(runCatching { fetchLockScreenPlayerCase.execute() }) }
    private val _lockScreenCheckRes by lazy { ResultLiveData<Boolean>() }
    val lockScreenCheckRes get() = _lockScreenCheckRes.toLiveData()
    val offlineChecked =
        liveData(viewModelScope.coroutineContext) { emit(runCatching { fetchPlayOfflineOnlyCase.execute() }) }
    private val _offlineCheckRes by lazy { ResultLiveData<Boolean>() }
    val offlineCheckRes get() = _offlineCheckRes.toLiveData()
    val notificationPlayerChecked =
        liveData(viewModelScope.coroutineContext) { emit(runCatching { fetchNotificationPlayerCase.execute() }) }
    private val _notificationPlayerCheckRes by lazy { ResultLiveData<Boolean>() }
    val notificationPlayerCheckRes get() = _notificationPlayerCheckRes.toLiveData()
    val autoMvChecked =
        liveData(viewModelScope.coroutineContext) { emit(runCatching { fetchAutoDisplayMvCase.execute() }) }
    private val _autoMvCheckRes by lazy { ResultLiveData<Boolean>() }
    val autoMvCheckRes get() = _autoMvCheckRes.toLiveData()

    fun setSleepTimerSwitch(isChecked: Boolean) = viewModelScope.launch {
        _sleepingTimerCheckRes.value = runCatching {
            addSleepingTimerCase.execute(AddSleepingTimerReq(isChecked))
        }
    }

    fun setLockScreenSwitch(isChecked: Boolean) = viewModelScope.launch {
        _lockScreenCheckRes.value = runCatching {
            addLockScreenPlayerCase.execute(AddLockScreenPlayerReq(isChecked))
        }
    }

    fun setOfflineSwitch(isChecked: Boolean) = viewModelScope.launch {
        _offlineCheckRes.value = runCatching {
            addPlayOfflineOnlyCase.execute(AddPlayOfflineOnlyReq(isChecked))
        }
    }

    fun setNotificationPlayerSwitch(isChecked: Boolean) = viewModelScope.launch {
        _notificationPlayerCheckRes.value = runCatching {
            addNotificationPlayerCase.execute(AddNotificationPlayerReq(isChecked))
        }
    }

    fun setAutoMvSwitch(isChecked: Boolean) = viewModelScope.launch {
        _autoMvCheckRes.value = runCatching {
            addAutoDisplayMvCase.execute(AddAutoDisplayMvReq(isChecked))
        }
    }

    fun logout() = launchBehind {
        _logoutRes.postValue(loginProvider.logout())
    }
}

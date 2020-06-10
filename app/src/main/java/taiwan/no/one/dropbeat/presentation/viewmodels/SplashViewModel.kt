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

package taiwan.no.one.dropbeat.presentation.viewmodels

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.concurrent.TimeUnit
import taiwan.no.one.core.presentation.viewmodel.BehindAndroidViewModel
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.BuildConfig
import taiwan.no.one.dropbeat.R
import taiwan.no.one.ktx.livedata.toLiveData

internal class SplashViewModel(
    application: Application
) : BehindAndroidViewModel(application) {
    private val _configs by lazy { ResultLiveData<Boolean>() }
    val configs = _configs.toLiveData()

    fun getConfigs() = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(6)
        })
        setDefaultsAsync(R.xml.remote_config_defaults)
        fetchAndActivate().addOnCompleteListener {
            _configs.value = if (it.isSuccessful) Result.success(true) else Result.failure(IllegalArgumentException())
        }
    }
}

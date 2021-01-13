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

package taiwan.no.one.dropbeat.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.devrapid.kotlinknifer.logw
import com.google.android.play.core.splitcompat.SplitCompat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance
import taiwan.no.one.dropbeat.databinding.ActivitySplashBinding
import taiwan.no.one.dropbeat.di.Constant.TAG_WORKER_INIT_DATA
import taiwan.no.one.dropbeat.presentation.viewmodels.SplashViewModel

internal class SplashActivity : AppCompatActivity(), DIAware {
    /**
     * A DI Aware class must be within reach of a [DI] object.
     */
    override val di by di()
    private var binding: ActivitySplashBinding? = null
    private val vm by viewModels<SplashViewModel>()
    private val workManager by instance<WorkManager>()
    private val worker by instance<OneTimeWorkRequest>(TAG_WORKER_INIT_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        vm.configs.observe(this) {
        }

        SplitCompat.install(this)

        // HACK(jieyi): 1/13/21 This should move to viewmodel.
        workManager.getWorkInfoByIdLiveData(worker.id).observe(this) {
            if (!it.state.isFinished) {
                return@observe
            }
            logw("Initialization has been finished")
            launchApp()
        }
        workManager.enqueue(worker)
        vm.getConfigs()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun launchApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

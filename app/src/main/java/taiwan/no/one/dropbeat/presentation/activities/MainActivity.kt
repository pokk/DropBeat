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

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.devrapid.kotlinknifer.changeStatusBarColor
import com.google.android.play.core.splitcompat.SplitCompat
import java.util.Locale
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.R
import taiwan.no.one.dropbeat.databinding.ActivityMainBinding
import taiwan.no.one.dropbeat.presentation.lifecycle.SplitModuleAddLifecycle
import taiwan.no.one.dropbeat.presentation.viewmodels.PrivacyViewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val vm by viewModels<PrivacyViewModel>()

    // If we are using [NavHostFragment], need to use this way for get the navController.
    private val navigator
        get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    init {
        SplitModuleAddLifecycle(DropBeatApp.appContext, listOf("featSearchMusic"))
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration().apply { setLocale(Locale.getDefault()) }
        val ctx = newBase?.createConfigurationContext(config)
        super.attachBaseContext(ctx)
        SplitCompat.install(this)
    }

    override fun preSetContentView() {
        changeStatusBarColor(0, 0f)
    }

    override fun viewComponentBinding() {
        binding.bnvNavigator.setupWithNavController(navigator)
    }

    override fun init(savedInstanceState: Bundle?) {
        vm.getUserInfo()
    }

    override fun showLoading() {
        navigator.navigate(R.id.action_global_to_loading_dialog)
    }

    override fun hideLoading() {
        if (navigator.currentDestination?.id != R.id.loading_dialog_fragment) return
        navigator.navigateUp()
    }

    override fun showError(message: String) {
        navigator.navigate(R.id.action_global_to_error_dialog)
    }
}

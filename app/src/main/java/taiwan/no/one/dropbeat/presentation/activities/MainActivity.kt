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

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.devrapid.kotlinknifer.changeStatusBarColor
import com.devrapid.kotlinknifer.getDimen
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.visible
import com.google.android.play.core.splitcompat.SplitCompat
import java.util.Locale
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.R
import taiwan.no.one.dropbeat.databinding.ActivityMainBinding
import taiwan.no.one.dropbeat.presentation.lifecycle.SplitModuleAddLifecycle
import taiwan.no.one.dropbeat.presentation.viewmodels.PrivacyViewModel
import taiwan.no.one.widget.WidgetResDimen

class MainActivity : BaseActivity<ActivityMainBinding>() {
    var isMinimalPlayer = false
    var isBottomNaviBarVisible = true
        set(value) {
            if (value) animatorSlideDown.reverse() else animatorSlideDown.start()
            field = value
        }
    private var toggle = false
    private var isPlayerFragmentVisible = false
        set(value) {
            if (value) binding.navPlayerFragment.visible() else binding.navPlayerFragment.gone()
            field = value
        }
    private val vm by viewModels<PrivacyViewModel>()
    private val slideInAnimation by lazy {
        AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_up).apply {
            setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    isPlayerFragmentVisible = true
                    isBottomNaviBarVisible = false
                }

                override fun onAnimationEnd(animation: Animation?) = Unit

                override fun onAnimationRepeat(animation: Animation?) = Unit
            })
        }
    }
    private val slideOutAnimation by lazy {
        AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_down).apply {
            setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    isBottomNaviBarVisible = true
                }

                override fun onAnimationEnd(animation: Animation?) {
                    isPlayerFragmentVisible = false
                }

                override fun onAnimationRepeat(animation: Animation?) = Unit
            })
        }
    }
    private val animatorSlideDown by lazy {
        ValueAnimator.ofFloat(0f, getDimen(WidgetResDimen.md_eight_unit)).apply {
            duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            val currentPosition = binding.bnvNavigator.y
            addUpdateListener {
                binding.bnvNavigator.y = currentPosition + it.animatedValue as Float
            }
        }
    }

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
        binding.btnActive.setOnClickListener {
            if (toggle) dismissPlayer() else displayPlayer()
            toggle = !toggle
        }
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

    fun updatePlayerFragmentHeight(height: Int) {
        if (binding.navPlayerFragment.layoutParams.height == height) return
        binding.navPlayerFragment.updateLayoutParams { this.height = height }
    }

    fun displayPlayer() {
        binding.navPlayerFragment.startAnimation(slideInAnimation)
    }

    fun dismissPlayer() {
        if (isMinimalPlayer) {
            isPlayerFragmentVisible = false
            toggle = false
        }
        else {
            binding.navPlayerFragment.startAnimation(slideOutAnimation)
        }
    }
}

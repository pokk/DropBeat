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

package taiwan.no.one.feat.setting.presentation.fragments

import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devrapid.kotlinknifer.getDimen
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.visible
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialSharedAxis
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.presentation.viewmodels.PrivacyViewModel
import taiwan.no.one.feat.setting.R
import taiwan.no.one.feat.setting.databinding.FragmentSettingBinding
import taiwan.no.one.feat.setting.databinding.MergeSettingAppBlockBinding
import taiwan.no.one.feat.setting.databinding.MergeSettingMusicBlockBinding
import taiwan.no.one.feat.setting.databinding.MergeSettingOtherBlockBinding
import taiwan.no.one.feat.setting.databinding.MergeSettingSyncBinding
import taiwan.no.one.feat.setting.databinding.MergeSettingUserBlockBinding
import taiwan.no.one.feat.setting.presentation.viewmodels.SettingViewModel
import taiwan.no.one.ktx.view.find
import taiwan.no.one.widget.WidgetResDimen

internal class SettingFragment : BaseFragment<BaseActivity<*>, FragmentSettingBinding>() {
    //region Variable of View Binding
    private val mergeUserBlock get() = MergeSettingUserBlockBinding.bind(binding.root)
    private val mergeAppBlock get() = MergeSettingAppBlockBinding.bind(binding.root)
    private val mergeMusicBlock get() = MergeSettingMusicBlockBinding.bind(binding.root)
    private val mergeSyncBlock get() = MergeSettingSyncBinding.bind(binding.root)
    private val mergeOtherBlock get() = MergeSettingOtherBlockBinding.bind(binding.root)
    //endregion

    //region Variable of View Model
    private val vm by viewModels<SettingViewModel>()
    private val privacyVm by activityViewModels<PrivacyViewModel>()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun bindLiveData() {
        vm.logoutRes.observe(this) { res ->
            res.onSuccess {
                privacyVm.clearUseInfo()
                findNavController().navigateUp()
            }.onFailure { loge(it) }
        }
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        addStatusBarHeightMarginTop(binding.btnBack)
        // Set the Text
        listOf(
            // User Block
            mergeUserBlock.includeName to "Showing Name",
            mergeUserBlock.includeEmail to "Email",
            mergeUserBlock.includeChangePassword to "Change Password",
            // App Block
            mergeAppBlock.includeSleepTimer to "Sleeping Timer",
            mergeAppBlock.includeLockScreen to "Lockscreen Player",
            mergeAppBlock.includeOffline to "Play offline music only",
            mergeAppBlock.includeNotificationPlayer to "Notification Player",
            // Music Block
            mergeMusicBlock.includeQuality to "Quality of Music",
            mergeMusicBlock.includeAutoMv to "Auto Show MV Auto",
            // Sync Block
            mergeSyncBlock.includeLoggedInSync to "Sync Playlist to cloud",
            // Other Block
            mergeOtherBlock.includePolicy to "Policy",
            mergeOtherBlock.includeCoffeeToMe to "Buy Me a Coffee",
            mergeOtherBlock.includeStar to "5 Start in Store",
            mergeOtherBlock.includeFeedback to "Feedback",
        ).forEach { (viewGroup, text) ->
            viewGroup.root.find<TextView>(R.id.mtv_title).text = text
        }
        mergeMusicBlock.includeQuality.btnNext.text = "Auto"
        // Set the Icons
        listOf(
            mergeUserBlock.includeName,
            mergeUserBlock.includeChangePassword,
            mergeMusicBlock.includeQuality,
            mergeSyncBlock.includeLoggedInSync,
            mergeOtherBlock.includePolicy,
            mergeOtherBlock.includeCoffeeToMe,
            mergeOtherBlock.includeStar,
            mergeOtherBlock.includeFeedback,
        ).forEach { it.root.find<MaterialButton>(R.id.btn_next).apply { setIconResource(R.drawable.ic_chevron_right) } }
        mergeUserBlock.includeEmail.btnNext.icon = null
        // Change the margin if has the user information.
        privacyVm.userInfo.value?.getOrNull()?.also {
            // Set components to visible.
            binding.btnLogout.visible()
            mergeUserBlock.llUser.visible()
            (mergeAppBlock.llApp.layoutParams as MarginLayoutParams).topMargin =
                getDimen(WidgetResDimen.md_four_unit).toInt()
            mergeSyncBlock.includeLoggedInSync.root.visible()
            mergeSyncBlock.includeSync.root.gone()
            // Set the user information.
            mergeUserBlock.includeName.btnNext.text = it.displayName
            mergeUserBlock.includeEmail.btnNext.text = it.email
            mergeUserBlock.includeChangePassword.btnNext.text = "●●●●●●"
        }
    }

    override fun componentListenersBinding() {
        binding.btnLogout.setOnClickListener { vm.logout() }
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }
}

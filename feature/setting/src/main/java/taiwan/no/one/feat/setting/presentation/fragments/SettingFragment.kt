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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialSharedAxis
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.setting.R
import taiwan.no.one.feat.setting.databinding.FragmentSettingBinding
import taiwan.no.one.feat.setting.databinding.MergeSettingAppBlockBinding
import taiwan.no.one.feat.setting.presentation.viewmodels.SettingViewModel
import taiwan.no.one.ktx.view.find

internal class SettingFragment : BaseFragment<BaseActivity<*>, FragmentSettingBinding>() {
    private val vm by viewModels<SettingViewModel>()
    private val mergeAppBlock get() = MergeSettingAppBlockBinding.bind(binding.root)
    private val mergeMusicBlock get() = MergeSettingAppBlockBinding.bind(binding.root)
    private val mergeSyncBlock get() = MergeSettingAppBlockBinding.bind(binding.root)
    private val mergeOtherBlock get() = MergeSettingAppBlockBinding.bind(binding.root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        // Set the Text
        listOf(
            // App Block
            find<ConstraintLayout>(R.id.include_sleep_timer) to "Sleeping Timer",
            find<ConstraintLayout>(R.id.include_lock_screen) to "Lockscreen Player",
            find<ConstraintLayout>(R.id.include_offline) to "Play offline music only",
            find<ConstraintLayout>(R.id.include_notification_player) to "Notification Player",
            // Music Block
            find<ConstraintLayout>(R.id.include_quality) to "Quality of Music",
            find<ConstraintLayout>(R.id.include_auto_mv) to "Auto Show MV Auto",
            // Other Block
            find<ConstraintLayout>(R.id.include_policy) to "Policy",
            find<ConstraintLayout>(R.id.include_coffee_to_me) to "Buy Me a Coffee",
            find<ConstraintLayout>(R.id.include_star) to "5 Start in Store",
            find<ConstraintLayout>(R.id.include_feedback) to "Feedback",
        ).forEach { (viewGroup, text) ->
            viewGroup.find<TextView>(R.id.mtv_title).text = text
        }
        // Set the Icons
        listOf(
            find<ConstraintLayout>(R.id.include_quality),
            find(R.id.include_policy),
            find(R.id.include_coffee_to_me),
            find(R.id.include_star),
            find(R.id.include_feedback),
        ).forEach { it.find<MaterialButton>(R.id.btn_next).apply { setIconResource(R.drawable.ic_chevron_right) } }
        find<ConstraintLayout>(R.id.include_quality).find<MaterialButton>(R.id.btn_next).text = "Auto"
    }

    override fun componentListenersBinding() {
        binding.btnLogout.setOnClickListener { vm.logout() }
    }
}

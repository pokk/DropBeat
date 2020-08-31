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

package taiwan.no.one.feat.library.presentation.fragments

import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinshaver.isNotNull
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.presentation.viewmodels.PrivacyViewModel
import taiwan.no.one.feat.library.R
import taiwan.no.one.feat.library.databinding.FragmentMyPageBinding
import taiwan.no.one.feat.library.databinding.MergeTopControllerBinding
import taiwan.no.one.ktx.view.find

class MyHomeFragment : BaseFragment<BaseActivity<*>, FragmentMyPageBinding>() {
    private val privacyVm by activityViewModels<PrivacyViewModel>()
    private var _mergeTopControllerBinding: MergeTopControllerBinding? = null
    private val mergeTopControllerBinding get() = checkNotNull(_mergeTopControllerBinding)
    private val includeFavorite by lazy { find<ConstraintLayout>(R.id.include_favorite) }
    private val includeDownloaded by lazy { find<ConstraintLayout>(R.id.include_download) }
    private val includeHistory by lazy { find<ConstraintLayout>(R.id.include_history) }
    private val userEntity get() = privacyVm.userInfo.value?.getOrNull()

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        super.viewComponentBinding()
        _mergeTopControllerBinding = MergeTopControllerBinding.bind(binding.root)
        includeFavorite.find<TextView>(AppResId.mtv_explore_title).text = "Favorite Music"
        includeDownloaded.find<TextView>(AppResId.mtv_explore_title).text = "Downloaded"
        includeHistory.find<TextView>(AppResId.mtv_explore_title).text = "History"
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        mergeTopControllerBinding.apply {
            btnLogin.setOnClickListener {
                findNavController().navigate(MyHomeFragmentDirections.actionMyHomeToLoginGraph())
            }
            btnSetting.setOnClickListener {
                findNavController().navigate(MyHomeFragmentDirections.actionMyHomeToSettingGraph())
            }
        }
    }

    /**
     * Initialize doing some methods or actions here.
     *
     * @param savedInstanceState previous status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
        userEntity?.takeIf { it.uid.isNotNull() }?.let {
            mergeTopControllerBinding.mtvTitle.text = it.displayName
            mergeTopControllerBinding.btnLogin.gone()
        }
    }
}

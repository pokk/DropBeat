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

package taiwan.no.one.feat.login.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.login.databinding.FragmentForgotPasswordBinding
import taiwan.no.one.feat.login.presentation.viewmodels.AnalyticsViewModel
import taiwan.no.one.feat.login.presentation.viewmodels.LoginViewModel
import taiwan.no.one.ktx.view.afterTextChanges

internal class ForgotPasswordFragment : BaseFragment<BaseActivity<*>, FragmentForgotPasswordBinding>() {
    private val vm by viewModels<LoginViewModel>()
    private val analyticsVm by viewModels<AnalyticsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun bindLiveData() {
        vm.resetResp.observe(this) {
            it.onSuccess {
                logw(it)
                findNavController().popBackStack()
                analyticsVm.navigatedGoBackFromReset()
            }.onFailure(::loge)
        }
        vm.isValidEmail.observe(this) {
            binding.btnReset.isEnabled = it
        }
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.btnBack)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun componentListenersBinding() {
        binding.btnReset.setOnClickListener {
            vm.resetPassword(binding.tietEmail.text.toString())
            analyticsVm.clickedResetPassword()
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
            analyticsVm.navigatedGoBackFromForgotPassword()
        }
        binding.tietEmail
            .afterTextChanges()
            .debounce(taiwan.no.one.ktx.view.Constant.DEFAULT_DEBOUNCE_TEXT_CHANGE)
            .onEach { vm.validEmailFormat(it.toString()) }
            .launchIn(lifecycleScope)
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.validEmailFormat(binding.tietEmail.toString())
//        Firebase.firestore.collection("users")
//            .document(Firebase.auth.currentUser?.uid.orEmpty())
//            .addSnapshotListener(requireActivity()) { documentSnapshot, firebaseFirestoreException ->
//                logw(documentSnapshot?.data)
//            }
    }
}

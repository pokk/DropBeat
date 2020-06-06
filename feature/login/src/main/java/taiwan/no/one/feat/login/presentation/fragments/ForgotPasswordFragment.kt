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
import androidx.navigation.fragment.findNavController
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.login.databinding.FragmentForgotPasswordBinding
import taiwan.no.one.feat.login.presentation.viewmodels.LoginViewModel
import androidx.lifecycle.observe as obs

internal class ForgotPasswordFragment : BaseFragment<BaseActivity<*>, FragmentForgotPasswordBinding>() {
    private val vm by viewModels<LoginViewModel> { vmFactory }

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.resetResp.obs(this) {
            it.onSuccess {
                logw(it)
                findNavController().popBackStack()
            }.onFailure {
                loge(it)
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        binding.btnReset.setOnClickListener {
            vm.resetPassword(binding.tietEmail.text.toString())
        }
    }

    /**
     * Initialize doing some methods or actions here.
     *
     * @param savedInstanceState previous status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
//        Firebase.firestore.collection("users")
//            .document(Firebase.auth.currentUser?.uid.orEmpty())
//            .addSnapshotListener(requireActivity()) { documentSnapshot, firebaseFirestoreException ->
//                logw(documentSnapshot?.data)
//            }
    }
}

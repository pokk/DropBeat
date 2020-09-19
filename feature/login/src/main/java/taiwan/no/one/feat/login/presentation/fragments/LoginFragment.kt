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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.getDimen
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.OAuthProvider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.login.R
import taiwan.no.one.feat.login.data.remote.services.firebase.Credential
import taiwan.no.one.feat.login.databinding.FragmentLoginBinding
import taiwan.no.one.feat.login.presentation.auths.GoogleConfig
import taiwan.no.one.feat.login.presentation.recyclerviews.adapters.ThirdPartyLoginAdapter
import taiwan.no.one.feat.login.presentation.recyclerviews.decorators.SnsItemDecorator
import taiwan.no.one.feat.login.presentation.viewmodels.LoginViewModel
import taiwan.no.one.widget.WidgetResDimen

internal class LoginFragment : BaseFragment<BaseActivity<*>, FragmentLoginBinding>() {
    private val vm by viewModels<LoginViewModel>()
    private val snsAdapter by lazy { ThirdPartyLoginAdapter(snsList) }
    private val snsList by lazy {
        listOf(R.drawable.ic_facebook,
               R.drawable.ic_google,
               R.drawable.ic_twitter,
               R.drawable.ic_instagram)
    }

    //region Third-party Authorization
    // Google
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>

    // Facebook
    private val facebookCallbackManager by lazy { CallbackManager.Factory.create() }
    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            vm.login(Credential.Facebook(loginResult.accessToken.token))
        }

        override fun onCancel() = Unit

        override fun onError(exception: FacebookException) {
            loge(exception)
        }
    }
    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback)
        googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java) ?: throw NullPointerException()
                    vm.login(Credential.Google(account.idToken.orEmpty()))
                }
                catch (apiException: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    loge(apiException)
                }
                catch (nullPointerException: NullPointerException) {
                    loge(nullPointerException)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        LoginManager.getInstance().unregisterCallback(facebookCallbackManager)
    }

    override fun bindLiveData() {
        vm.userInfo.observe(this) {
            it.onSuccess {
                logw(it)
                findNavController().navigateUp()
            }.onFailure {
                loge(it)
            }
        }
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        binding.rvSns.apply {
            if (adapter == null) {
                adapter = snsAdapter
            }
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
            }
            if (itemDecorationCount <= 1) {
                addItemDecoration(SnsItemDecorator(getDimen(WidgetResDimen.md_two_unit).toInt()))
            }
        }
    }

    override fun componentListenersBinding() {
        snsAdapter.setOnClickListener {
            when (it) {
                R.drawable.ic_facebook -> LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("email", "public_profile"))
                R.drawable.ic_google -> googleLauncher.launch(GoogleConfig.getIntent(requireActivity()))
                R.drawable.ic_twitter -> {
                    val provider = OAuthProvider.newBuilder("twitter.com")
                    provider.addCustomParameter("lang", "en")
                }
                R.drawable.ic_instagram -> {
                }
                else -> Unit
            }
            binding.apply {
                btnForgotPassword.setOnClickListener {
                    findNavController().navigate(LoginFragmentDirections.actionLoginToForgotPassword())
                }
                btnLogin.setOnClickListener {
                    vm.login(tietEmail.text.toString(), tietPassword.text.toString())
                }
            }
        }
    }
}

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe as obs
import androidx.navigation.fragment.findNavController
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.login.data.remote.services.firebase.Credential
import taiwan.no.one.feat.login.databinding.FragmentLoginBinding
import taiwan.no.one.feat.login.presentation.auths.GoogleConfig
import taiwan.no.one.feat.login.presentation.viewmodels.LoginViewModel

internal class LoginFragment : BaseFragment<BaseActivity<*>, FragmentLoginBinding>() {
    private val vm by viewModels<LoginViewModel>()

    //region Third-party Authorization
    // Google
    private val googleLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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

    // Facebook
    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            vm.login(Credential.Facebook(loginResult.accessToken.token))
        }

        override fun onCancel() = Unit

        override fun onError(exception: FacebookException) {
            loge(exception)
        }
    }
    private val callbackManager by lazy { CallbackManager.Factory.create() }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.userInfo.obs(this) {
            it.onSuccess {
                logw(it)
            }.onFailure {
                loge(it)
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        binding.apply {
            btnRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginToRegister())
            }
            btnForgotPassword.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginToForgotPassword())
            }
            btnLogin.setOnClickListener {
                vm.login(binding.tietEmail.text.toString(), binding.tietPassword.text.toString())
            }
            btnGoogle.setOnClickListener {
                googleLauncher.launch(GoogleConfig.getIntent(requireContext()))
            }
            btnFacebook.setOnClickListener {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this@LoginFragment, listOf("email", "public_profile"))
            }
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        logw(Firebase.auth.currentUser?.uid)

        binding.btnTwitter.setOnClickListener {
            val provider = OAuthProvider.newBuilder("twitter.com")
            provider.addCustomParameter("lang", "en")
        }
    }

    override fun onDetach() {
        super.onDetach()
        LoginManager.getInstance().unregisterCallback(callbackManager)
    }
}

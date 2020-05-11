/*
 * MIT License
 *
 * Copyright (c) 2019 SmashKs
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

package taiwan.no.one.core.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.kodein.di.generic.instance
import taiwan.no.one.core.presentation.activity.BaseActivity
import java.lang.reflect.ParameterizedType

/**
 * The basic fragment is for the normal activity that prepares all necessary variables or functions.
 */
abstract class BaseFragment<out A : BaseActivity<*>, out V : ViewBinding> : LoadableFragment(),
                                                                            CoroutineScope by MainScope() {
    /** Provide the viewmodel factory to create a viewmodel */
    val vmFactory: ViewModelProvider.Factory by instance()
    @Suppress("UNCHECKED_CAST")
    protected val parent
        // If there's no parent, forcing crashing the app.
        get() = requireActivity() as A
    // Set action bar's back icon color into all fragments are inheriting advFragment.
    protected open val backDrawable by lazy {
        //                android.R.drawable.arrow_down_float
//            .toDrawable(parent)
//            .changeColor(getColor(R.color.colorPrimaryTextV1))
        android.R.drawable.arrow_down_float
    }
    protected val binding by viewBinding()
    private lateinit var localInflater: LayoutInflater
    //        private val actionTitle by extra<String>(COMMON_TITLE)
    private val actionTitle = ""

    //region Lifecycle
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        // Focusing on entering animation ([enter] == true) with animation ([nextAnim] != 0).
        if (!enter || nextAnim == 0) return super.onCreateAnimation(transit, enter, nextAnim)
        val anim = AnimationUtils.loadAnimation(activity, nextAnim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                onTransitionStart(animation)
            }

            override fun onAnimationRepeat(animation: Animation) {
                onTransitionRepeat(animation)
            }

            override fun onAnimationEnd(animation: Animation) {
                onTransitionEnd(animation)
                anim.setAnimationListener(null)
            }
        })

        return anim
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pre-set the binding live data.
        bindLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Keep the instance data.
        retainInstance = true
        localInflater = customTheme()?.let {
            // Create ContextThemeWrapper from the original Activity Context with the custom theme
            val contextThemeWrapper = ContextThemeWrapper(activity, it)
            // Clone the inflater using the ContextThemeWrapper
            inflater.cloneInContext(contextThemeWrapper)
        } ?: inflater

        return binding.root
    }

    /**
     * For initializing the view components and setting the listeners.
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Before setting.
        viewComponentBinding()
        componentListenersBinding()
        // Action for customizing.
        rendered(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        cancel() // cancel is extension on CoroutineScope
    }

    //endregion

    //region Customized methods
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    @UiThread
    protected open fun bindLiveData() = Unit

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    @UiThread
    protected open fun viewComponentBinding() {
        if (parent.supportActionBar == null) {
            // Set the title into the support action bar.
            parent.setSupportActionBar(provideActionBarResource())
        }
        parent.supportActionBar?.apply {
            actionBarTitle()?.let(this::setTitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(backDrawable)
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    @UiThread
    protected open fun componentListenersBinding() = Unit

    /**
     * Initialize doing some methods or actions here.
     *
     * @param savedInstanceState previous status.
     */
    @UiThread
    protected open fun rendered(savedInstanceState: Bundle?) = Unit

    /**
     * Set a specific theme to this fragment.
     *
     * @return [Int] style xml resource.
     */
    @UiThread
    @StyleRes
    protected open fun customTheme(): Int? = null

    /**
     * Set fragment title into action bar.
     *
     * @return [String] action bar title.
     */
    @UiThread
    protected open fun actionBarTitle(): CharSequence? = actionTitle

    /**
     * Provide action bar object for pre-setting.
     *
     * @return [Toolbar] action bar object.
     */
    @UiThread
    protected open fun provideActionBarResource(): Toolbar? = null

    /**
     * The event for starting the view transition animation.
     *
     * @param animation
     */
    @UiThread
    protected open fun onTransitionStart(animation: Animation) = Unit

    /**
     * The event for repeating the view transition animation.
     *
     * @param animation
     */
    @UiThread
    protected open fun onTransitionRepeat(animation: Animation) = Unit

    /**
     * The event for ending the view transition animation.
     *
     * @param animation
     */
    @UiThread
    protected open fun onTransitionEnd(animation: Animation) = Unit
    //endregion

    @UiThread
    open fun onBackPressed() = Unit

    @UiThread
    protected inline fun <reified VM : ViewModel> viewModel() = viewModels<VM> { vmFactory }

    @UiThread
    protected inline fun <reified VM : ViewModel> activityViewModel() = activityViewModels<VM> { vmFactory }

    @Suppress("UNCHECKED_CAST")
    @UiThread
    /** Using reflection to get dynamic view binding name. */
    private fun viewBinding() = lazy {
        /** [ViewBinding] is the second (index: 1) in the generic declare. */
        val viewBindingConcreteClass =
            (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        val inflateMethod = viewBindingConcreteClass.getMethod("inflate", LayoutInflater::class.java)
        inflateMethod.invoke(null, layoutInflater) as V
    }
}

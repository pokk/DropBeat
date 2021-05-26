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

package taiwan.no.one.core.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import taiwan.no.one.core.presentation.LoadView
import taiwan.no.one.core.presentation.activity.BaseActivity

/**
 * The basic fragment is for the normal activity that prepares all necessary variables or functions.
 */
abstract class BaseDialogFragment<out A : BaseActivity<*>, V : ViewBinding> : LoadableDialogFragment(),
                                                                              CoroutineScope by MainScope() {
    @Suppress("UNCHECKED_CAST")
    protected val parent
        // If there's no parent, forcing crashing the app.
        get() = requireActivity() as A

    private var _binding: V? = null
    protected val binding get() = checkNotNull(_binding) { "The View Binding is null!" }
    private lateinit var localInflater: LayoutInflater
    private val actionTitle = ""

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pre-set the binding live data.
        bindLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        localInflater = customTheme()?.let {
            // Create ContextThemeWrapper from the original Activity Context with the custom theme
            val contextThemeWrapper = ContextThemeWrapper(activity, it)
            // Clone the inflater using the ContextThemeWrapper
            inflater.cloneInContext(contextThemeWrapper)
        } ?: inflater
        _binding = viewBinding()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        cancel() // cancel is extension on CoroutineScope
    }

    //endregion

    //region Loading
    override fun showLoading() = (requireActivity() as LoadView).showLoading()

    override fun hideLoading() = (requireActivity() as LoadView).hideLoading()

    override fun showRetry() = (requireActivity() as LoadView).showRetry()

    override fun hideRetry() = (requireActivity() as LoadView).hideRetry()

    override fun showError(message: String) = (requireActivity() as LoadView).showError(message)
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
    //endregion

    @UiThread
    open fun onBackPressed() = Unit

    @UiThread
    protected fun addStatusBarHeightMarginTop(view: View) {
        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
            .takeIf { 0 < it }
            ?.let { resources.getDimensionPixelSize(it) } ?: 0
        if (view.layoutParams is ConstraintLayout.LayoutParams) {
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = view.top + statusBarHeight
            }
        }
        else {
            view.updateLayoutParams<MarginLayoutParams> {
                setMargins(view.left, view.top + statusBarHeight, view.right, view.bottom)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @UiThread
    /** Using reflection to get dynamic view binding name. */
    private fun viewBinding(): V {
        /** [ViewBinding] is the second (index: 1) in the generic declare. */
        val viewBindingConcreteClass =
            (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        val inflateMethod = viewBindingConcreteClass.getMethod("inflate", LayoutInflater::class.java)

        return inflateMethod.invoke(null, layoutInflater) as V
    }
}

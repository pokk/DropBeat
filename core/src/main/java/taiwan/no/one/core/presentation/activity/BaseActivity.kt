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

package taiwan.no.one.core.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * The basic activity is for the normal activity that prepares all necessary variables or functions.
 */
abstract class BaseActivity<out V : ViewBinding> : LoadableActivity(), CoroutineScope by MainScope() {
    private var _binding: V? = null
    protected val binding get() = checkNotNull(_binding) { "The View Binding is null!" }

    //region Activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pre-set the view components.
        preSetContentView()
        _binding = viewBinding()
        setContentView(binding.root)
        // Post-set the view components.
        viewComponentBinding()
        componentListenersBinding()
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        uninit()
        cancel() // cancel is extension on CoroutineScope
    }
    //endregion

    /**
     * Initialize doing some methods and actions.
     *
     * @param savedInstanceState previous state after this activity was destroyed.
     */
    @UiThread
    protected open fun init(savedInstanceState: Bundle?) = Unit

    /**
     * For separating the huge function code in [init]. Initialize all view components here before [setContentView].
     */
    @UiThread
    protected open fun preSetContentView() = Unit

    /**
     * For separating the huge function code in [init]. Initialize all view components here after [setContentView].
     */
    @UiThread
    protected open fun viewComponentBinding() = Unit

    /**
     * For separating the huge function code in [init]. Initialize all component listeners here.
     */
    @UiThread
    protected open fun componentListenersBinding() = Unit

    /**
     * Uninit the data or deconstruct objects.
     */
    @UiThread
    protected open fun uninit() = Unit

    @Suppress("UNCHECKED_CAST")
    @UiThread
    /** Using reflection to get dynamic view binding name. */
    private fun viewBinding(): V {
        /** [ViewBinding] is the first (index: 0) in the generic declare. */
        val viewBindingConcreteClass =
            (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val inflateMethod = viewBindingConcreteClass.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, layoutInflater) as V
    }
}

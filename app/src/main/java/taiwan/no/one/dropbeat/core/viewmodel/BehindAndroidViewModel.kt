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

package taiwan.no.one.dropbeat.core.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DI.Module
import org.kodein.di.DIAware
import org.kodein.di.LateInitDI
import kotlin.coroutines.CoroutineContext

abstract class BehindAndroidViewModel(application: Application) : AndroidViewModel(application), DIAware {
    init {
        (context as? DIAware)?.di?.let { di.baseDI = it }
    }

    override val di = LateInitDI()

    var module: Module? = null
        @VisibleForTesting
        set(value) {
            if (value == null) return
            di.baseDI = DI.lazy {
                import(value)
            }
        }
    protected val context get() = getApplication<Application>()
    protected abstract val handle: SavedStateHandle

    protected inline fun launchBehind(
        context: CoroutineContext = Dispatchers.IO,
        crossinline block: suspend CoroutineScope.() -> Unit,
    ) = viewModelScope.launch(context) { block() }
}

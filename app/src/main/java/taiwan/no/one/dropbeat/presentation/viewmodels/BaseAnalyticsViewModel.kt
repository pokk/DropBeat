/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
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

package taiwan.no.one.dropbeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import taiwan.no.one.analytics.AnalyticsSender
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource
import taiwan.no.one.ext.extensions.now

open class BaseAnalyticsViewModel : ViewModel(), DIAware {
    override val di by closestDI(DropBeatApp.appContext)
    protected val sender by instance<AnalyticsSender>()

    protected open fun sendClickedEvent(which: String) {
        sender.sendEvent(ClickedEvent.SendClicked(which, now()))
    }

    protected open fun navigated(from: NavigationSource, to: NavigationSource, extra: String? = null) {
        sender.sendEvent(ClickedEvent.Navigated(from, to, extra, now()))
    }
}

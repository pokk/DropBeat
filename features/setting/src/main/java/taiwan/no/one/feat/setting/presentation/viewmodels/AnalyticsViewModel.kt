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

package taiwan.no.one.feat.setting.presentation.viewmodels

import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.LOGIN
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.PREVIOUS_SCREEN
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.SETTING
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.LOGOUT
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.SYNC
import taiwan.no.one.dropbeat.presentation.viewmodels.BaseAnalyticsViewModel

internal class AnalyticsViewModel : BaseAnalyticsViewModel() {
    fun navigatedToLogin() = navigated(SETTING, LOGIN)

    fun navigatedGoBackFromSetting() = navigated(SETTING, PREVIOUS_SCREEN)

    fun clickedSync() = sendClickedEvent("$SYNC")

    fun clickedLogout() = sendClickedEvent("$LOGOUT")
}

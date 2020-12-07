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

package taiwan.no.one.feat.explore.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import org.kodein.di.instance
import taiwan.no.one.analytics.AnalyticsSender
import taiwan.no.one.dropbeat.core.viewmodel.BehindViewModel
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.EXPLORE
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.PLAYER
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.PLAYLIST
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.FAVORITE
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.OPTION
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.PLAY
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.UNFAVORITE
import java.util.Calendar

internal class AnalyticsViewModel(override val handle: SavedStateHandle) : BehindViewModel() {
    private val sender by instance<AnalyticsSender>()

    fun navigatedToPlayer() = navigated(EXPLORE, PLAYER)

    fun navigatedToPlaylist(extra: String) = navigated(EXPLORE, PLAYLIST, extra)

    fun clickedPlayAMusic(which: String) = sendClickedEvent("$PLAY: $which")

    fun clickedOption(which: String) = sendClickedEvent("$OPTION: $which")

    fun clickedFavorite(isFavorite: Boolean, which: String) = sendClickedEvent("${
        if (isFavorite) FAVORITE else UNFAVORITE
    }: $which")

    fun sendClickedEvent(which: String) {
        sender.sendEvent(ClickedEvent.SendClicked(which, Calendar.getInstance().time))
    }

    fun navigated(from: NavigationSource, to: NavigationSource, extra: String? = null) {
        sender.sendEvent(ClickedEvent.Navigated(from, to, extra, Calendar.getInstance().time))
    }
}

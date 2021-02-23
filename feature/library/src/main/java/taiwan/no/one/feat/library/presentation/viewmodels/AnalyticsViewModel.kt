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

package taiwan.no.one.feat.library.presentation.viewmodels

import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.ARTIST_DETAIL
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.CREATE_PLAYLIST
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.LOGIN
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.MYPAGE
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.PLAYLIST
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.PREVIOUS_SCREEN
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.RENAME_PLAYLIST
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.SEARCH
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.SETTING
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.NavigationSource.TAG_PLAYLIST
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.FAVORITE
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.MORE
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.NEW_PLAYLIST
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.OPTION
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.PLAY
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.UNFAVORITE
import taiwan.no.one.dropbeat.presentation.analytics.ClickedEvent.SendClicked.TypeSource.UPDATE_PLAYLIST
import taiwan.no.one.dropbeat.presentation.viewmodels.BaseAnalyticsViewModel

internal class AnalyticsViewModel : BaseAnalyticsViewModel() {
    fun navigatedToPlaylist() = navigated(MYPAGE, PLAYLIST)

    fun navigatedToLogin() = navigated(MYPAGE, LOGIN)

    fun navigatedToSetting() = navigated(MYPAGE, SETTING)

    fun navigatedToRename() = navigated(PLAYLIST, RENAME_PLAYLIST)

    fun navigatedToCreate() = navigated(PLAYLIST, CREATE_PLAYLIST)

    fun navigatedToSearch() = navigated(PLAYLIST, SEARCH)

    fun navigatedGoBackFromRename() = navigated(RENAME_PLAYLIST, PREVIOUS_SCREEN)

    fun navigatedGoBackFromCreate() = navigated(CREATE_PLAYLIST, PREVIOUS_SCREEN)

    fun navigatedGoBackFromPlaylist() = navigated(PLAYLIST, PREVIOUS_SCREEN)

    fun navigatedGoBackFromTagPlaylist() = navigated(TAG_PLAYLIST, PREVIOUS_SCREEN)

    fun navigatedGoBackFromArtist() = navigated(ARTIST_DETAIL, PREVIOUS_SCREEN)

    fun clickedMore() = sendClickedEvent("$MORE")

    fun clickedRename(which: String) = sendClickedEvent("$UPDATE_PLAYLIST: $which")

    fun clickedCreateNewPlaylist(which: String) = sendClickedEvent("$NEW_PLAYLIST: $which")

    fun clickedPlayAMusic(which: String) = sendClickedEvent("$PLAY: $which")

    fun clickedOption(which: String) = sendClickedEvent("$OPTION: $which")

    fun clickedFavorite(isFavorite: Boolean, which: String) = sendClickedEvent("${
        if (isFavorite) FAVORITE else UNFAVORITE
    }: $which")
}

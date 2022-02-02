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

package taiwan.no.one.dropbeat.presentation.analytics

import kotlinx.datetime.Instant
import taiwan.no.one.analytics.AnalyticsEvent
import taiwan.no.one.analytics.Constant

object ClickedEvent {
    class SendClicked(
        which: String,
        timestamp: Instant,
    ) : AnalyticsEvent(
        Constant.Events.SendClicked.name,
        mapOf(
            Constant.Events.SendClicked.Params.TIME_OF_CLICKS to timestamp.toString(),
            Constant.Events.SendClicked.Params.WHICH to which,
        )
    ) {
        enum class TypeSource {
            PLAY,
            STOP_TRACK,
            PREVIOUS_TRACK,
            NEXT_TRACK,
            OPTION,
            UNFAVORITE,
            FAVORITE,
            SEARCH,
            DOWNLOAD,
            UPDATE_PLAYLIST,
            NEW_PLAYLIST,
            MORE,
            REGISTER,
            RESET_PASSWORD,
            SYNC,
            LOGIN_APP,
            LOGOUT,
        }
    }

    class Navigated(
        from: NavigationSource,
        to: NavigationSource,
        extraInfo: String? = null,
        timestamp: Instant,
    ) : AnalyticsEvent(
        Constant.Events.Navigated.name,
        buildMap {
            put(Constant.Events.Navigated.Params.TIME_OF_CLICKS, timestamp.toString())
            put(Constant.Events.Navigated.Params.FROM, from.name)
            put(Constant.Events.Navigated.Params.TO, to.name)
            extraInfo?.let { put(Constant.Events.Navigated.Params.EXTRA_INFO, it) }
        }
    )

    // Screen Name.
    enum class NavigationSource {
        MAIN,

        // Explore features.
        EXPLORE,
        PLAYLIST,
        TAG_PLAYLIST,
        CREATE_PLAYLIST,
        RENAME_PLAYLIST,
        ARTIST_DETAIL,
        RANKING_DETAIL,

        // Search features.
        SEARCH,
        SEARCH_RESULT,

        // MyPage features.
        MYPAGE,
        LOGIN,
        FORGOT_PASSWORD,
        REGISTER,
        SETTING,

        // Player features.
        PLAYER,

        // Others
        PREVIOUS_SCREEN,
    }
}

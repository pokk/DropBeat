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

package taiwan.no.one.analytics

object Constant {
    sealed class Events(val name: String) {
        object FirstLaunch : Events("first_launch")

        object SessionStart : Events("session_start")

        // Each event is having its own class
        object Navigated : Events("navigated") {
            // this is an object which contains possible parameters. Note: it's not a final
            // list of possible parameters, some parameters can be re-used from other events.
            // The point is to keep all possible constants in this class
            object Params {
                const val FROM = "from"
                const val TO = "to"
            }

            // this are possible values of event parameters from and to.
            object NavigationSources {
                const val NOTIFICATIONS = "notifications"
            }
        }

        object SendClicked : Events("send_clicked") {
            object Params {
                const val TIME_BETWEEN_CLICKS = "time_between_clicks"
            }
        }

        object NotificationsChanged : Events("notifications_changed")
    }

    object UserProperties {
        const val NOTIFICATION_STATE = "notification_state" // here we have a list of user properties
    }
}

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

package taiwan.no.one.analytics

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message
import java.util.Date

class AnalyticsSender(
    private val analytics: FirebaseAnalytics,
) {
    fun sendEvent(event: AnalyticsEvent) {
        if (event.providers.contains(AnalyticsProvider.ANALYTICS_FIREBASE)) {
            analytics.logEvent(event.eventName, event.params.toBundle())
        }
        if (event.providers.contains(AnalyticsProvider.SENTRY)) {
            val sentryEvent = SentryEvent(Date()).apply {
                level = SentryLevel.INFO
                logger = event.eventName
                message = Message().apply { unknown = event.params }
            }
            Sentry.captureEvent(sentryEvent)
        }
        Log.d("Analytics",
              "\uD83D\uDCE9 Event was sent: ${event.eventName}. Params: ${event.params}. Providers: ${event.providers}")
    }

    fun setUserProperty(property: AnalyticsProperty) {
        if (property.providers.contains(AnalyticsProvider.ANALYTICS_FIREBASE)) {
            analytics.setUserProperty(property.propertyName, property.toString())
        }
        if (property.providers.contains(AnalyticsProvider.SENTRY)) {
        }
    }
}

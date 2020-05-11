/*
 * MIT License
 *
 * Copyright (c) 2019 SmashKs
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

package taiwan.no.one.core.data.remote.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import taiwan.no.one.ktx.internet.hasNetwork

class ConnectInterceptor(
    private val context: Context
) : Interceptor {
    companion object {
        private const val AWeekTime = 60 * 60 * 24 * 7
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the request from the chain.
        var request = chain.request()

        /**
         *  Leveraging the advantage of using Kotlin,
         *  we initialize the request and change its header depending on whether
         *  the device is connected to Internet or not.
         */
        request = if (hasNetwork(context) == true) {
            /**
             *  If there is Internet, get the cache that was stored 5 seconds ago.
             *  If the cache is older than 5 seconds, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-age' attribute is responsible for this behavior.
             */
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        }
        else {
            /**
             *  If there is no Internet, get the cache that was stored 7 days ago.
             *  If the cache is older than 7 days, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-stale' attribute is responsible for this behavior.
             *  The 'only-if-cached' attribute indicates to not retrieve add data; fetch the cache
             *  only instead.
             */
            request.newBuilder().header("Cache-Control",
                                        "public, only-if-cached, max-stale=$AWeekTime").build()
        }
        // End of if-else statement

        // Add the modified request to the chain.
        return chain.proceed(request)
    }
}

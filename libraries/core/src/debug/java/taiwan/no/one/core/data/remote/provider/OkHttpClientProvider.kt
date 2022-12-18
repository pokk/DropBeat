/*
 * MIT License
 *
 * Copyright (c) 2022 Jieyi
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

package taiwan.no.one.core.data.remote.provider

import android.content.Context
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import taiwan.no.one.core.data.remote.interceptor.MockRequestInterceptor
import taiwan.no.one.core.data.remote.util.SelfSigningClientBuilder

abstract class OkHttpClientProvider(
    private val context: Context,
) {
    companion object Constant {
        private const val CACHE_MAX_SIZE = 10 * 1024 * 1024L // 10 MiB
    }

    var readTimeOut = 0L
    var writeTimeOut = 0L
    var connectTimeOut = 0L

    protected open val cacheMaxSize = CACHE_MAX_SIZE

    open fun provideClientBuilder(vararg interceptors: Interceptor) = OkHttpClient.Builder().apply {
        cache(provideCache())
        readTimeout(readTimeOut, TimeUnit.SECONDS)
        writeTimeout(writeTimeOut, TimeUnit.SECONDS)
        connectTimeout(connectTimeOut, TimeUnit.SECONDS)
        interceptors.forEach { addInterceptor(it) }
        addInterceptor(MockRequestInterceptor(context))
        // Those three are for HTTPS protocol.
        connectionSpecs(mutableListOf(ConnectionSpec.RESTRICTED_TLS,
                                      ConnectionSpec.MODERN_TLS,
                                      ConnectionSpec.COMPATIBLE_TLS,
            // This is for HTTP protocol.
                                      ConnectionSpec.CLEARTEXT))
        sslSocketFactory(SelfSigningClientBuilder.initSSL(context),
                         SelfSigningClientBuilder.systemDefaultTrustManager())
    }

    open fun provideCache() = Cache(context.cacheDir, cacheMaxSize)
}

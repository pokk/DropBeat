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

package taiwan.no.one.core.data.remote

// import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import android.content.Context
import okhttp3.logging.HttpLoggingInterceptor
import taiwan.no.one.core.data.remote.interceptor.ConnectInterceptor
import taiwan.no.one.core.data.remote.interceptor.OfflineCacheInterceptor
import taiwan.no.one.core.data.remote.provider.OkHttpClientProvider
import taiwan.no.one.core.data.remote.provider.RetrofitProvider

abstract class BaseRetrofitConfig(
    private val context: Context,
    private val clientProvider: OkHttpClientProvider,
    private val retrofitProvider: RetrofitProvider
) : RetrofitConfig {
    companion object Constant {
        private const val TIME_OUT = 10L
    }

    init {
        clientProvider.also {
            it.readTimeOut = readTimeOut
            it.writeTimeOut = writeTimeOut
            it.connectTimeOut = connectTimeOut
        }
    }

    override val baseUrl = ""

    override val readTimeOut = TIME_OUT

    override val writeTimeOut = TIME_OUT

    override val connectTimeOut = TIME_OUT

    override fun provideRetrofitBuilder() = retrofitProvider.provideBuilder(baseUrl)
        .client(provideOkHttpClientBuilder().build())

    protected open fun provideOkHttpClientBuilder() = clientProvider.provideClientBuilder(
//        OkHttpProfilerInterceptor(),
        OfflineCacheInterceptor(context),
        ConnectInterceptor(context),
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    )
}

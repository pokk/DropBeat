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

package taiwan.no.one.dropbeat.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import taiwan.no.one.dropbeat.BuildConfig
import taiwan.no.one.dropbeat.di.Constant.TAG_BASE_OKHTTP

object NetworkModules {
    fun provide(context: Context) = Kodein.Module("NetworkModule") {
        import(BasedNetworkModules.netProvider(context))
        // Build Debug OkHttp object
        bind<OkHttpClient.Builder>() with provider {
            instance<OkHttpClient.Builder>(TAG_BASE_OKHTTP).apply {
                if (BuildConfig.DEBUG) {
//                    addInterceptor(HttpLoggingInterceptor().setLevel(BODY))  // For print to logcat.
//                    addInterceptor(OkHttpProfilerInterceptor())  // For OkHttp Profiler plugins.
                    addNetworkInterceptor(StethoInterceptor())
                }
            }
        }
    }
}

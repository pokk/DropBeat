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

package config

object DebugDependency {
    private object Version {
        const val STEHO = "1.6.0"
        const val DEBUG_DB = "1.0.6"
        const val LEAKCANARY = "2.8.1"
    }

    const val STEHO = "com.facebook.stetho:stetho:${Version.STEHO}"
    const val STEHO_INTERCEPTOR = "com.facebook.stetho:stetho-okhttp3:${Version.STEHO}"
    const val DEBUG_DB = "com.amitshekhar.android:debug-db:${Version.DEBUG_DB}"
    const val LEAKCANARY = "com.squareup.leakcanary:leakcanary-android:${Version.LEAKCANARY}"
}

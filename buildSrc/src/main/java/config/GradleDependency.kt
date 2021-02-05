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

package config

import config.LibraryDependency.Version.NAVIGATION_KTX

object GradleDependency {
    object Version {
        const val SAFE_ARGS = NAVIGATION_KTX
        const val GOOGLE_SERVICE = "4.3.5"
        const val CRASHLYTICS = "2.4.1"
        const val PERFORMANCE = "1.3.4"
    }

    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${CoreDependency.Version.KOTLIN}"
    const val SAFE_ARGS = "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.SAFE_ARGS}"
    const val GOOGLE_SERVICE = "com.google.gms:google-services:${Version.GOOGLE_SERVICE}"
    const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-gradle:${Version.CRASHLYTICS}"
    const val PERFORMANCE = "com.google.firebase:perf-plugin:${Version.PERFORMANCE}"
}

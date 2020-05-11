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

package config

import config.LibraryDependency.Version.NAVIGATION_KTX

object GradleDependency {
    object Version {
        const val SAFE_ARGS = NAVIGATION_KTX
        const val GOOGLE_SERVICE = "4.3.3"
        const val DETEKT = "1.8.0"
        const val VERSION_UPDATER = "0.28.0"
    }

    const val SAFE_ARGS = "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.SAFE_ARGS}"
    const val GOOGLE_SERVICE = "com.google.gms:google-services:${Version.GOOGLE_SERVICE}"
    const val DETEKT = "io.gitlab.arturbosch.detekt"
    const val GRADLE_VERSION_UPDATER = "com.github.ben-manes.versions"
}

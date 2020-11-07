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

object TestLibraryDependency {
    private object Version {
        const val KTX_CORE = "1.3.1-alpha02"
        const val CORE = "2.1.0"
        const val JUNIT = "1.1.3-alpha02"
        const val ASSERTK = "0.23"
        const val ESPRESSO = "3.4.0-alpha02"
        const val KAKAO = "2.1.0"
        const val MOCKK = "1.10.2"
    }

    const val COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${CoreDependency.Version.KOTLIN_COROUTINE}"
    const val JUNIT = "junit:junit:4.13.1"

    // Assert
    const val ASSERTK = "com.willowtreeapps.assertk:assertk-jvm:${Version.ASSERTK}"

    // Mock
    const val MOCKK = "io.mockk:mockk:${Version.MOCKK}"
    const val MOCKK_ANDROID = "io.mockk:mockk-android:${Version.MOCKK}"

    // Android Related
    const val ANDROID_JUNIT = "androidx.test.ext:junit:${Version.JUNIT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Version.ESPRESSO}"
    const val ESPRESSO_INTENT = "androidx.test.espresso:espresso-intents:${Version.ESPRESSO}"
    const val ESPRESSO_IDLING = "androidx.test.espresso:espresso-idling-resource:${Version.ESPRESSO}"
    const val CORE_KTX = "androidx.test:core-ktx:${Version.KTX_CORE}"
    const val ARCH_CORE = "androidx.arch.core:core-testing:${Version.CORE}"
    const val NAVIGATION = "androidx.navigation:navigation-testing:${LibraryDependency.Version.NAVIGATION_KTX}"
    const val FRAGMENT = "androidx.fragment:fragment-testing:${LibraryDependency.Version.FRAGMENT_KTX}"
    const val KAKAO = "com.agoda.kakao:kakao:${Version.KAKAO}"

    // Retrofit2
    const val OKHTTP_SERVER = "com.squareup.okhttp3:mockwebserver:${LibraryDependency.Version.OKHTTP3}"
}

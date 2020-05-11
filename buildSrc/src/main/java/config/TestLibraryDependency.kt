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

object TestLibraryDependency {
    private object Version {
        const val CORE = "1.2.0"
        const val JUNIT = "1.1.1"
        const val ASSERT_K = "0.19"
        const val RUNNER = "1.2.0"
        const val ESPRESSO = "3.2.0"
        const val KAKAO = "2.1.0"
        const val POWER_MOCKITO = "1.7.4"
        const val MOCKITO_KOTLIN = "2.2.0"
        const val MOCKITO_ANDROID = "2.23.0"
        const val MOCKK = "v1.8.9.kotlin13"
        const val BYTE_BUDDY = "1.10.1"
    }

    const val JUNIT_CORE = "androidx.test:core:${Version.CORE}"
    const val JUNIT = "androidx.test.ext:junit:${Version.JUNIT}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-test-junit:${CoreDependency.Version.KOTLIN}"

    // Assert
    const val ASSERTK = "com.willowtreeapps.assertk:assertk-jvm:${Version.ASSERT_K}"

    // Mockito
    const val MOCKITO = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Version.MOCKITO_KOTLIN}"
    const val POWER_MOCK_JUNIT = "org.powermock:powermock-module-junit4:${Version.POWER_MOCKITO}"
    const val POWER_MOCKITO = "org.powermock:powermock-api-mockito2:${Version.POWER_MOCKITO}"

    // Buddy
    const val BYTE_BUDDY = "net.bytebuddy:byte-buddy:${Version.BYTE_BUDDY}"
    const val BYTE_BUDDY_AGENT = "net.bytebuddy:byte-buddy-agent:${Version.BYTE_BUDDY}"
    const val BYTE_BUDDY_ANDROID = "net.bytebuddy:byte-buddy-android:${Version.BYTE_BUDDY}"

    // Android Related
    const val MOCKITO_ANDROID = "org.mockito:mockito-android:${Version.MOCKITO_ANDROID}"
    const val MOCKK_ANDROID = "io.mockk:mockk-android:${Version.MOCKK}"
    const val KAKAO = "com.agoda.kakao:kakao:${Version.KAKAO}"
    const val RUNNER = "androidx.test:runner:${Version.RUNNER}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Version.ESPRESSO}"

    // Testing stub
    const val IDLING_ESPRESSO = "androidx.test.espresso:espresso-idling-resource:${Version.ESPRESSO}"
}

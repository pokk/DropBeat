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

package plugins

import config.CommonModuleDependency

val modules = CommonModuleDependency.getLibraryModuleSimpleName()
val features = CommonModuleDependency.getFeatureModuleSimpleName()

subprojects {
    beforeEvaluate {
        apply {
            when (name) {
                in listOf("ext") -> {
                    plugin("java-library")
                    plugin("kotlin")
                }
                in modules -> {
                    plugin("com.android.library")
                    plugin("kotlin-android")
                    // special plugins
                    if (name in listOf("entity", "core")) {
                        plugin("com.google.devtools.ksp")
                    }
                    if (name in listOf("entity")) {
                        plugin("kotlin-parcelize")
                    }
                }
                in features -> {
                    plugin("com.android.dynamic-feature")
                    plugin("kotlin-android")
                    plugin("kotlin-parcelize")
                    plugin("kotlin-kapt")
                    plugin("com.google.devtools.ksp")
                    plugin("androidx.navigation.safeargs.kotlin")
                }
            }
            if (this@subprojects.name == "core") {
                plugin("org.jetbrains.kotlin.kapt")
            }
        }
    }
}

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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import config.AndroidConfiguration
import config.CommonModuleDependency

val modules = CommonModuleDependency.getLibraryModuleSimpleName()
val features = CommonModuleDependency.getFeatureModuleSimpleName()

subprojects {
    afterEvaluate {
        //region Common Setting
        if (name !in listOf("ext", "features", "libraries")) {
            // BaseExtension is common parent for application, library and test modules
            extensions.apply {
                configure<BaseExtension> {
                    compileSdkVersion(AndroidConfiguration.COMPILE_SDK)
                    defaultConfig {
                        minSdk = AndroidConfiguration.MIN_SDK
                        targetSdk = AndroidConfiguration.TARGET_SDK
                        vectorDrawables.useSupportLibrary = true
                        testInstrumentationRunner = AndroidConfiguration.TEST_INSTRUMENTATION_RUNNER
                        consumerProguardFiles(file("consumer-rules.pro"))
                    }
                    buildTypes {
                        getByName("release") {
                            // This is exceptions.
                            if (this@subprojects.name == "app") {
                                // isMinifyEnabled = true
                                proguardFiles(
                                    getDefaultProguardFile("proguard-android-optimize.txt"),
                                    file("proguard-rules.pro"),
                                )
                            }
                        }
                        getByName("debug") {
                            splits.abi.isEnable = false
                            splits.density.isEnable = false
                            aaptOptions.cruncherEnabled = false
                            // XXX(jieyi): Don't know the reason why if [isTestCoverageEnabled] removed, the jacoco
                            //  report is generated.
                            //  ref: [https://stackoverflow.com/questions/70589854/android-jacoco-code-coverage-is-not-generating-after-gradle-upgrade-to-7-0-x]
                            // isTestCoverageEnabled = true
                            // Only use this flag on builds you don't proguard or upload to beta-by-crashlytics.
                            // ext.set("alwaysUpdateBuildId", false)
                            isCrunchPngs = false // Enabled by default for RELEASE build type
                        }
                    }
                    applyCompileOptions()
                    applyTestOptions()
                    if (this@subprojects.name !in modules) {
                        buildFeatures.viewBinding = true
                    }
                }
                // For the different type of the project.
                findByType(ApplicationExtension::class.java)?.applyLintOptions()
                findByType(DynamicFeatureExtension::class.java)?.applyLintOptions()
                findByType(LibraryExtension::class.java)?.applyLintOptions()
            }
        }
        //endregion
    }
}

fun BaseExtension.applyCompileOptions() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

fun BaseExtension.applyTestOptions() {
    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

fun DynamicFeatureExtension.applyLintOptions() {
    lint {
        abortOnError = false
        ignoreWarnings = true
        quiet = true
    }
}

fun ApplicationExtension.applyLintOptions() {
    lint {
        abortOnError = false
        ignoreWarnings = true
        quiet = true
    }
}

fun LibraryExtension.applyLintOptions() {
    lint {
        abortOnError = false
        ignoreWarnings = true
        quiet = true
    }
}

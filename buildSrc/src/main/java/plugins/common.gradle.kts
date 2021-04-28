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

package plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import config.AndroidConfiguration
import config.CommonModuleDependency
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

val modules = CommonModuleDependency.getLibraryModuleSimpleName()
val features = CommonModuleDependency.getFeatureModuleSimpleName()

subprojects {
    afterEvaluate {
        //region Common Setting
        if (name !in listOf("ext", "feature")) {
            // BaseExtension is common parent for application, library and test modules
            extensions.configure<BaseExtension> {
                compileSdkVersion(AndroidConfiguration.COMPILE_SDK)
                defaultConfig {
                    minSdkVersion(AndroidConfiguration.MIN_SDK)
                    targetSdkVersion(AndroidConfiguration.TARGET_SDK)
                    vectorDrawables.useSupportLibrary = true
                    testInstrumentationRunner = config.AndroidConfiguration.TEST_INSTRUMENTATION_RUNNER
                    consumerProguardFiles(file("consumer-rules.pro"))
                    //region NOTE: This is exceptions, only the library is using room.
                    if (this@subprojects.name in features) {
                        applyRoomSetting()
                    }
                    //endregion
                }
                buildTypes {
                    getByName("release") {
                        // This is exceptions.
                        if (this@subprojects.name == "app") {
                            //                            isMinifyEnabled = true
                            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                                          file("proguard-rules.pro"))
                        }
                    }
                    getByName("debug") {
                        splits.abi.isEnable = false
                        splits.density.isEnable = false
                        aaptOptions.cruncherEnabled = false
                        isTestCoverageEnabled = true
                        // Only use this flag on builds you don't proguard or upload to beta-by-crashlytics.
                        //                        ext.set("alwaysUpdateBuildId", false)
                        isCrunchPngs = false // Enabled by default for RELEASE build type
                    }
                }
                applyLintOptions()
                applyCompileOptions()
                applyTestOptions()
                if (this@subprojects.name !in modules) {
                    buildFeatures.viewBinding = true
                }
            }
        }
        if (name in features + listOf("app", "core")) {
            extensions.configure<KaptExtension> {
                useBuildCache = true
                correctErrorTypes = true
                mapDiagnosticLocations = true
            }
        }
        //endregion
    }
}

fun DefaultConfig.applyRoomSetting() {
    javaCompileOptions {
        annotationProcessorOptions {
            arguments["room.schemaLocation"] = "$projectDir/schemas"
            arguments["room.incremental"] = "true"
            arguments["room.expandProjection"] = "true"
        }
    }
}

fun BaseExtension.applyLintOptions() {
    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = true
        isQuiet = true
    }
}

fun BaseExtension.applyCompileOptions() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

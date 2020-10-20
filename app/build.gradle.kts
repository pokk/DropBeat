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

import config.AndroidConfiguration
import config.CommonModuleDependency
import config.androidTestDependencies
import config.annotationDependencies
import config.appDependencies
import config.unitTestDependencies
import org.jetbrains.kotlin.gradle.internal.CacheImplementation
import resources.FeatureRes

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    defaultConfig {
        applicationId = AndroidConfiguration.ID
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        renderscriptTargetApi = AndroidConfiguration.MIN_SDK
        renderscriptSupportModeEnabled = true
    }
    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
    }
    buildFeatures.viewBinding = true
    dynamicFeatures = CommonModuleDependency.getDynamicFeatureModules()
}

androidExtensions {
    isExperimental = true
    defaultCacheImplementation = CacheImplementation.SPARSE_ARRAY
}

kapt {
    useBuildCache = true
    correctErrorTypes = true
    mapDiagnosticLocations = true
}

dependencies {
    //    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    listOf(project(CommonModuleDependency.LIB_CORE), project(CommonModuleDependency.LIB_MEDIA_PLAYER)).forEach(::api)
    testImplementation(project(CommonModuleDependency.LIB_TEST))
    appDependencies()
    annotationDependencies()
    unitTestDependencies()
    androidTestDependencies()
}

fun com.android.build.gradle.internal.dsl.DefaultConfig.buildConfigField(name: String, value: Set<String>) {
    // Generates String that holds Java String Array code
    val strValue = value.joinToString(separator = ",", prefix = "\"", postfix = "\"")
    buildConfigField("String", name, strValue)
}

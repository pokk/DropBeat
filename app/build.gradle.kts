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

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.build.gradle.internal.dsl.InternalApplicationExtension
import config.AndroidConfiguration
import config.CommonModuleDependency
import utils.androidTestDependencies
import utils.annotationDependencies
import utils.appDependencies
import utils.unitTestDependencies

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("../taiwanno1_release.keystore")
            storePassword = gradleLocalProperties(rootDir).getProperty("store_password")
            keyAlias = gradleLocalProperties(rootDir).getProperty("key_alias")
            keyPassword = gradleLocalProperties(rootDir).getProperty("key_password")
        }
    }
    defaultConfig {
        applicationId = AndroidConfiguration.ID
        versionCode = AndroidConfiguration.VERSION_CODE
        versionName = AndroidConfiguration.VERSION_NAME
        vectorDrawables.useSupportLibrary = true
        renderscriptTargetApi = AndroidConfiguration.MIN_SDK
        renderscriptSupportModeEnabled = true
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
    }
    (this as InternalApplicationExtension).apply {
        setDynamicFeatures(CommonModuleDependency.getDynamicFeatureModules())
    }
}

dependencies {
    //    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    listOf(project(CommonModuleDependency.LIB_CORE),
           project(CommonModuleDependency.LIB_MEDIA_PLAYER),
           project(CommonModuleDependency.LIB_SYNC),
           project(CommonModuleDependency.LIB_PURE_ENTITY),
           project(CommonModuleDependency.LIB_ANALYTICS)).forEach { api(it) }
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

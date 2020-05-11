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

import config.AndroidConfiguration
import config.CommonModuleDependency
import config.Configuration
import config.annotationDependencies

plugins {
    if (config.Configuration.isFeature) {
        id("com.android.application")
    }
    else {
        id("com.android.dynamic-feature")
    }
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(AndroidConfiguration.COMPILE_SDK)
    defaultConfig {
        minSdkVersion(AndroidConfiguration.MIN_SDK)
        targetSdkVersion(AndroidConfiguration.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"
        if (Configuration.isFeature) {
            applicationId = "taiwan.no.one.featureDummy"
        }
        vectorDrawables.useSupportLibrary = true
        renderscriptTargetApi = AndroidConfiguration.MIN_SDK
        testInstrumentationRunner = AndroidConfiguration.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles(file("consumer-rules.pro"))
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mutableMapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            splits.abi.isEnable = false
            splits.density.isEnable = false
            aaptOptions.cruncherEnabled = false
            isMinifyEnabled = false
            isTestCoverageEnabled = false
            // Only use this flag on builds you don't proguard or upload to beta-by-crashlytics.
            ext.set("alwaysUpdateBuildId", false)
            isCrunchPngs = false // Enabled by default for RELEASE build type
        }
    }
    sourceSets {
        getByName("main").apply {
            if (Configuration.isFeature) {
                java.srcDirs("src/main/java", "src/main/alone")
                manifest.srcFile("src/main/alone/AndroidManifest.xml")
            }
        }
    }
    dexOptions {
        jumboMode = true
        preDexLibraries = true
        threadCount = 8
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    if (Configuration.isFeature) {
        packagingOptions {
            exclude("META-INF/atomicfu.kotlin_module")
            exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
        }
    }
    testOptions { unitTests.apply { isReturnDefaultValues = true } }
    lintOptions { isAbortOnError = false }
    kotlinOptions {
        // We have to add the explicit cast before accessing the options itself.
        // If we don't, it does not work: "unresolved reference: jvmTarget"
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = JavaVersion.VERSION_1_8.toString()
        languageVersion = "1.4"
    }
    viewBinding.isEnabled = true
}

kapt {
    useBuildCache = true
    correctErrorTypes = true
    mapDiagnosticLocations = true
}

dependencies {
    implementation(project(CommonModuleDependency.APP))
    annotationDependencies()
}

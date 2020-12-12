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

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import config.AndroidConfiguration
import config.CommonModuleDependency
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
            url = uri("https://maven.fabric.io/public")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath(config.GradleDependency.KOTLIN)
        classpath(config.GradleDependency.SAFE_ARGS)
        classpath(config.GradleDependency.GOOGLE_SERVICE)
        classpath(config.GradleDependency.CRASHLYTICS)
//        classpath "org.jacoco:org.jacoco.core:0.8.4"
//        classpath("io.fabric.tools:gradle:1.31.1")
    }
}

plugins {
    id(config.GradleDependency.DETEKT).version(config.GradleDependency.Version.DETEKT)
    id(config.GradleDependency.GRADLE_VERSION_UPDATER).version(config.GradleDependency.Version.VERSION_UPDATER)
}

dependencies {
    detektPlugins(config.GradleDependency.DETEKT_FORMAT)
}

//region Detekt
val detektVersion = config.GradleDependency.Version.DETEKT
detekt {
    toolVersion = detektVersion
    failFast = true
    debug = true
    parallel = true
    input = files("src/main/java", "src/main/kotlin")
    config = files("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/detekt/baseline.xml")
    buildUponDefaultConfig = true
}
//endregion

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        // required to find the project's artifacts
        maven { url = uri("https://dl.bintray.com/pokk/maven") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://dl.bintray.com/kodein-framework/Kodein-DI") }
    }
}

val modules = CommonModuleDependency.getLibraryModuleSimpleName()
val features = CommonModuleDependency.getFeatureModuleSimpleName()

subprojects {
    beforeEvaluate {
        apply {
            //region Apply plugin
            when (name) {
                "ext" -> {
                    plugin("java-library")
                    plugin("kotlin")
                }
                in modules -> {
                    plugin("com.android.library")
                    plugin("kotlin-android")
                }
                in features -> {
                    plugin("com.android.dynamic-feature")
                    plugin("kotlin-android")
                    plugin("kotlin-parcelize")
                    plugin("kotlin-kapt")
                    plugin("androidx.navigation.safeargs.kotlin")
                }
            }
            if (this@subprojects.name == "core") {
                plugin("org.jetbrains.kotlin.kapt")
            }
            plugin(config.GradleDependency.DETEKT)
            //endregion
        }
    }

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
                        if (this@subprojects.name !in features) {
                            isMinifyEnabled = true
                        }
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                                      file("proguard-rules.pro"))
                    }
                    getByName("debug") {
                        splits.abi.isEnable = false
                        splits.density.isEnable = false
                        aaptOptions.cruncherEnabled = false
                        isTestCoverageEnabled = true
                        // Only use this flag on builds you don't proguard or upload to beta-by-crashlytics.
                        ext.set("alwaysUpdateBuildId", false)
                        isCrunchPngs = false // Enabled by default for RELEASE build type
                    }
                }
                applyDexOptions()
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

    tasks {
        withType<Detekt> {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            exclude(".*/resources/.*", ".*/build/.*") // but exclude our legacy internal package
        }
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
                suppressWarnings = false
                freeCompilerArgs = listOf(
                    "-Xuse-experimental=kotlin.Experimental",
                    "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
                    "-Xuse-experimental=kotlin.ExperimentalContracts",
                    "-Xuse-experimental=org.mylibrary.ExperimentalMarker",
                    "-Xallow-result-return-type",
                    "-Xjvm-default=all"
                )
            }
        }
    }

//    tasks.whenObjectAdded {
//        if (
//            name.contains("lint") ||
//            name == "clean" ||
//            name.contains("jacoco") ||
//            name.contains("lintVitalRelease") ||
//            name.contains("Aidl") ||
//            name.contains("mockableAndroidJar") ||
//            name.contains("UnitTest") ||
//            name.contains("AndroidTest") ||
//            name.contains("Ndk") ||
//            name.contains("Jni")
//        ) {
//            enabled = false
//        }
//    }
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

fun BaseExtension.applyDexOptions() {
    dexOptions {
        jumboMode = true
        preDexLibraries = true
        threadCount = 8
    }
}

fun BaseExtension.applyCompileOptions() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

fun BaseExtension.applyLintOptions() {
    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = true
        isQuiet = true
    }
}

fun BaseExtension.applyTestOptions() {
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// pre-requirement: brew install graphviz
// ./gradlew projectDependencyGraph
apply(file("gradle/projectDependencyGraph.gradle"))

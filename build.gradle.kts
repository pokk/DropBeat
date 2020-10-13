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

import config.CommonModuleDependency

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
        classpath("com.android.tools.build:gradle:4.1.0")
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
    println(CommonModuleDependency.getLibraryModuleSimpleName())
    //region Apply plugin
    apply {
        when (this@subprojects.name) {
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
                plugin("kotlin-android-extensions")
                plugin("kotlin-kapt")
                plugin("androidx.navigation.safeargs.kotlin")
            }
        }
        if (this@subprojects.name == "core") {
            plugin("kotlin-android-extensions")
            plugin("org.jetbrains.kotlin.kapt")
        }
        afterEvaluate {
            if (this@subprojects.name !in listOf("ext", "feature")) {
                // BaseExtension is common parent for application, library and test modules
                extensions.configure(com.android.build.gradle.BaseExtension::class.java) {
                    compileSdkVersion(config.AndroidConfiguration.COMPILE_SDK)
                    defaultConfig {
                        minSdkVersion(config.AndroidConfiguration.MIN_SDK)
                        targetSdkVersion(config.AndroidConfiguration.TARGET_SDK)
                        testInstrumentationRunner = config.AndroidConfiguration.TEST_INSTRUMENTATION_RUNNER
                        consumerProguardFiles(file("consumer-rules.pro"))
                        //region NOTE: This is exceptions, only the library is using room.
                        if (this@subprojects.name in listOf("library", "search", "ranking")) {
                            javaCompileOptions {
                                annotationProcessorOptions {
                                    arguments["room.schemaLocation"] = "$projectDir/schemas"
                                    arguments["room.incremental"] = "true"
                                    arguments["room.expandProjection"] = "true"
                                }
                            }
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
                    dexOptions {
                        jumboMode = true
                        preDexLibraries = true
                        threadCount = 8
                    }
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_1_8
                        targetCompatibility = JavaVersion.VERSION_1_8
                    }
                    lintOptions {
                        isAbortOnError = false
                        isIgnoreWarnings = true
                        isQuiet = true
                    }
                    testOptions {
                        unitTests.isReturnDefaultValues = true
                    }
                }
            }
        }
        plugin(config.GradleDependency.DETEKT)
    }
    //endregion

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

    tasks {
        withType<io.gitlab.arturbosch.detekt.Detekt> {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            exclude(".*/resources/.*", ".*/build/.*") // but exclude our legacy internal package
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
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

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

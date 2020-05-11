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
        classpath("com.android.tools.build:gradle:4.0.0-beta05")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath(config.GradleDependency.SAFE_ARGS)
        classpath(config.GradleDependency.GOOGLE_SERVICE)
//        classpath "org.jacoco:org.jacoco.core:0.8.4"
//        classpath("io.fabric.tools:gradle:1.31.1")
    }
}

plugins {
    id(config.GradleDependency.DETEKT).version(config.GradleDependency.Version.DETEKT)
    id(config.GradleDependency.GRADLE_VERSION_UPDATER).version(config.GradleDependency.Version.VERSION_UPDATER)
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

    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                suppressWarnings = false
                freeCompilerArgs = listOf(
                    "-Xuse-experimental=kotlin.Experimental",
                    "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
                    "-Xuse-experimental=kotlin.ExperimentalContracts",
                    "-Xuse-experimental=org.mylibrary.ExperimentalMarker",
                    "-Xallow-result-return-type"
                )
            }
        }
    }
}

subprojects {
    //region Apply plugin
    apply {
        when (name) {
            "domain", "ext" -> {
                plugin("java-library")
                plugin("kotlin")
            }
            "widget", "ktx", "device", "core" -> {
                plugin("com.android.library")
                plugin("kotlin-android")
            }
        }
        if (name == "core") {
            plugin("kotlin-android-extensions")
            plugin("org.jetbrains.kotlin.kapt")
        }
        plugin(config.GradleDependency.DETEKT)
    }
    //endregion

    //region Detekt
    val detektVersion = config.GradleDependency.Version.DETEKT
    detekt {
        toolVersion = detektVersion
        debug = true
        parallel = true
        input = files("src/main/java")
        config = files("$rootDir/detekt.yml")

        idea {
            path = "$rootDir/.idea"
            codeStyleScheme = "$rootDir/.idea/idea-code-style.xml"
            inspectionsProfile = "$rootDir/.idea/inspect.xml"
            mask = "*.kt"
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
        exclude(".*/resources/.*", ".*/build/.*") // but exclude our legacy internal package
    }
    //endregion

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

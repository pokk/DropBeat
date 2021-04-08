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

import utils.addDefaults

// Top-level build file where you can add configuration options common to all sub-projects/modules.
// plugins.apply("plugins.git-hooks")
plugins.apply("plugins.header")
plugins.apply("plugins.common")
plugins.apply("plugins.kotlin")
plugins.apply("plugins.detekt")
plugins.apply("plugins.ktlint")
plugins.apply("plugins.update-dependency")

buildscript {
    repositories {
        google()
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
            url = uri("https://maven.fabric.io/public")
        }
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath(config.GradleDependency.KOTLIN)
        classpath(config.GradleDependency.SAFE_ARGS)
        classpath(config.GradleDependency.GOOGLE_SERVICE)
        classpath(config.GradleDependency.CRASHLYTICS)
        classpath(config.GradleDependency.PERFORMANCE)
    }
}

allprojects {
    repositories.addDefaults()
}

subprojects {
    beforeEvaluate {
        // TODO(jieyi): [app] has some issues for jacoco.
        if (name !in listOf("ext", "app", "test", "feature")) plugins.apply("plugins.jacoco")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// pre-requirement: brew install graphviz
// ./gradlew projectDependencyGraph
apply(file("gradle/projectDependencyGraph.gradle"))

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

plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.withType<Test> {
    // Jacoco is back [https://issuetracker.google.com/issues/171125857]
    maxHeapSize = "4g"
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

private val classDirectoriesTree = fileTree("${project.buildDir}") {
    include(
        "**/classes/**/main/**",
        "**/intermediates/classes/debug/**",
        "**/intermediates/javac/debug/*/classes/**", // Android Gradle Plugin 3.2.x support.
        "**/tmp/kotlin-classes/debug/**",
    )
    exclude(
        "**/R.class",
        "**/R\$*.class",
        "**/*\$1*",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/models/**",
        "**/*\$Lambda$*.*",
        "**/*\$inlined$*.*",
    )
}
private val sourceDirectoriesTree = files("$projectDir/src/main/java")
private val executionDataTree = fileTree("${project.buildDir}") {
    include(
        "outputs/code_coverage/**/*.ec",
        "jacoco/jacocoTestReportDebug.exec",
        "jacoco/testDebugUnitTest.exec",
        "jacoco/test.exec",
    )
}
private val xmlReportPath = "$buildDir/reports/code-coverage/jacocoTestReport.xml"
private val htmlReportPath = "$buildDir/reports/code-coverage/html"
val jacocoGroup = "verification"
val taskJacocoAndroidTestReport = "jacocoTestReport"
val taskJacocoAndroidCoverageVerification = "jacocoCoverageVerification"
val minimumCoverage = "0.5".toBigDecimal()

fun JacocoReportsContainer.reports() {
    csv.required.set(false)
    xml.apply {
        required.set(true)
        outputLocation.set(file(xmlReportPath))
    }
    html.apply {
        required.set(true)
        outputLocation.set(file(htmlReportPath))
    }
}

fun JacocoReport.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

fun JacocoCoverageVerification.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

tasks.register<JacocoReport>(taskJacocoAndroidTestReport) {
    group = jacocoGroup
    description = "Code coverage report for both Android and Unit tests."
    dependsOn("testDebugUnitTest")
    reports {
        reports()
    }
    setDirectories()
}

tasks.register<JacocoCoverageVerification>(taskJacocoAndroidCoverageVerification) {
    group = jacocoGroup
    description = "Code coverage verification for Android both Android and Unit tests."
    dependsOn("testDebugUnitTest")
    violationRules {
        rule {
            limit {
                minimum = minimumCoverage
            }
        }
        rule {
            element = "CLASS"
            excludes = listOf(
                "**.FactorFacade.Builder",
                "**.ServiceFacade.Builder",
                "**.ChallengeFacade.Builder",
                "**.Task",
            )
            limit {
                minimum = minimumCoverage
            }
        }
    }
    setDirectories()
}

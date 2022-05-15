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

package plugins

plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.withType<Test> {
    // Jacoco is back [https://issuetracker.google.com/issues/171125857]
    maxHeapSize = "3g"
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

private val sourceDirectoriesTree = fileTree(project.projectDir) {
    setIncludes(listOf("src/main/java/**", "src/main/kotlin/**", "src/debug/java/**", "src/debug/kotlin/**"))
}

private val classDirectoriesTree = fileTree("$buildDir/tmp/kotlin-classes/debug") {
    setExcludes(
        listOf(
            "**/R.class",
            "**/R\$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*"
        )
    )
}

private val executionDataTree = fileTree(buildDir) {
    setIncludes(listOf("jacoco/testDebugUnitTest.exec", "outputs/code_coverage/connected/*coverage.ec"))
}

fun JacocoReportsContainer.reports() {
    xml.required.set(true)
    html.required.set(true)
    csv.required.set(false)
}

fun JacocoReport.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree, "${project.projectDir}/jacoco.exec")
}

fun JacocoCoverageVerification.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree, "${project.projectDir}/jacoco.exec")
}

private val taskJacocoAndroidTestReport = "jacocoAndroidTestReport"
private val taskJacocoAndroidCoverageVerification = "jacocoAndroidCoverageVerification"

if (tasks.findByName(taskJacocoAndroidTestReport) == null) {
    tasks.register<JacocoReport>(taskJacocoAndroidTestReport) {
        group = BuildTaskGroups.REPORTING
        description = "Code coverage report for both Android and Unit tests."
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")
        reports {
            reports()
        }
        setDirectories()
    }
}

if (tasks.findByName(taskJacocoAndroidCoverageVerification) == null) {
    tasks.register<JacocoCoverageVerification>(taskJacocoAndroidCoverageVerification) {
        group = BuildTaskGroups.REPORTING
        description = "Code coverage verification for Android both Android and Unit tests."
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")
        violationRules {
            rule {
                limit {
                    counter = "INSTRUCTIONAL"
                    value = "COVEREDRATIO"
                    minimum = "0.5".toBigDecimal()
                }
            }
        }
        setDirectories()
    }
}

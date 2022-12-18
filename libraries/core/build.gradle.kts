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

import config.CommonModuleDependency

android {
    namespace = "taiwan.no.one.core"
    buildFeatures.buildConfig = true
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

dependencies {
    // api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    listOf(project(CommonModuleDependency.LIB_KTX), project(CommonModuleDependency.LIB_DEVICE)).forEach { api(it) }

    api(coreLibs.bundles.kotlin.android)

    api(libs.bundles.androidx.bom)
    api(libs.bundles.internet)
    api(libs.bundles.database)
    api(libs.bundles.moshi)
    api(libs.di.kodein)
    api(libs.autoservice)

    ksp(libs.autoservice.ksp)
    ksp(libs.bundles.annotation.ksp)

    testImplementation(project(CommonModuleDependency.LIB_TEST))
    testImplementation(testLibs.bundles.test)

    androidTestImplementation(testLibs.bundles.android.test)
}

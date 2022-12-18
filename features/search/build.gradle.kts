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

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import config.CommonModuleDependency

android {
    namespace = "taiwan.no.one.feat.search"
    buildTypes {
        getByName("release") {
            buildConfigField(
                "String",
                "SeekSongUriDomain",
                gradleLocalProperties(rootDir).getProperty("seek_song_uri_domain"),
            )
            buildConfigField(
                "String",
                "SeekSongUriRequest",
                gradleLocalProperties(rootDir).getProperty("seek_song_uri_request"),
            )
            buildConfigField(
                "String",
                "SearchMusicQuery1",
                gradleLocalProperties(rootDir).getProperty("seek_song_query_1"),
            )
            buildConfigField(
                "String",
                "SearchMusicParameter1",
                gradleLocalProperties(rootDir).getProperty("seek_song_param_1"),
            )
            buildConfigField(
                "String",
                "SearchMusicQuery2",
                gradleLocalProperties(rootDir).getProperty("seek_song_param_2"),
            )
            buildConfigField(
                "String",
                "SearchMusicParameter2",
                gradleLocalProperties(rootDir).getProperty("seek_song_param_2"),
            )
        }
        getByName("debug") {
            buildConfigField(
                "String",
                "SeekSongUriDomain",
                gradleLocalProperties(rootDir).getProperty("seek_song_uri_domain"),
            )
            buildConfigField(
                "String",
                "SeekSongUriRequest",
                gradleLocalProperties(rootDir).getProperty("seek_song_uri_request"),
            )
            buildConfigField(
                "String",
                "SearchMusicQuery1",
                gradleLocalProperties(rootDir).getProperty("seek_song_query_1"),
            )
            buildConfigField(
                "String",
                "SearchMusicParameter1",
                gradleLocalProperties(rootDir).getProperty("seek_song_param_1"),
            )
            buildConfigField(
                "String",
                "SearchMusicQuery2",
                gradleLocalProperties(rootDir).getProperty("seek_song_param_2"),
            )
            buildConfigField(
                "String",
                "SearchMusicParameter2",
                gradleLocalProperties(rootDir).getProperty("seek_song_param_2"),
            )
        }
    }
    buildFeatures.buildConfig = true
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

dependencies {
    implementation(project(CommonModuleDependency.APP))
    ksp(libs.bundles.annotation.ksp)
}

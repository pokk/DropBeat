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
// NOTE(jieyi): New sub-project need to be added here!
include(":app", ":ext")
include(
    ":libraries:analytics",
    ":libraries:core",
    ":libraries:device",
    ":libraries:entity",
    ":libraries:mediaplayer",
    ":libraries:test",
    ":libraries:sync",
    ":libraries:widget",
    ":libraries:ktx",
)
include(
    ":features:search",
    ":features:ranking",
    ":features:login",
    ":features:library",
    ":features:explore",
    ":features:player",
    ":features:setting",
)

dependencyResolutionManagement {
    versionCatalogs {
        create("lib") {
            from(files("gradle/libs.versions.toml"))
        }
        create("coreLibs") {
            from(files("gradle/core-libs.versions.toml"))
        }
        create("testLibs") {
            from(files("gradle/test-libs.versions.toml"))
        }
        create("debugLibs") {
            from(files("gradle/debug-libs.versions.toml"))
        }
    }
}

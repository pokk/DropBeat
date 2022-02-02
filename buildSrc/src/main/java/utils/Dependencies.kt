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

package utils

import config.CoreDependency
import config.DebugDependency
import config.LibraryDependency.Analytics
import config.LibraryDependency.AndroidKtx
import config.LibraryDependency.Database
import config.LibraryDependency.Di
import config.LibraryDependency.Firebase
import config.LibraryDependency.Internet
import config.LibraryDependency.JetPack
import config.LibraryDependency.Jieyi
import config.LibraryDependency.Media
import config.LibraryDependency.Tool
import config.LibraryDependency.Ui
import config.TestLibraryDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope

const val DepEnvImpl = "implementation"
const val DepEnvApi = "api"
const val DepEnvDebugApi = "debugApi"
const val DepEnvKapt = "kapt"
const val DepEnvKsp = "ksp"
const val DepEnvTest = "testImplementation"
const val DepEnvAndroidTest = "androidTestImplementation"

// This is only for auto-service.
const val DepAnnotationProcessor = "annotationProcessor"

/**********************************************************
 * Below functions will be used in each Gradles directly. *
 **********************************************************/

fun DependencyHandlerScope.annotationDependencies() {
    DepEnvKapt(Database.ROOM_ANNOTATION)
    DepEnvKapt(JetPack.LIFECYCLE_COMPILER)
    DepEnvKapt(Tool.AUTO_SERVICE)
    DepEnvKsp(Tool.MOSHI_CODEGEN)
}

fun DependencyHandlerScope.coreDependencies() {
    kotlinAndroidDependencies(DepEnvApi)
    commonAndroidxDependencies(DepEnvApi)
    androidxUiDependencies(DepEnvApi)
    diDependencies(DepEnvApi)
    internetDependencies(DepEnvApi)
    localDependencies(DepEnvApi)
    // Others
    DepEnvApi(Tool.MOSHI)
    DepEnvApi(Tool.MOSHI_ADAPTER)
    DepEnvApi(Tool.MOSHI_KOTLIN)
}

fun DependencyHandlerScope.appDependencies() {
    androidxKtxDependencies(DepEnvApi)
    uiDependencies(DepEnvApi)
    firebaseDependencies(DepEnvApi)
    // Others
    DepEnvApi(Firebase.PLAY_CORE)
    DepEnvApi(Jieyi.KNIFER)
    DepEnvApi(Database.MMKV)
    DepEnvApi(Internet.COIL)
}

fun DependencyHandlerScope.ktxDependencies() {
    commonAndroidxDependencies(DepEnvImpl)
}

fun DependencyHandlerScope.widgetDependencies() {
    kotlinAndroidDependencies(DepEnvImpl)
    commonAndroidxDependencies(DepEnvImpl)
    // Auto Service
    DepEnvApi(Tool.AUTO_SERVICE)
    // Others
    DepEnvImpl(JetPack.RECYCLERVIEW)
    DepEnvImpl(JetPack.PAGING)
    DepEnvImpl(JetPack.MATERIAL_DESIGN)
    DepEnvImpl(JetPack.CARDVIEW)
    DepEnvImpl(JetPack.CONSTRAINT_LAYOUT)
    DepEnvImpl(Ui.LOTTIE)
    //    DepEnvImpl(Deps.Presentation.arv)
    //    DepEnvImpl(Deps.Widget.quickDialog)
}

fun DependencyHandlerScope.settingDependencies() {
    // Others
    DepEnvImpl(JetPack.DATASTORE)
}

fun DependencyHandlerScope.mediaDependencies() {
    kotlinAndroidDependencies(DepEnvImpl)
    commonAndroidxDependencies(DepEnvImpl)
    DepEnvImpl(Media.EXOPLAYER_CORE)
}

fun DependencyHandlerScope.firebaseAuthDependencies() {
    DepEnvImpl(Firebase.FIREBASE_AUTH_GOOGLE)
    DepEnvImpl(Firebase.FIREBASE_AUTH_FACEBOOK)
}

fun DependencyHandlerScope.analyticsDependencies() {
    kotlinDependencies(DepEnvImpl)
    DepEnvApi(platform(Firebase.FIREBASE_BOM))
    DepEnvApi(Analytics.FIREBASE_ANALYTICS)
    DepEnvApi(Analytics.SENTRY)
}

fun DependencyHandlerScope.syncDependencies() {
    kotlinDependencies(DepEnvImpl)
    DepEnvImpl(JetPack.DATASTORE)
    DepEnvApi(platform(Firebase.FIREBASE_BOM))
    DepEnvApi(Firebase.FIREBASE_FIRESTORE)
}

fun DependencyHandlerScope.testDependencies() {
    kotlinDependencies(DepEnvImpl)
    DepEnvImpl(TestLibraryDependency.JUNIT)
    DepEnvImpl(TestLibraryDependency.COROUTINE)
    DepEnvImpl(TestLibraryDependency.ESPRESSO_CORE)

    DepEnvImpl(JetPack.MATERIAL_DESIGN)
}

/************************************
 * Below functions are for modeling *
 ************************************/

fun DependencyHandlerScope.kotlinDependencies(env: String) {
    env(CoreDependency.KOTLIN)
    env(CoreDependency.KOTLIN_REFLECT)
    env(CoreDependency.KOTLIN_COROUTINE)
    env(CoreDependency.KOTLIN_DATETIME)
}

fun DependencyHandlerScope.kotlinAndroidDependencies(env: String) {
    kotlinDependencies(env)
    env(CoreDependency.ANDROID_COROUTINE)
    env(CoreDependency.GOOGLE_PLAY_COROUTINE) // might only move the module need.
}

fun DependencyHandlerScope.androidJetpackDependencies(env: String) {
    env(JetPack.APPCOMPAT)
    env(JetPack.LIFECYCLE_SAVEDSTATE)
    env(JetPack.LIFECYCLE_SERVICE)
    env(JetPack.NAVIGATION_DYNAMIC_FEATURE)
    env(JetPack.RECYCLERVIEW)
    env(JetPack.PAGING)
}

fun DependencyHandlerScope.commonKtxDependencies(env: String) {
    env(AndroidKtx.KTX)
    env(AndroidKtx.ACTIVITY_KTX)
    env(AndroidKtx.FRAGMENT_KTX)
    env(AndroidKtx.VIEWMODEL_KTX)
    env(AndroidKtx.LIVEDATA_KTX)
    env(AndroidKtx.RUNTIME_KTX)
}

fun DependencyHandlerScope.androidxKtxDependencies(env: String) {
    commonKtxDependencies(env)
    env(AndroidKtx.PALETTE_KTX)
    env(AndroidKtx.COLLECTION_KTX)
    env(AndroidKtx.NAVIGATION_FRAGMENT_KTX)
    env(AndroidKtx.NAVIGATION_UI_KTX)
    env(AndroidKtx.WORKER_KTX)
    env(JetPack.APP_STARTUP)
}

fun DependencyHandlerScope.commonAndroidxDependencies(env: String) {
    androidJetpackDependencies(env)
    commonKtxDependencies(env)
}

fun DependencyHandlerScope.androidxUiDependencies(env: String) {
    env(JetPack.MATERIAL_DESIGN)
    env(JetPack.RECYCLERVIEW)
    env(JetPack.PAGING)
    env(JetPack.CARDVIEW)
    env(JetPack.COORDINATOR_LAYOUT)
    env(JetPack.CONSTRAINT_LAYOUT)
    env(JetPack.ANNOT)
}

fun DependencyHandlerScope.diDependencies(env: String) {
    env(Di.KODEIN_ANDROID_X)
}

fun DependencyHandlerScope.internetDependencies(env: String) {
    env(Internet.OKHTTP)
    env(Internet.OKHTTP_INTERCEPTOR)
    env(Internet.RETROFIT2)
    env(Internet.RETROFIT2_CONVERTER_MOSHI)
}

fun DependencyHandlerScope.firebaseDependencies(env: String) {
    env(platform(Firebase.FIREBASE_BOM))
    env(Firebase.FIREBASE_CRASHLYTICS)
    env(Firebase.FIREBASE_PERFORMANCE)
    env(Firebase.FIREBASE_CONFIG)
    env(Firebase.FIREBASE_MESSAGING)
    env(Firebase.FIREBASE_DB)
    env(Firebase.FIREBASE_FIRESTORE)
    env(Firebase.FIREBASE_AUTH)
    env(Firebase.FIREBASE_AUTH_FACEBOOK)
}

fun DependencyHandlerScope.localDependencies(env: String) {
    env(Database.ROOM)
    env(Database.ROOM_KTX)
}

fun DependencyHandlerScope.uiDependencies(env: String) {
    env(Ui.LOTTIE)
    env(Ui.SHAPE_OF_VIEW)
    // env(Jieyi.ARV)
}

fun DependencyHandlerScope.debugDependencies(env: String) {
    env(DebugDependency.STEHO)
    env(DebugDependency.STEHO_INTERCEPTOR)
    env(DebugDependency.LEAKCANARY)
    // env(DebugDependency.DEBUG_DB)
    // env(DebugDependency.OK_HTTP_PROFILER)
}

fun DependencyHandlerScope.unitTestDependencies(env: String = DepEnvTest) {
    env(TestLibraryDependency.JUNIT)
    env(TestLibraryDependency.COROUTINE)
    env(TestLibraryDependency.ASSERTK)
    env(TestLibraryDependency.MOCKK)
    env(TestLibraryDependency.OKHTTP_SERVER)
}

fun DependencyHandlerScope.androidTestDependencies(env: String = DepEnvAndroidTest) {
    env(TestLibraryDependency.ANDROID_JUNIT)
    env(TestLibraryDependency.ESPRESSO_CORE)
    env(TestLibraryDependency.ESPRESSO_INTENT)
    env(TestLibraryDependency.ESPRESSO_IDLING)
    env(TestLibraryDependency.CORE_KTX)
    env(TestLibraryDependency.ARCH_CORE)
    env(TestLibraryDependency.NAVIGATION)
    env(TestLibraryDependency.FRAGMENT)
    env(TestLibraryDependency.ASSERTK)
    env(TestLibraryDependency.MOCKK_ANDROID)
    env(TestLibraryDependency.ANDROID_JUNIT)
}

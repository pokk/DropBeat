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

package config

object LibraryDependency {
    object Version {
        const val ARV = "1.0.16"
        const val KINFER = "2.4.0"
        const val MATERIAL = "1.7.0-alpha01"
        const val ANDROIDX = "1.0.0"
        const val ANNOTATION = "1.4.0-alpha02"
        const val APPCOMPAT = "1.6.0-alpha03"
        const val APP_STARTUP = "1.2.0-alpha01"
        const val CARDVIEW = ANDROIDX
        const val RECYCLERVIEW = "1.3.0-alpha02"
        const val PAGING = "3.1.1"
        const val CONSTRAINTLAYOUT = "2.1.3"
        const val COORDINATORLAYOUT = "1.2.0"
        const val AAC_LIFECYCLE = "2.5.0-beta01"
        const val DATASTORE = "1.0.0"
        const val KODEIN = "7.11.0"
        const val KTX = "1.9.0-alpha03"
        const val ACTIVITY_KTX = "1.6.0-alpha03"
        const val FRAGMENT_KTX = "1.5.0-beta01"
        const val PALETTE_KTX = "1.0.0"
        const val COLLECTION_KTX = "1.2.0"
        const val NAVIGATION_KTX = "2.5.0-beta01"
        const val WORK_KTX = "2.8.0-alpha02"
        const val DYN_ANIM_KTX = "1.0.0-alpha01"
        const val ROOM = "2.5.0-alpha01"
        const val MMKV = "1.2.13"
        const val PLAY_CORE = "1.10.3"
        const val COIL = "2.0.0-rc03"
        const val RETROFIT2 = "2.9.0"
        const val OKHTTP3 = "5.0.0-alpha.7"
        const val JSOUP = "1.14.3"
        const val MOSHI = "1.13.0"
        const val AUTO_SERVICE = "1.0.1"
        const val FIREBASE_BOM = "30.0.0"
        const val FIREBASE_AUTH_GOOGLE = "20.2.0"
        const val FIREBASE_AUTH_FACEBOOK = "13.2.0"
        const val EXOPLAYER = "2.17.1"
        const val SHAPE_OF_VIEW = "1.4.7"
        const val REALTIME_BLUR = "1.2.1"
        const val LOTTIE = "5.1.1"
        const val SENTRY = "6.0.0-beta.3"
    }

    object Jieyi {
        const val KNIFER = "studio.pokkbaby:kotlinknifer:${Version.KINFER}"
        const val ARV = "com.devrapid.jieyi:adaptiverecyclerview:${Version.ARV}"
    }

    object Tool {
        const val JSOUP = "org.jsoup:jsoup:${Version.JSOUP}"
        const val MOSHI = "com.squareup.moshi:moshi:${Version.MOSHI}"
        const val MOSHI_ADAPTER = "com.squareup.moshi:moshi-adapters:${Version.MOSHI}"
        const val MOSHI_KOTLIN = "com.squareup.moshi:moshi-kotlin:${Version.MOSHI}"
        const val MOSHI_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:${Version.MOSHI}"

        const val AUTO_SERVICE = "com.google.auto.service:auto-service:${Version.AUTO_SERVICE}"
    }

    object JetPack {
        const val MATERIAL_DESIGN = "com.google.android.material:material:${Version.MATERIAL}"
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Version.APPCOMPAT}"
        const val APP_STARTUP = "androidx.startup:startup-runtime:${Version.APP_STARTUP}"
        const val ANNOT = "androidx.annotation:annotation:${Version.ANNOTATION}"
        const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Version.RECYCLERVIEW}"
        const val PAGING = "androidx.paging:paging-runtime:${Version.PAGING}"
        const val CARDVIEW = "androidx.cardview:cardview:${Version.CARDVIEW}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINTLAYOUT}"
        const val COORDINATOR_LAYOUT = "androidx.coordinatorlayout:coordinatorlayout:${Version.COORDINATORLAYOUT}"
        const val LIFECYCLE_SAVEDSTATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.AAC_LIFECYCLE}"
        const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Version.AAC_LIFECYCLE}"
        const val LIFECYCLE_SERVICE = "androidx.lifecycle:lifecycle-service:${Version.AAC_LIFECYCLE}"
        const val NAVIGATION_DYNAMIC_FEATURE =
            "androidx.navigation:navigation-dynamic-features-fragment:${Version.NAVIGATION_KTX}"
        const val DATASTORE = "androidx.datastore:datastore-preferences:${Version.DATASTORE}"
    }

    object AndroidKtx {
        const val KTX = "androidx.core:core-ktx:${Version.KTX}"
        const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Version.ACTIVITY_KTX}"
        const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Version.FRAGMENT_KTX}"
        const val PALETTE_KTX = "androidx.palette:palette-ktx:${Version.PALETTE_KTX}"
        const val COLLECTION_KTX = "androidx.collection:collection-ktx:${Version.COLLECTION_KTX}"
        const val VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.AAC_LIFECYCLE}"
        const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.AAC_LIFECYCLE}"
        const val RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.AAC_LIFECYCLE}"
        const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:${Version.NAVIGATION_KTX}"
        const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${Version.NAVIGATION_KTX}"
        const val WORKER_KTX = "androidx.work:work-runtime-ktx:${Version.WORK_KTX}"
        const val DYN_ANIM_KTX = "androidx.dynamicanimation-ktx:${Version.DYN_ANIM_KTX}"
    }

    object Database {
        const val ROOM = "androidx.room:room-runtime:${Version.ROOM}"
        const val ROOM_KTX = "androidx.room:room-ktx:${Version.ROOM}"
        const val ROOM_ANNOTATION = "androidx.room:room-compiler:${Version.ROOM}"
        const val MMKV = "com.tencent:mmkv-static:${Version.MMKV}"
    }

    object Di {
        const val KODEIN_ANDROID_X = "org.kodein.di:kodein-di-framework-android-x:${Version.KODEIN}"
    }

    object Internet {
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Version.OKHTTP3}"
        const val OKHTTP_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Version.OKHTTP3}"
        const val RETROFIT2 = "com.squareup.retrofit2:retrofit:${Version.RETROFIT2}"
        const val RETROFIT2_CONVERTER_MOSHI = "com.squareup.retrofit2:converter-moshi:${Version.RETROFIT2}"
        const val COIL = "io.coil-kt:coil:${Version.COIL}"
    }

    object Firebase {
        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Version.FIREBASE_BOM}"
        const val PLAY_CORE = "com.google.android.play:core:${Version.PLAY_CORE}"
        const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx"
        const val FIREBASE_AUTH_GOOGLE = "com.google.android.gms:play-services-auth:${Version.FIREBASE_AUTH_GOOGLE}"
        const val FIREBASE_AUTH_FACEBOOK = "com.facebook.android:facebook-login:${Version.FIREBASE_AUTH_FACEBOOK}"
        const val FIREBASE_DB = "com.google.firebase:firebase-database-ktx"
        const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
        const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging-ktx"
        const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
        const val FIREBASE_PERFORMANCE = "com.google.firebase:firebase-perf-ktx"
        const val FIREBASE_CONFIG = "com.google.firebase:firebase-config-ktx"
        const val FIREBASE_ML = "com.google.firebase:firebase-ml-vision"
    }

    object Analytics {
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
        const val SENTRY = "io.sentry:sentry-android:${Version.SENTRY}"
    }

    object Media {
        const val EXOPLAYER_CORE = "com.google.android.exoplayer:exoplayer-core:${Version.EXOPLAYER}"
    }

    object Ui {
        const val SHAPE_OF_VIEW = "io.github.florent37:shapeofview:${Version.SHAPE_OF_VIEW}"
        const val REALTIME_BLUR = "com.github.mmin18:realtimeblurview:${Version.REALTIME_BLUR}"
        const val LOTTIE = "com.airbnb.android:lottie:${Version.LOTTIE}"
    }
}

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

package config

object LibraryDependency {
    object Version {
        const val ARV = "1.0.16"
        const val QUICK_DIALOG = "1.0.7"
        const val KINFER = "2.1.19"
        const val MATERIAL = "1.2.0-alpha06"
        const val ANDROIDX = "1.0.0"
        const val ANNOTATION = "1.1.0"
        const val APPCOMPAT = "1.2.0-beta01"
        const val CARDVIEW = ANDROIDX
        const val RECYCLERVIEW = "1.2.0-alpha03"
        const val CONSTRAINTLAYOUT = "2.0.0-beta5"
        const val COORDINATORLAYOUT = "1.1.0"
        const val AAC_LIFECYCLE = "2.3.0-alpha02"
        const val KODEIN = "6.5.5"
        const val KTX = "1.3.0-rc01"
        const val ACTIVITY_KTX = "1.2.0-alpha04"
        const val FRAGMENT_KTX = "1.3.0-alpha04"
        const val PALETTE_KTX = "1.0.0"
        const val COLLECTION_KTX = "1.1.0"
        const val NAVIGATION_KTX = "2.3.0-alpha06"
        const val WORK_KTX = "2.4.0-alpha03"
        const val DYN_ANIM_KTX = "1.0.0-alpha01"
        const val ROOM = "2.2.5"
        const val MMKV = "1.1.1"
        const val GSON = "2.8.6"
        const val PLAY_CORE = "1.7.2"
        const val COIL = "0.10.1"
        const val RETROFIT2 = "2.8.1"
        const val OKHTTP3 = "4.6.0"
        const val JSOUP = "1.12.1"
        const val FIREBASE_CORE = "17.2.2"
        const val FIREBASE_DATABASE = "16.1.0"
        const val FIREBASE_AUTH = "16.0.3"
        const val FIREBASE_MESSAGING = "17.5.0"
        const val FIREBASE_ANALYTICS = "17.2.2"
        const val FIREBASE_CRASHLYTICS = "2.10.1"
        const val FIREBASE_ML = "24.0.1"
        const val SHAPE_OF_VIEW = "1.4.7"
        const val REALTIME_BLUR = "1.2.1"
        const val LOTTIE = "3.4.0"
    }

    object Jieyi {
        const val KNIFER = "com.devrapid.jieyi:kotlinknifer:${Version.KINFER}"
        const val ARV = "com.devrapid.jieyi:adaptiverecyclerview:${Version.ARV}"
        const val QUICK_DIALOG = "com.devrapid.jieyi:dialogbuilder:${Version.QUICK_DIALOG}"
    }

    object Tool {
        const val GSON = "com.google.code.gson:gson:${Version.GSON}"
        const val JSOUP = "org.jsoup:jsoup:${Version.JSOUP}"
//    const val CLOUDINARY = "com.cloudinary:cloudinary-android:${Version.cloudinary}"
    }

    object JetPack {
        const val MATERIAL_DESIGN = "com.google.android.material:material:${Version.MATERIAL}"
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Version.APPCOMPAT}"
        const val ANNOT = "androidx.annotation:annotation:${Version.ANNOTATION}"
        const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Version.RECYCLERVIEW}"
        const val CARDVIEW = "androidx.cardview:cardview:${Version.CARDVIEW}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINTLAYOUT}"
        const val COORDINATOR_LAYOUT = "androidx.coordinatorlayout:coordinatorlayout:${Version.COORDINATORLAYOUT}"
        const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Version.AAC_LIFECYCLE}"
        const val LIFECYCLE_SERVICE = "androidx.lifecycle:lifecycle-service:${Version.AAC_LIFECYCLE}"
        const val LIFECYCLE_PROCESS = "androidx.lifecycle:lifecycle-process:${Version.AAC_LIFECYCLE}"
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
        const val NAVIGATION_COMMON_KTX = "androidx.navigation:navigation-common-ktx:${Version.NAVIGATION_KTX}"
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
        const val KODEIN_JVM = "org.kodein.di:kodein-di-generic-jvm:${Version.KODEIN}"
        const val KODEIN_CORE = "org.kodein.di:kodein-di-core-jvm:${Version.KODEIN}"
        const val KODEIN_ANDROID_X = "org.kodein.di:kodein-di-framework-android-x:${Version.KODEIN}"
    }

    object Internet {
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Version.OKHTTP3}"
        const val OKHTTP_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Version.OKHTTP3}"
        const val RETROFIT2 = "com.squareup.retrofit2:retrofit:${Version.RETROFIT2}"
        const val RETROFIT2_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Version.RETROFIT2}"
        const val COIL = "io.coil-kt:coil:${Version.COIL}"
    }

    object Firebase {
        const val PLAY_CORE = "com.google.android.play:core:${Version.PLAY_CORE}"
        const val FIREBASE_CORE = "com.google.firebase:firebase-core:${Version.FIREBASE_CORE}"
        const val FIREBASE_DB = "com.google.firebase:firebase-database:${Version.FIREBASE_DATABASE}"
        const val FIREBASE_AUTH = "com.google.firebase:firebase-auth:${Version.FIREBASE_AUTH}"
        const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging:${Version.FIREBASE_MESSAGING}"
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics:${Version.FIREBASE_ANALYTICS}"
        const val FIREBASE_CRASHLYTICS = "com.crashlytics.sdk.android:crashlytics:${Version.FIREBASE_CRASHLYTICS}"
        const val FIREBASE_ML = "com.google.firebase:firebase-ml-vision:${Version.FIREBASE_ML}"
    }

    object Ui {
        const val SHAPE_OF_VIEW = "com.github.florent37:shapeofview:${Version.SHAPE_OF_VIEW}"
        const val REALTIME_BLUR = "com.github.mmin18:realtimeblurview:${Version.REALTIME_BLUR}"
        const val LOTTIE = "com.airbnb.android:lottie:${Version.LOTTIE}"
    }
}

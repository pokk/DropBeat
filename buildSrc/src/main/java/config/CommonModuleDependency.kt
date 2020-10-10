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

package config

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

private const val FEATURE_PREFIX = ":feature:"

object CommonModuleDependency {
    const val APP = ":app"
    const val LIB_PURE_EXT = ":ext"
    const val LIB_KTX = ":ktx"
    const val LIB_WIDGET = ":widget"
    const val LIB_MEDIA_PLAYER = ":mediaplayer"
    const val LIB_DEVICE = ":device"
    const val LIB_CORE = ":core"
    const val LIB_TEST = ":test"

    // feature module name
    const val FEAT_SEARCH_MUSIC = "${FEATURE_PREFIX}search"
    const val FEAT_RANKING = "${FEATURE_PREFIX}ranking"
    const val FEAT_LOGIN = "${FEATURE_PREFIX}login"
    const val FEAT_LIBRARY = "${FEATURE_PREFIX}library"
    const val FEAT_EXPLORE = "${FEATURE_PREFIX}explore"
    const val FEAT_PLAYER = "${FEATURE_PREFIX}player"
    const val FEAT_SETTING = "${FEATURE_PREFIX}setting"

    fun getAllModules() = CommonModuleDependency::class.memberProperties
        .asSequence()
        .filter(KProperty1<CommonModuleDependency, *>::isConst)
        .map { it.getter.call().toString() }
        .toSet()

    fun getDynamicFeatureModules() = getAllModules()
        .asSequence()
        .filter { it.startsWith(FEATURE_PREFIX) }
        .toMutableSet()

    fun getFeatureModuleName() = getDynamicFeatureModules()
        .toMutableSet()
}

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

package taiwan.no.one.dropbeat.di

import android.content.Context
import android.content.SharedPreferences
import androidx.collection.lruCache
import org.kodein.di.DI
import org.kodein.di.bindConstant
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.core.data.repostory.cache.local.DiskCache
import taiwan.no.one.core.data.repostory.cache.local.MemoryCache
import taiwan.no.one.dropbeat.core.cache.LruMemoryCache
import taiwan.no.one.dropbeat.core.cache.MmkvCache
import taiwan.no.one.dropbeat.di.Constant.TAG_DEFAULT_MMKV
import taiwan.no.one.dropbeat.di.Constant.TAG_FEAT_REPO_SHARED_PREFS

object CacheModule {
    private const val TAG_CACHE_SIZE = "cache size"
    private const val TAG_REPO_SHARED_PREFS = "repo timestamp shared preferences"

    fun provide(context: Context) = DI.Module("Cache Module") {
        bindConstant(TAG_CACHE_SIZE) { 40 }

        bindSingleton<DiskCache> { MmkvCache(instance(TAG_DEFAULT_MMKV), instance()) }
        bindSingleton<MemoryCache> { LruMemoryCache(lruCache(instance(TAG_CACHE_SIZE)), instance()) }
        bindSingleton<SharedPreferences>(TAG_FEAT_REPO_SHARED_PREFS) {
            context.getSharedPreferences(TAG_REPO_SHARED_PREFS, Context.MODE_PRIVATE)
        }
    }
}

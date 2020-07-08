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

package taiwan.no.one.dropbeat.core.cache

import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import java.util.Date
import taiwan.no.one.core.data.repostory.cache.local.DiskCache

class MmkvCache(
    private val mmkv: MMKV
) : DiskCache {
    private val gson by lazy { GsonBuilder().create() }

    override fun <RT> get(key: String, classOf: Class<RT>): Pair<Long, RT>? {
        val set = mmkv.getStringSet(key, null)?.iterator() ?: return null
        val timestamp = set.next().toLong()
        return timestamp to gson.fromJson(set.next(), classOf)
    }

    override fun put(key: String, value: Any?) {
        if (value == null) return
        // [0]: timestamp
        // [1]: value
        val set = mutableSetOf(Date().time.toString(), gson.toJson(value))
        mmkv.putStringSet(key, set)
    }
}

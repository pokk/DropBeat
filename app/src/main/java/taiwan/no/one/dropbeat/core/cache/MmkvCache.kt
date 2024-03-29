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

package taiwan.no.one.dropbeat.core.cache

import com.squareup.moshi.Moshi
import com.tencent.mmkv.MMKV
import taiwan.no.one.core.data.repostory.cache.local.Caching.Constant.TIME_STAMP
import taiwan.no.one.core.data.repostory.cache.local.DiskCache
import taiwan.no.one.ext.extensions.now

class MmkvCache(
    private val mmkv: MMKV,
    private val parser: Moshi,
) : DiskCache {
    override fun <RT> get(key: String, classOf: Class<RT>): Pair<Long, RT>? {
        val stringValue = mmkv.getString(key, null) ?: return null
        val timestamp = mmkv.getString("$key+$TIME_STAMP", null)?.toLong() ?: return null
        return timestamp to requireNotNull(parser.adapter<RT>(classOf).fromJson(stringValue))
    }

    override fun <RT> put(key: String, value: RT?, classOf: Class<RT>) {
        if (value == null) return
        mmkv.putString("$key+$TIME_STAMP", now().toEpochMilliseconds().toString())
        mmkv.putString(key, parser.adapter<RT>(classOf).toJson(value))
    }
}

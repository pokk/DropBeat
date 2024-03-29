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

package taiwan.no.one.core.data.repostory.cache.local

import java.lang.reflect.ParameterizedType
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.datetime.Instant
import taiwan.no.one.core.exceptions.NotFoundException
import taiwan.no.one.ext.extensions.now

abstract class LocalCaching<RT>(
    private val memoryCache: MemoryCache,
    private val diskCache: DiskCache,
) {
    protected abstract val key: String
    private var timestamp = 0L
    private val expired = 1.toDuration(DurationUnit.DAYS)

    @Suppress("UNCHECKED_CAST")
    private val classOf by lazy {
        // OPTIMIZE(jieyi): 7/5/20 Need to find a good way to fix this reflection.
        (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<RT>
    }

    suspend fun value(): RT {
        val dataSource = loadFromCache()
        timestamp = dataSource?.first ?: 0L
        return if (dataSource == null || shouldFetch(dataSource.second)) {
            fetchFromDisk()?.second ?: throw NotFoundException()
        }
        else {
            dataSource.second
        }
    }

    protected open suspend fun saveCallResult(data: RT?) = memoryCache.put(key, data, classOf)

    protected open suspend fun loadFromCache() = memoryCache.get(key, classOf)

    protected open suspend fun createCall() = diskCache.get(key, classOf)

    /**
     * The function determines whether it should fetch the data from the disk cache or not.
     *
     * @param data RT?
     * @return Boolean if `true` will fetch the disk cache; otherwise, will just return the data from the memory cache.
     */
    protected open suspend fun shouldFetch(data: RT?) = Instant.fromEpochMilliseconds(timestamp) + expired >= now()

    private suspend fun fetchFromDisk() = createCall().apply { saveCallResult(this?.second) }
}

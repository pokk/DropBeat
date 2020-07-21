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

package taiwan.no.one.core.data.repostory.cache

import taiwan.no.one.core.exceptions.NotFoundException
import java.util.Date

abstract class LayerCaching<RT> {
    protected open var timestamp = 0L

    suspend fun value() = try {
        val dbSource = loadFromLocal()
        if (dbSource == null || shouldFetch(dbSource)) {
            timestamp = Date().time
            fetchFromRemote()
        }
        else {
            dbSource
        }
    }
    catch (notFoundException: NotFoundException) {
        // If can't find from the cache or the local persistence, will throw the [NotFoundException].
        timestamp = Date().time
        fetchFromRemote()
    }
    catch (e: Exception) {
        timestamp = Date().time
        fetchFromRemote()
    }

    private suspend fun fetchFromRemote() = createCall().apply { saveCallResult(this) }

    protected abstract suspend fun saveCallResult(data: RT)

    protected abstract suspend fun shouldFetch(data: RT): Boolean

    protected abstract suspend fun loadFromLocal(): RT?

    protected abstract suspend fun createCall(): RT
}

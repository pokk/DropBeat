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

package taiwan.no.one.core.domain

/**
 * The super class response from the data layer. There're three result of the response [Loading],
 * [Success], and [Error] for HTTP result.
 */
sealed class ResponseState<out T> constructor(val data: T? = null) {
    /**
     * A request is still processing from a remote/local service.
     */
    class Loading<T>(data: T? = null) : ResponseState<T>(data)

    /**
     * A request success getting a result from a remote/local service.
     */
    class Success<T>(data: T? = null) : ResponseState<T>(data)

    /**
     * A request sent then a remote/local service has happened an error.
     */
    class Error<T>(data: T? = null, val msg: String = "") : ResponseState<T>(data)
}

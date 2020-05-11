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

@file:Suppress("NOTHING_TO_INLINE")

package taiwan.no.one.ext

const val DEFAULT_INT = -1
const val DEFAULT_LONG = -1L
const val DEFAULT_DOUBLE = -1.0
const val DEFAULT_FLOAT = -1f
const val DEFAULT_STR = ""

inline fun Int.isDefault() = DEFAULT_INT == this
inline fun Long.isDefault() = DEFAULT_LONG == this
inline fun String.isDefault() = DEFAULT_STR == this
inline fun Double.isDefault() = DEFAULT_DOUBLE == this
inline fun Float.isDefault() = DEFAULT_FLOAT == this

//region All data type for `takeIfDefault`
inline fun <T> Int.takeIfDefault(block: (Int) -> T) = takeIf(Int::isDefault)?.let(block)

/**
 * Check [Int] is default value or not.
 *
 * @return if [DEFAULT_INT] return @receiver, otherwise, null
 */
inline fun Int.takeIfDefault() = takeIf(Int::isDefault)

inline fun <T> Long.takeIfDefault(block: (Long) -> T) = takeIf(Long::isDefault)?.let(block)

/**
 * Check [Long] is default value or not.
 *
 * @return if [DEFAULT_LONG] return @receiver, otherwise, null
 */
inline fun Long.takeIfDefault() = takeIf(Long::isDefault)

inline fun <T> String.takeIfDefault(block: (String) -> T) = takeIf(String::isDefault)?.let(block)

/**
 * Check [String] is default value or not.
 *
 * @return if [DEFAULT_STR] return @receiver, otherwise, null
 */
inline fun String.takeIfDefault() = takeIf(String::isDefault)

inline fun <T> Double.takeIfDefault(block: (Double) -> T) = takeIf(Double::isDefault)?.let(block)

/**
 * Check [Double] is default value or not.
 *
 * @return if [DEFAULT_DOUBLE] return @receiver, otherwise, null
 */
inline fun Double.takeIfDefault() = takeIf(Double::isDefault)

inline fun <T> Float.takeIfDefault(block: (Float) -> T) = takeIf(Float::isDefault)?.let(block)

/**
 * Check [Float] is default value or not.
 *
 * @return if [DEFAULT_FLOAT] return @receiver, otherwise, null
 */
inline fun Float.takeIfDefault() = takeIf(Float::isDefault)
//endregion

//region All data type for `takeUnlessDefault`
inline fun <T> Int.takeUnlessDefault(block: (Int) -> T) = takeUnless(Int::isDefault)?.let(block)

/**
 * Check [Int] is not default value or not.
 *
 * @return if [DEFAULT_INT] return null, otherwise, @receiver.
 * */
inline fun Int.takeUnlessDefault() = takeUnless(Int::isDefault)

inline fun <T> Long.takeUnlessDefault(block: (Long) -> T) = takeUnless(Long::isDefault)?.let(block)

/**
 * Check [Long] is default value or not.
 *
 * @return if [DEFAULT_LONG] return null, otherwise, @receiver
 */
inline fun Long.takeUnlessDefault() = takeUnless(Long::isDefault)

inline fun <T> String.takeUnlessDefault(block: (String) -> T) =
    takeUnless(String::isDefault)?.let(block)

/**
 * Check [String] is default value or not.
 *
 * @return if [DEFAULT_STR] return null, otherwise, @receiver
 */
inline fun String.takeUnlessDefault() = takeUnless(String::isDefault)

inline fun <T> Double.takeUnlessDefault(block: (Double) -> T) =
    takeUnless(Double::isDefault)?.let(block)

/**
 * Check [Double] is default value or not.
 *
 * @return if [DEFAULT_DOUBLE] return null, otherwise, @receiver
 */
inline fun Double.takeUnlessDefault() = takeUnless(Double::isDefault)

inline fun <T> Float.takeUnlessDefault(block: (Float) -> T) =
    takeUnless(Float::isDefault)?.let(block)

/**
 * Check [Float] is default value or not.
 *
 * @return if [DEFAULT_FLOAT] return null, otherwise, @receiver
 */
inline fun Float.takeUnlessDefault() = takeUnless(Float::isDefault)
//endregion

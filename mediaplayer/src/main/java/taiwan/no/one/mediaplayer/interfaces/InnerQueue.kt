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

package taiwan.no.one.mediaplayer.interfaces

import java.util.LinkedList

internal interface InnerQueue<T> {
    /**
     * This is the real queue we need.
     */
    val queue: LinkedList<T>

    /** Size of the playlist. */
    val size: Int

    /**
     * Set the current to the next item.
     *
     * @return
     */
    fun goNext(): T?

    /**
     * Set the current to the previous item
     *
     * @return
     */
    fun goPrevious(): T?

    /**
     * Reset the current object to the first of the list.
     *
     * @return
     */
    fun resetCurrentToBegin()

    /**
     * Append a list of objs into the playlist.
     *
     * @param objs List<T>
     * @return Boolean
     */
    fun enqueue(objs: List<T>): Boolean

    /**
     * Append an item into the playlist.
     *
     * @param obj
     * @return
     */
    fun enqueue(obj: T): Boolean

    /**
     * Clean the queue and add a new list into.
     *
     * @param objs
     * @return
     */
    fun reset(objs: List<T>): Boolean

    /**
     * Clean the queue and add a new object into.
     *
     * @param obj
     * @return
     */
    fun reset(obj: T): Boolean

    /**
     * Clear the while playlist.
     */
    fun clear()
}

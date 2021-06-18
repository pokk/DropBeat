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

package taiwan.no.one.mediaplayer.utils

import java.util.LinkedList
import taiwan.no.one.mediaplayer.MusicInfo
import taiwan.no.one.mediaplayer.interfaces.InnerQueue

/**
 * It is a playing queue when a media player plays a song, it could get a sequence song from
 * this data structure.
 *
 * @property current MusicInfo?
 * @property iterator MutableListIterator<MusicInfo>?
 * @property queue LinkedList<MusicInfo>
 * @property size Int
 */
@Deprecated("Using the ExoPlayer build-in playlist.")
internal class MusicQueue : InnerQueue<MusicInfo> {
    private var current: MusicInfo? = null
    private var iterator: MutableListIterator<MusicInfo>? = null
    private val queue by lazy { LinkedList<MusicInfo>() }

    override val size get() = queue.size

    override val currentItem get() = current

    override fun goNext() =
        iterator?.takeIf { it.hasNext() }?.next()?.also { current = it } // set back the current variable

    override fun goPrevious() =
        iterator?.takeIf { it.hasPrevious() }?.previous()?.also { current = it } // set back the current variable

    override fun resetCurrentToBegin() {
        iterator = queue.listIterator()
        goNext()
    }

    override fun enqueue(objs: List<MusicInfo>) = searchBackTheCurrentObject { queue.addAll(objs) }

    override fun enqueue(obj: MusicInfo) = searchBackTheCurrentObject { queue.add(obj) }

    override fun reset(objs: List<MusicInfo>): Boolean {
        clear()
        if (objs.isEmpty()) {
            return true
        }
        resetCurrentToBegin()
        return enqueue(objs)
    }

    override fun reset(obj: MusicInfo): Boolean {
        clear()
        resetCurrentToBegin()
        return enqueue(obj)
    }

    override fun clear() {
        iterator = null
        current = null
        queue.clear()
    }

    private fun searchBackTheCurrentObject(block: () -> Boolean): Boolean {
        val currentIndex = iterator?.previousIndex() ?: 0
        val res = block()
        resetCurrentToBegin()
        repeat(currentIndex + 1) { goNext() }
        return res
    }
}

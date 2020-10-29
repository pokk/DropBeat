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

package taiwan.no.one.widget.recyclerviews.layoutmanagers

import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import androidx.core.util.getOrDefault
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.State
import kotlin.math.max
import kotlin.math.min

class FirstBigSizeLayoutManager : LayoutManager() {
    companion object Constant {
        private const val SCALE_SIZE = 0.78f
    }

    private var decoratedChildWidth = 0
    private var decoratedChildHeight = 0
    private var horizontalScrollOffset = 0
    private var totalWidth = 0
    private var firstTime = true
    private val allItemFrames by lazy { SparseArray<Rect>() }
    private val hasAttachedItems by lazy { SparseBooleanArray() }

    override fun generateDefaultLayoutParams() =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

    override fun isAutoMeasureEnabled() = true

    override fun onLayoutChildren(recycler: Recycler, state: State) {
        // We have nothing to show for an empty data set but clear any existing views.
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }
        if (state.isPreLayout) {
            return
        }
        // ...on empty layout, update child size measurements.
        if (childCount == 0) {
            val scrap = recycler.getViewForPosition(0)
            addView(scrap)
            measureChildWithMargins(scrap, 0, 0)
            /*
             * We make some assumptions in this code based on every child
             * view being the same size (i.e. a uniform grid). This allows
             * us to compute the following values up front because they
             * won't change.
             */
            decoratedChildWidth = getDecoratedMeasuredWidth(scrap)
            decoratedChildHeight = getDecoratedMeasuredHeight(scrap)
            detachAndScrapView(scrap, recycler)
        }

        var offsetX = 0
        totalWidth = 0

        repeat(itemCount) {
            val view = recycler.getViewForPosition(it)
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)

            // Calculate the total width of all items.
            totalWidth += width
            val frame = allItemFrames.getOrDefault(it, Rect())
            frame.set(offsetX, 0, width + offsetX, height)
            // Keep the item frame position.
            allItemFrames.put(it, frame)
            hasAttachedItems.put(it, false)

            offsetX += width
        }
        // If the all items' width is smaller than the recyclerview component, get the longer one.
        totalWidth = max(totalWidth, getHorizontalSpace())

        recycleAndFillItems(recycler, state)
    }

    override fun onAdapterChanged(oldAdapter: Adapter<*>?, newAdapter: Adapter<*>?) {
        // Completely scrap the existing layout.
        removeAllViews()
    }

    override fun canScrollHorizontally() = true

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: State): Int {
        detachAndScrapAttachedViews(recycler)

        var interval = dx
        // If scroll to the top.
        if (horizontalScrollOffset + dx < 0) {
            interval = -horizontalScrollOffset
        }
        // If scroll to the bottom.
        else if (horizontalScrollOffset + dx > totalWidth - getHorizontalSpace()) {
            interval = totalWidth - getHorizontalSpace() - horizontalScrollOffset
        }
        // Keep the horizontal offset all the time.
        horizontalScrollOffset += interval
        offsetChildrenHorizontal(-interval)
        recycleAndFillItems(recycler, state)
        return interval
    }

    private fun recycleAndFillItems(recycler: Recycler, state: State) {
        // The displaying views' frame.
        val displayFrame = Rect(
            horizontalScrollOffset,
            0,
            getHorizontalSpace() + horizontalScrollOffset,
            getVerticalSpace()
        )
        // Recycle the views are out of the recyclerview to the buffer.
        val childFrame = Rect()
        repeat(childCount) {
            val child = getChildAt(it) ?: return@repeat
            childFrame.apply {
                left = getDecoratedLeft(child)
                top = getDecoratedTop(child)
                right = getDecoratedRight(child)
                bottom = getDecoratedBottom(child)
            }
            // If it's not in the displaying area, it will be recycled.
            if (!Rect.intersects(displayFrame, childFrame)) {
                removeAndRecycleView(child, recycler)
            }
        }
        // Redisplay the children view.
        var view: View? = null
        var scalableView: View? = null
        repeat(itemCount) {
            val frame = allItemFrames[it]
            if (Rect.intersects(displayFrame, frame)) {
                val scrap = recycler.getViewForPosition(it)
                measureChildWithMargins(scrap, 0, 0)
                // *** Do some customization here. ***
                val height = (frame.bottom - frame.top).toFloat()
                val width = (frame.right - frame.left).toFloat()
                val gap = (height / 2) * SCALE_SIZE
                scrap.pivotY = (height / 2) + gap + (gap / 4)
                scrap.pivotX = (width / 2) + gap + (gap / 4)
                if (!firstTime || it != 0) {
                    scrap.scaleX = SCALE_SIZE
                    scrap.scaleY = SCALE_SIZE
                    // The first time come this layout should keep the first item's size.
                    firstTime = false
                }
                if (-scrap.width < frame.left - horizontalScrollOffset && frame.left - horizontalScrollOffset < 0) {
                    view = scrap
                }
                if (0 <= frame.left - horizontalScrollOffset && frame.left - horizontalScrollOffset < scrap.width) {
                    scalableView = scrap
                }

                addView(scrap)
                // Layout the view.
                layoutDecorated(
                    scrap,
                    frame.left - horizontalScrollOffset,
                    frame.top,
                    frame.right - horizontalScrollOffset,
                    frame.bottom
                )
            }
            // The first view
            view?.apply {
                scaleX = 1f
                scaleY = 1f
            }
            // The second view
            scalableView?.apply {
                val threshold = min(width - left, width / 2).toFloat() / // to the half
                                (width / 2) * // reg
                                (1 - SCALE_SIZE) // reg to the scale
                scaleX = SCALE_SIZE + threshold
                scaleY = SCALE_SIZE + threshold
            }
        }
    }

    private fun getHorizontalSpace() = width - paddingStart - paddingEnd

    private fun getVerticalSpace() = height - paddingTop - paddingBottom
}

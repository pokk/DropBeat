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

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.State

class FirstBigSizeLayoutManager : LayoutManager() {
    private var decoratedChildWidth = 0
    private var decoratedChildHeight = 0
    private var interval = 0
    private var middle = 0
    private var offset = 0
    private var offsetList = mutableListOf<Int>()

    /**
     * Create a default `LayoutParams` object for a child of the RecyclerView.
     *
     *
     * LayoutManagers will often want to use a custom `LayoutParams` type
     * to store extra information specific to the layout. Client code should subclass
     * [RecyclerView.LayoutParams] for this purpose.
     *
     *
     * *Important:* if you use your own custom `LayoutParams` type
     * you must also override
     * [.checkLayoutParams],
     * [.generateLayoutParams] and
     * [.generateLayoutParams].
     *
     * @return A new LayoutParams for a child view
     */
    override fun generateDefaultLayoutParams() =
        RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)

    /**
     * Lay out all relevant child views from the given adapter.
     *
     * The LayoutManager is in charge of the behavior of item animations. By default,
     * RecyclerView has a non-null [ItemAnimator][.getItemAnimator], and simple
     * item animations are enabled. This means that add/remove operations on the
     * adapter will result in animations to add new or appearing items, removed or
     * disappearing items, and moved items. If a LayoutManager returns false from
     * [.supportsPredictiveItemAnimations], which is the default, and runs a
     * normal layout operation during [.onLayoutChildren], the
     * RecyclerView will have enough information to run those animations in a simple
     * way. For example, the default ItemAnimator, [DefaultItemAnimator], will
     * simply fade views in and out, whether they are actually added/removed or whether
     * they are moved on or off the screen due to other add/remove operations.
     *
     *
     * A LayoutManager wanting a better item animation experience, where items can be
     * animated onto and off of the screen according to where the items exist when they
     * are not on screen, then the LayoutManager should return true from
     * [.supportsPredictiveItemAnimations] and add additional logic to
     * [.onLayoutChildren]. Supporting predictive animations
     * means that [.onLayoutChildren] will be called twice;
     * once as a "pre" layout step to determine where items would have been prior to
     * a real layout, and again to do the "real" layout. In the pre-layout phase,
     * items will remember their pre-layout positions to allow them to be laid out
     * appropriately. Also, [removed][LayoutParams.isItemRemoved] items will
     * be returned from the scrap to help determine correct placement of other items.
     * These removed items should not be added to the child list, but should be used
     * to help calculate correct positioning of other views, including views that
     * were not previously onscreen (referred to as APPEARING views), but whose
     * pre-layout offscreen position can be determined given the extra
     * information about the pre-layout removed views.
     *
     *
     * The second layout pass is the real layout in which only non-removed views
     * will be used. The only additional requirement during this pass is, if
     * [.supportsPredictiveItemAnimations] returns true, to note which
     * views exist in the child list prior to layout and which are not there after
     * layout (referred to as DISAPPEARING views), and to position/layout those views
     * appropriately, without regard to the actual bounds of the RecyclerView. This allows
     * the animation system to know the location to which to animate these disappearing
     * views.
     *
     *
     * The default LayoutManager implementations for RecyclerView handle all of these
     * requirements for animations already. Clients of RecyclerView can either use one
     * of these layout managers directly or look at their implementations of
     * onLayoutChildren() to see how they account for the APPEARING and
     * DISAPPEARING views.
     *
     * @param recycler Recycler to use for fetching potentially cached views for a
     * position
     * @param state    Transient state of RecyclerView
     */
    override fun onLayoutChildren(recycler: Recycler, state: State) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }
        if (childCount == 0) {
            val scrap = recycler.getViewForPosition(0)
            addView(scrap)
            measureChildWithMargins(scrap, 0, 0)
            decoratedChildWidth = getDecoratedMeasuredWidth(scrap)
            decoratedChildHeight = getDecoratedMeasuredHeight(scrap)
            interval = 10
            middle = (getHorizontalSpace() - decoratedChildWidth) / 2
        }
        var property = 0
        for (i in 0 until itemCount) {
            offsetList.add(property)
            property += decoratedChildWidth + interval
        }
        detachAndScrapAttachedViews(recycler)
        layoutItems(recycler, state, 0)
    }

    /**
     * Called if the RecyclerView this LayoutManager is bound to has a different adapter set via
     * [RecyclerView.setAdapter] or
     * [RecyclerView.swapAdapter]. The LayoutManager may use this
     * opportunity to clear caches and configure state such that it can relayout appropriately
     * with the new data and potentially new view types.
     *
     *
     * The default implementation removes all currently attached views.
     *
     * @param oldAdapter The previous adapter instance. Will be null if there was previously no
     * adapter.
     * @param newAdapter The new adapter instance. Might be null if
     * [RecyclerView.setAdapter] is called with
     * `null`.
     */
    override fun onAdapterChanged(oldAdapter: Adapter<*>?, newAdapter: Adapter<*>?) {
        removeAllViews()
    }

    /**
     * Query if horizontal scrolling is currently supported. The default implementation
     * returns false.
     *
     * @return True if this LayoutManager can scroll the current contents horizontally
     */
    override fun canScrollHorizontally() = true

    /**
     * Scroll horizontally by dx pixels in screen coordinates and return the distance traveled.
     * The default implementation does nothing and returns 0.
     *
     * @param dx       distance to scroll by in pixels. X increases as scroll position
     * approaches the right.
     * @param recycler Recycler to use for fetching potentially cached views for a
     * position
     * @param state    Transient state of RecyclerView
     * @return The actual distance scrolled. The return value will be negative if dx was
     * negative and scrolling proceeeded in that direction.
     * `Math.abs(result)` may be less than dx if a boundary was reached.
     */
    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: State): Int {
        offset += dx
        if (offset < 0 || offset > offsetList[offsetList.size - 1]) return 0
        layoutItems(recycler, state, dx)
        return dx
    }

    private fun layoutItems(recycler: Recycler, state: State, dy: Int) {
        for (i in 0 until childCount) {
            val view = requireNotNull(getChildAt(i))
            val pos = getPosition(view)
            if (outOfRange(offsetList[pos] - offset.toFloat())) {
                removeAndRecycleView(view, recycler)
            }
        }
        detachAndScrapAttachedViews(recycler)
        val left = 100
        var selectedView: View? = null
        var maxScale = Float.MIN_VALUE
        for (i in 0 until itemCount) {
            val top = offsetList[i]
            if (outOfRange(top - offset.toFloat())) continue
            val scrap: View = recycler.getViewForPosition(i)
            measureChildWithMargins(scrap, 0, 0)
            if (dy >= 0) addView(scrap) else addView(scrap, 0)
            val deltaY = Math.abs(top - offset - middle)
            scrap.scaleX = 1f
            scrap.scaleY = 1f
            val scale = 1 + decoratedChildHeight / (deltaY + 1)
            if (scale > maxScale) {
                maxScale = scale.toFloat()
                selectedView = scrap
            }
            layoutDecorated(scrap,
                            left,
                            top - offset,
                            left + decoratedChildWidth,
                            top - offset + decoratedChildHeight)
        }
        if (selectedView != null) {
            maxScale = if (maxScale > 2) 2f else maxScale
            selectedView.scaleX = maxScale
            selectedView.scaleY = maxScale
        }
    }

    private fun outOfRange(targetOffSet: Float) =
        targetOffSet > getHorizontalSpace() + decoratedChildHeight || targetOffSet < -decoratedChildHeight

    private fun getHorizontalSpace() = width - paddingLeft - paddingRight

    private fun getVerticalSpace() = height - paddingTop - paddingBottom
}

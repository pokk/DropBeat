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

package taiwan.no.one.widget.popupwindow

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Size
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding

abstract class CustomPopupWindow<VB : ViewBinding>(private val context: Context) {
    protected open var _binding: VB? = null
    protected var anchorPosX = 0
    protected var anchorPosY = 0
    protected val binding get() = requireNotNull(_binding)
    private var anchor: View? = null
    private val popup by lazy { PopupWindow(context) }

    @UiThread
    abstract fun PopupWindow.buildPopup()

    @UiThread
    abstract fun setAnchorPosition(contentSize: Size, anchorRect: Rect)

    @UiThread
    fun anchorOn(anchor: View): CustomPopupWindow<VB> {
        this.anchor = anchor
        val screenPos = IntArray(2)

        // Get location of anchor view on screen
        anchor.getLocationOnScreen(screenPos)
        // Get rect for anchor view
        val anchorRect = Rect(
            screenPos[0],
            screenPos[1],
            screenPos[0] + anchor.width,
            screenPos[1] + anchor.height
        )
        // Call view measure to calculate how big your view should be.
        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        // Padding has be reduced.
        val contentViewHeight = binding.root.measuredHeight - binding.root.paddingTop - binding.root.paddingBottom
        val contentViewWidth = binding.root.measuredWidth - binding.root.paddingStart - binding.root.paddingEnd
        // In this case , I don't need much calculation for x and y position of tooltip
        // For cases if anchor is near screen border, you need to take care of
        // direction as well to show left, right, above or below of anchor view
        setAnchorPosition(Size(contentViewWidth, contentViewHeight), anchorRect)

        return this
    }

    @UiThread
    fun builder(buildBlock: VB.() -> Unit): CustomPopupWindow<VB> {
        popup.apply {
            height = LayoutParams.WRAP_CONTENT
            width = LayoutParams.WRAP_CONTENT
            contentView = binding.root
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            buildPopup()
        }
        binding.buildBlock()
        return this
    }

    @UiThread
    fun popup() {
        popup.showAtLocation(anchor, Gravity.NO_GRAVITY, anchorPosX, anchorPosY)
        anchor = null
    }

    @UiThread
    fun dismiss(preDismissProc: (VB.() -> Unit)? = null) {
        if (popup.isShowing) {
            preDismissProc?.invoke(binding)
            _binding = null
            popup.dismiss()
        }
    }
}

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

package taiwan.no.one.widget.components.popupwindow

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import taiwan.no.one.widget.R

class TooltipWindow(context: Context) {
    private val tipWindow: PopupWindow?
    private val contentView: View

    val isTooltipShown get() = tipWindow?.isShowing ?: false

    init {
        tipWindow = PopupWindow(context)
        contentView = View.inflate(context, R.layout.popup_tooltip, null)
    }

    fun showToolTip(anchor: View, tip: String) {
        if (tipWindow == null) return
        tipWindow.apply {
            height = LayoutParams.WRAP_CONTENT
            width = LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            isTouchable = true
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            contentView = this@TooltipWindow.contentView
            animationStyle = R.style.PopupWindow_Animation
        }

        contentView.findViewById<TextView>(R.id.tv_tip).text = tip

        val screenPos = IntArray(2)
        // Get location of anchor view on screen
        anchor.getLocationOnScreen(screenPos)

        // Get rect for anchor view
        val anchorRect =
            Rect(
                screenPos[0],
                screenPos[1],
                screenPos[0] + anchor.width,
                screenPos[1] + anchor.height
            )

        // Call view measure to calculate how big your view should be.
        contentView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val contentViewHeight = contentView.measuredHeight
        val contentViewWidth = contentView.measuredWidth
        // In this case , I don't need much calculation for x and y position of tooltip
        // For cases if anchor is near screen border, you need to take care of
        // direction as well to show left, right, above or below of anchor view
        val positionX = anchorRect.centerX() - contentViewWidth / 2
        val positionY = anchorRect.bottom + (contentViewHeight / 3)

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, positionX, positionY)
    }

    fun dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing) {
            tipWindow.dismiss()
        }
    }
}

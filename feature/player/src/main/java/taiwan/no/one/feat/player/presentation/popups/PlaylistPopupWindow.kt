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

package taiwan.no.one.feat.player.presentation.popups

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.R.layout
import taiwan.no.one.feat.player.databinding.PopupPlaylistBinding

internal class PlaylistPopupWindow(private val context: Context) {
    private val popup by lazy { PopupWindow(context) }
    private val binding by lazy {
        PopupPlaylistBinding.bind(View.inflate(context, layout.popup_playlist, null))
    }
    private var anchorPosX = 0
    private var anchorPosY = 0
    private var anchor: View? = null

    fun builder(buildBlock: (PopupPlaylistBinding) -> Unit): PlaylistPopupWindow {
        popup.apply {
            height = context.resources.getDimension(R.dimen.popup_playlist_height).toInt()
            width = context.resources.getDimension(R.dimen.popup_playlist_width).toInt()
            contentView = binding.root
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        buildBlock(binding)
        return this@PlaylistPopupWindow
    }

    fun anchorOn(anchor: View): PlaylistPopupWindow {
        this@PlaylistPopupWindow.anchor = anchor
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
        binding.root.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val contentViewHeight = binding.root.measuredHeight
        val contentViewWidth = binding.root.measuredWidth
        // In this case , I don't need much calculation for x and y position of tooltip
        // For cases if anchor is near screen border, you need to take care of
        // direction as well to show left, right, above or below of anchor view
        anchorPosX = anchorRect.centerX() - contentViewWidth / 2
        anchorPosY = anchorRect.bottom + contentViewHeight / 3

        return this@PlaylistPopupWindow
    }

    fun popup() {
        popup.showAtLocation(anchor, Gravity.NO_GRAVITY, anchorPosX, anchorPosY)
        anchor = null
    }

    fun dismiss() {
        popup.dismiss()
    }
}

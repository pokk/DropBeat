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

import android.content.Context
import android.graphics.Rect
import android.util.Size
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.databinding.PopupSettingBinding
import taiwan.no.one.widget.popupwindow.CustomPopupWindow

internal class SettingPopupWindow(private val context: Context) : CustomPopupWindow<PopupSettingBinding>(context) {
    override var _binding: PopupSettingBinding? =
        PopupSettingBinding.bind(View.inflate(context, R.layout.popup_setting, null))

    override fun PopupWindow.buildPopup() {
        height = LayoutParams.WRAP_CONTENT
        width = LayoutParams.WRAP_CONTENT
        // TODO(jieyi): 5/8/21 Add the popup menu animation.
    }

    override fun setAnchorPosition(contentSize: Size, anchorRect: Rect) {
        anchorPosX = anchorRect.left - (contentSize.width - anchorRect.width())
        anchorPosY = anchorRect.bottom
    }
}

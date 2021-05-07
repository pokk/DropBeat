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
import android.widget.PopupWindow
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.R.layout
import taiwan.no.one.feat.player.databinding.PopupPlaylistBinding
import taiwan.no.one.widget.popupwindow.CustomPopupWindow

internal class PlaylistPopupWindow(private val context: Context) : CustomPopupWindow<PopupPlaylistBinding>(context) {
    override var _binding: PopupPlaylistBinding? =
        PopupPlaylistBinding.bind(View.inflate(context, layout.popup_playlist, null))

    override fun PopupWindow.buildPopup() {
        height = context.resources.getDimension(R.dimen.popup_playlist_height).toInt()
        width = context.resources.getDimension(R.dimen.popup_playlist_width).toInt()
    }

    override fun setAnchorPosition(contentSize: Size, anchorRect: Rect) {
        // FIXME(jieyi): 5/7/21 The anchor position is incorrect from the design.
        anchorPosX = anchorRect.centerX() - contentSize.width / 2
        anchorPosY = anchorRect.bottom + contentSize.height / 3
    }
}

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

package taiwan.no.one.widget.components.viewgroups

import FontTextView
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import taiwan.no.one.widget.R

class SeparateLineWithTitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    companion object {
        private const val TITLE_TEXT = ""
        private const val TITLE_SIZE = 14f
        private const val TITLE_COLOR = Color.WHITE
        private const val SEPARATER_COLOR = Color.WHITE
        private const val SEPARATER_HEIGHT = 1f
    }

    //region Components
    private val tvTitle: TextView
    private val vSeparate: View
    //endregion
    private val scale by lazy { resources.displayMetrics.density }

    //region Parameters
    var title = TITLE_TEXT
    var titleSize = TITLE_SIZE
    var titleColor = TITLE_COLOR
    var textFont = ""
    var between = 8 * scale * .5f
    var lineColor = SEPARATER_COLOR
    var lineHeight = SEPARATER_HEIGHT
    //endregion

    init {
        context.obtainStyledAttributes(attrs, R.styleable.SeparateLineWithTitle, defStyleAttr, 0)
            .apply {
                title = getString(R.styleable.SeparateLineWithTitle_sltTitle) ?: title
                titleSize = getFloat(R.styleable.SeparateLineWithTitle_sltTitleSize, titleSize)
                titleColor = getColor(R.styleable.SeparateLineWithTitle_sltTitleColor, titleColor)
                textFont = getString(R.styleable.SeparateLineWithTitle_sltTextFont) ?: textFont
                between = getDimension(R.styleable.SeparateLineWithTitle_sltBetween, between)
                lineColor = getColor(R.styleable.SeparateLineWithTitle_sltLineColor, lineColor)
                lineHeight =
                    getDimension(R.styleable.SeparateLineWithTitle_sltLineHeight, lineHeight)
            }.recycle()
        setBackgroundColor(resources.getColor(android.R.color.transparent))
        addView(FontTextView(context, attrs, defStyleAttr).apply {
            tvTitle = this
            text = title
            setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize)
            setTextColor(titleColor)
            if (textFont.isNotBlank()) setFont(textFont)
            setBackgroundResource(android.R.color.transparent)
        })
        addView(View(context, attrs, defStyleAttr).apply {
            vSeparate = this
            setBackgroundColor(lineColor)
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(tvTitle, widthMeasureSpec, heightMeasureSpec)
        val parentHeight = tvTitle.measuredHeight + between + vSeparate.measuredHeight
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sepWidthSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY)
        val sepHeightSpec = MeasureSpec.makeMeasureSpec(lineHeight.toInt(), MeasureSpec.EXACTLY)
        measureChild(vSeparate, sepWidthSpec, sepHeightSpec)

        setMeasuredDimension(parentWidth, parentHeight.toInt())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        data class Rectangle(val l: Int, val t: Int, val r: Int, val b: Int)

        repeat(childCount) { index ->
            val view = getChildAt(index)
            val (childH, _) = view.measuredHeight to view.measuredWidth

            val (l, t, r, b) = when (index) {
                // Title
                0 -> Rectangle(0, 0, r, childH)
                // Separator
                1 -> {
                    val preY = (tvTitle.y + between + tvTitle.height).toInt()
                    Rectangle(0, preY, r, (preY + lineHeight).toInt())
                }
                else -> Rectangle(0, 0, 0, 0)
            }

            view.layout(l, t, r, b)
        }
    }
}

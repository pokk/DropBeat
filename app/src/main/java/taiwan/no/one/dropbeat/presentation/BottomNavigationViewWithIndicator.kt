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

package taiwan.no.one.dropbeat.presentation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.google.android.material.bottomnavigation.BottomNavigationView
import taiwan.no.one.dropbeat.R
import java.lang.Math.abs
import taiwan.no.one.widget.R as WidgetR

class BottomNavigationViewWithIndicator :
    BottomNavigationView, BottomNavigationView.OnNavigationItemSelectedListener {
    companion object Constant {
        private const val DEFAULT_SCALE = 1f
        private const val MAX_SCALE = 15f
        private const val BASE_DURATION = 300L
        private const val VARIABLE_DURATION = 300L
    }

    private var externalSelectedListener: OnNavigationItemSelectedListener? = null
    private var animator: ValueAnimator? = null

    private val indicator = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.coral)
    }

    private val bottomOffset = resources.getDimension(WidgetR.dimen.md_zero_unit)
    private val defaultSize = resources.getDimension(WidgetR.dimen.md_one_half_unit)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        super.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (externalSelectedListener?.onNavigationItemSelected(item) != false) {
            onItemSelected(item.itemId)
            return true
        }
        return false
    }

    override fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        externalSelectedListener = listener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doOnPreDraw {
            // Move the indicator in place when the view is laid out
            onItemSelected(selectedItemId, false)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up the animator if the view is going away
        cancelAnimator(setEndValues = true)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isLaidOut) {
            val cornerRadius = indicator.height() / 2f
            canvas.drawRoundRect(indicator, cornerRadius, cornerRadius, paint)
            indicator
        }
    }

    private fun onItemSelected(itemId: Int, animate: Boolean = true) {
        if (!isLaidOut) return

        // Interrupt any current animation, but don't set the end values,
        // if it's in the middle of a movement we want it to start from
        // the current position, to make the transition smoother.
        cancelAnimator(setEndValues = false)

        val itemView = findViewById<View>(itemId) ?: return
        val fromCenterX = indicator.centerX()
        val fromScale = indicator.width() / defaultSize

        animator = ValueAnimator.ofFloat(fromScale, MAX_SCALE, DEFAULT_SCALE).apply {
            addUpdateListener {
                val progress = it.animatedFraction
                val distanceTravelled = linearInterpolation(progress, fromCenterX, itemView.centerX)

                val scale = it.animatedValue as Float
                val indicatorWidth = defaultSize * scale

                val left = distanceTravelled - indicatorWidth / 2f
                val top = height - bottomOffset - defaultSize
                val right = distanceTravelled + indicatorWidth / 2f
                val bottom = height - bottomOffset

                indicator.set(left, top, right, bottom)
                invalidate()
            }

            interpolator = LinearOutSlowInInterpolator()

            val distanceToMove = abs(fromCenterX - itemView.centerX)
            duration = if (animate) calculateDuration(distanceToMove) else 0L

            start()
        }
    }

    /**
     * Linear interpolation between 'a' and 'b' based on the progress 't'
     */
    private fun linearInterpolation(t: Float, a: Float, b: Float) = (1 - t) * a + t * b

    /**
     * Calculates a duration for the translation based on a fixed duration + a duration
     * based on the distance the indicator is being moved.
     */
    private fun calculateDuration(distance: Float) =
        (BASE_DURATION + VARIABLE_DURATION * (distance / width).coerceIn(0f, 1f)).toLong()

    /**
     * Convenience property for getting the center X value of a View
     */
    private val View.centerX get() = left + width / 2f

    private fun cancelAnimator(setEndValues: Boolean) = animator?.let {
        if (setEndValues) {
            it.end()
        }
        else {
            it.cancel()
        }
        it.removeAllUpdateListeners()
        animator = null
    }
}

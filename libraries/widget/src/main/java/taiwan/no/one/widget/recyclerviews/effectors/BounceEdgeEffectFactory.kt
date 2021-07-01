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

package taiwan.no.one.widget.recyclerviews.effectors

import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView

/**
 * Replace edge effect by a bounce.
 * Reference [https://medium.com/androidxx/say-hi-to-the-bounce-effect-in-recyclerview-without-headaches-589c157d0a92]
 */
class BounceEdgeEffectFactory(
    private val isVertical: Boolean = true
) : RecyclerView.EdgeEffectFactory() {
    companion object Constant {
        /** The magnitude of translation distance while the list is over-scrolled. */
        private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f

        /** The magnitude of translation distance when the list reaches the edge on fling. */
        private const val FLING_TRANSLATION_MAGNITUDE = 0.5f
    }

    override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int) =
        object : EdgeEffect(recyclerView.context) {
            // A reference to the [SpringAnimation] for this RecyclerView used to
            // bring the item back after the over-scroll effect.
            var translationAnim: SpringAnimation? = null

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            override fun onRelease() {
                super.onRelease()
                // The finger is lifted. Start the animation to bring translation back to the resting state.
                if (isVertical) {
                    if (recyclerView.translationY != 0f) {
                        translationAnim = createAnim().also { it.start() }
                    }
                }
                else {
                    if (recyclerView.translationX != 0f) {
                        translationAnim = createAnim().also { it.start() }
                    }
                }
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)
                // The list has reached the edge on fling.
                val sign = if (isVertical) {
                    if (direction == DIRECTION_BOTTOM) -1 else 1
                }
                else {
                    if (direction == DIRECTION_RIGHT) -1 else 1
                }
                val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                translationAnim?.cancel()
                translationAnim = createAnim().setStartVelocity(translationVelocity)?.also { it.start() }
            }

            // don't paint the usual edge effect
            override fun draw(canvas: Canvas?) = false

            // Without this, will skip future calls to onAbsorb()
            override fun isFinished() = translationAnim?.isRunning?.not() ?: true

            private fun handlePull(deltaDistance: Float) {
                // This is called on every touch event while the list is scrolled with a finger.
                // Translate the recyclerView with the distance
                if (isVertical) {
                    val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                    val translationYDelta = sign * recyclerView.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                    recyclerView.translationY += translationYDelta
                }
                else {
                    val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                    val translationXDelta =
                        sign * recyclerView.height * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                    recyclerView.translationX += translationXDelta
                }
                translationAnim?.cancel()
            }

            private fun createAnim(): SpringAnimation {
                val direct = if (isVertical) SpringAnimation.TRANSLATION_Y else SpringAnimation.TRANSLATION_X
                SpringAnimation(recyclerView, direct).spring = SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            }
        }
}

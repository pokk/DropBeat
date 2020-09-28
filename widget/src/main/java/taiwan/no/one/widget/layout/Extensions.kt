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

package taiwan.no.one.widget.layout

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout

/**
 * Wait for the transition to complete so that the given [transitionId] is fully displayed.
 * From [https://medium.com/androiddevelopers/suspending-over-views-example-260ce3dc9100].
 *
 * @param transitionId The transition set to await the completion of
 * @param timeout Timeout for the transition to take place. Defaults to 5 seconds.
 */
suspend fun MotionLayout.awaitTransitionComplete(transitionId: Int, timeout: Long = 5000L) {
    // If we're already at the specified state, return now
    if (currentState == transitionId) return
    var listener: MotionLayout.TransitionListener? = null
    try {
        withTimeout(timeout) {
            suspendCancellableCoroutine<Unit> { continuation ->
                val l = object : TransitionAdapter() {
                    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                        if (currentId == transitionId) {
                            removeTransitionListener(this)
                            continuation.resume(Unit, Throwable::printStackTrace)
                        }
                    }
                }
                // If the coroutine is cancelled, remove the listener
                continuation.invokeOnCancellation {
                    removeTransitionListener(l)
                }
                // And finally add the listener
                addTransitionListener(l)
                listener = l
            }
        }
    }
    catch (tex: TimeoutCancellationException) {
        // Transition didn't happen in time. Remove our listener and throw a cancellation
        // exception to let the coroutine know 
        listener?.let(::removeTransitionListener)
        throw CancellationException("Transition to state with id: $transitionId did not complete in timeout.", tex)
    }
}

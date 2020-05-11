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

package taiwan.no.one.ktx.view

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment

fun View.obtainPositionOnScreen(): Pair<Float, Float> {
    val position = IntArray(2)
    getLocationOnScreen(position)
    return position[0].toFloat() to position[1].toFloat()
}

fun View.obtainPositionInWindow(): Pair<Float, Float> {
    val position = IntArray(2)
    getLocationInWindow(position)
    return position[0].toFloat() to position[1].toFloat()
}

//region FindViewById
fun <vt : View> Activity.find(@IdRes idRes: Int) = requireNotNull(findViewById<vt>(idRes)) {
    "ID does not reference a View inside this Activity"
}

fun <vt : View> Fragment.find(@IdRes idRes: Int) =
    requireNotNull(requireView().findViewById<vt>(idRes)) {
        "ID does not reference a View inside this Fragment"
    }

fun <vt : View> View.find(@IdRes idRes: Int) = requireNotNull(findViewById<vt>(idRes)) {
    "ID does not reference a View inside this View"
}

fun <vt : View> AppCompatDialog.find(@IdRes idRes: Int) =
    requireNotNull(findViewById<vt>(idRes)) {
        "ID does not reference a View inside this View"
    }

fun <vt : View> Activity.findOptional(@IdRes idRes: Int) = findViewById<vt>(idRes)
fun <vt : View> Fragment.findOptional(@IdRes idRes: Int) = requireView().findViewById<vt>(idRes)
fun <vt : View> View.findOptional(@IdRes idRes: Int) = findViewById<vt>(idRes)
fun <vt : View> AppCompatDialog.findOptional(@IdRes idRes: Int) = findViewById<vt>(idRes)
//endregion

//region Lazy Binding
fun <vt : View> Activity.bindView(@IdRes idRes: Int): Lazy<vt> {
    return lazy(LazyThreadSafetyMode.NONE) { find<vt>(idRes) }
}

fun <vt : View> Fragment.bindView(@IdRes idRes: Int): Lazy<vt> {
    return lazy(LazyThreadSafetyMode.NONE) { find<vt>(idRes) }
}

fun <vt : View> View.bindView(@IdRes idRes: Int): Lazy<vt> {
    return lazy(LazyThreadSafetyMode.NONE) { find<vt>(idRes) }
}
//endregion

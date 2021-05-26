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

package taiwan.no.one.test.matcher

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun withMarginStart(size: Int) = object : TypeSafeMatcher<View>() {
    public override fun matchesSafely(view: View) = (view.layoutParams as MarginLayoutParams).marginStart == size

    override fun describeTo(description: Description) {
        description.appendText("margin start should have the same $size.")
    }
}

fun withMarginEnd(size: Int) = object : TypeSafeMatcher<View>() {
    public override fun matchesSafely(view: View) = (view.layoutParams as MarginLayoutParams).marginEnd == size

    override fun describeTo(description: Description) {
        description.appendText("margin end should have the same $size.")
    }
}

fun withTextViewStartDrawableSize(height: Int, width: Int) = object : TypeSafeMatcher<View>() {
    public override fun matchesSafely(view: View): Boolean {
        if (view !is TextView) return false
        val bounds = view.compoundDrawables[0].bounds
        val boundHeight = bounds.bottom - bounds.top
        val boundWidth = bounds.right - bounds.left
        return boundHeight == height && boundWidth == width
    }

    override fun describeTo(description: Description) {
        description.appendText("with textview start drawable bound size should be the same as $height and $width")
    }
}

fun hasTextInputLayoutHintText(expectedHintText: String) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("Edit Layout hint should have the same $expectedHintText.")
    }

    override fun matchesSafely(view: View?): Boolean {
        val hint = (view as? TextInputLayout)?.hint ?: return false
        return expectedHintText == hint.toString()
    }
}

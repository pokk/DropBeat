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

package taiwan.no.one.test.action

import android.view.View
import android.widget.TextView
import androidx.test.espresso.PerformException.Builder
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import java.util.concurrent.TimeoutException

fun waitId(viewId: Int, millis: Long = 0L) = object : ViewAction {
    override fun getConstraints() = isRoot()

    override fun getDescription() = "wait for a specific view with id <$viewId> during $millis millis."

    override fun perform(uiController: UiController, view: View?) {
        uiController.loopMainThreadUntilIdle()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + millis
        val viewMatcher = withId(viewId)
        do {
            for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                if (viewMatcher.matches(child)) return
            }
            uiController.loopMainThreadForAtLeast(50)
        } while (System.currentTimeMillis() < endTime)
        throw Builder()
            .withActionDescription(this.description)
            .withViewDescription(HumanReadables.describe(view))
            .withCause(TimeoutException())
            .build()
    }
}

fun waitFor(delay: Long) = object : ViewAction {
    override fun getConstraints() = isRoot()

    override fun getDescription() = "wait for ${delay}milliseconds"

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadForAtLeast(delay)
    }
}

fun getText(matcher: ViewInteraction): String {
    var text = String()
    matcher.perform(object : ViewAction {
        override fun getConstraints() = isAssignableFrom(TextView::class.java)

        override fun getDescription() = "Text of the view"

        override fun perform(uiController: UiController, view: View) {
            val tv = view as TextView
            text = tv.text.toString()
        }
    })
    return text
}

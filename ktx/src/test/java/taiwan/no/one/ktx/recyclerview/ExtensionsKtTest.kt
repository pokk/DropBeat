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

package taiwan.no.one.ktx.recyclerview

import androidx.recyclerview.widget.RecyclerView
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import taiwan.no.one.test.CoroutineTestRule

class ExtensionsKtTest : TestCase() {
    @Rule
    val rule = CoroutineTestRule()

    @MockK
    private lateinit var recyclerView: RecyclerView

    @MockK
    private lateinit var decorator: RecyclerView.ItemDecoration

    @Before
    public override fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test success of containing`() {
        // GIVEN
        every { recyclerView.itemDecorationCount } returns 2
        every { recyclerView.getItemDecorationAt(0) } returns mockk()
        every { recyclerView.getItemDecorationAt(1) } returns decorator
        // WHEN
        // THEN
        assertTrue(decorator in recyclerView)
    }

    @Test
    fun `test contains 0 items`() {
        // GIVEN
        every { recyclerView.itemDecorationCount } returns 0
        // WHEN
        // THEN
        assertFalse(decorator in recyclerView)
    }

    @Test
    fun `test not in the container`() {
        // GIVEN
        val numberOfItems = 3
        every { recyclerView.itemDecorationCount } returns numberOfItems
        repeat(numberOfItems) { every { recyclerView.getItemDecorationAt(it) } returns mockk() }
        // WHEN
        // THEN
        assertTrue(decorator !in recyclerView)
    }
}

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

package taiwan.no.one.core.data.remote.interceptor

import android.content.Context
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Interceptor
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class MockRequestInterceptorTest : TestCase() {
    private val chain = mockk<Interceptor.Chain>(relaxed = true)
    private val context = mockk<Context>()
    private val interceptor = MockRequestInterceptor(context)

    @Before
    public override fun setUp() = clearAllMocks()

    @Test
    fun `test has mock header with false value`() = runBlockingTest {
        // GIVEN
        every { chain.request().header(any()) } returns "false"
        // WHEN
        val response = interceptor.intercept(chain)
        // THEN
        assertThat(response).isNotNull()
    }

    @Test
    fun `test no mock header`() = runBlockingTest {
        // GIVEN
        // WHEN
        val response = interceptor.intercept(chain)
        // THEN
        assertThat(response).isNotNull()
    }

    @Test
    fun `test has mock header with true value`() = runBlockingTest {
        // GIVEN
        val inputStream = requireNotNull(
            javaClass.classLoader?.getResourceAsStream("json/test.json")
        )
        every { chain.request().header(any()) } returns "true"
        every { chain.request().url.pathSegments } returns listOf("a", "b", "c")
        every { context.applicationContext.assets.open(any()) } returns inputStream
        // WHEN
        val response = interceptor.intercept(chain)
        // THEN
        assertThat(response).isNotNull().all {
            prop(Response::code).isEqualTo(200)
        }
    }
}

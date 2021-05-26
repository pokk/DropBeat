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

package taiwan.no.one.ktx.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import assertk.assertThat
import assertk.assertions.isInstanceOf
import junit.framework.TestCase
import org.junit.Test

class ExtensionsKtTest : TestCase() {
    @Test
    fun `test success casting of livedata`() {
        // GIVEN
        val mutableLiveData = MutableLiveData<Int>()
        val wrappedMutableLiveData = MutableLiveData<Result<Any>>()
        // WHEN
        val liveData = mutableLiveData.toLiveData()
        val wrappedLiveData = wrappedMutableLiveData.toLiveData()
        // THEN
        assertThat(liveData).isInstanceOf(LiveData::class.java)
        assertThat(wrappedLiveData).isInstanceOf(LiveData::class.java)
    }

    @Test
    fun `test success casting of safe livedata`() {
        // GIVEN
        val mutableLiveData = SafeMutableLiveData(1)
        // WHEN
        val liveData = mutableLiveData.toLiveData()
        // THEN
        assertThat(liveData).isInstanceOf(LiveData::class.java)
        assertThat(liveData).isInstanceOf(SafeLiveData::class.java)
    }
}

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

package taiwan.no.one.ktx.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.devrapid.kotlinshaver.accessible
import taiwan.no.one.ktx.BuildConfig

abstract class SilentLiveData<T> : LiveData<T>(), SilentHook<T> {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        try {
            beSilent(observer)
        }
        catch (e: Exception) {
            if (BuildConfig.DEBUG)
                e.printStackTrace()
        }
    }

    override fun beSilent(observer: Observer<in T>) {
        // Get wrapper's version.
        val classLiveData = LiveData::class.java
        val fieldObservers = classLiveData.getDeclaredField("mObservers").accessible()
        val objectObservers = fieldObservers.get(this)
        val classObservers = objectObservers.javaClass
        val methodGet = classObservers.getDeclaredMethod("get", Any::class.java).accessible()
        val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
        lateinit var objectWrapper: Any
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = requireNotNull(objectWrapperEntry.value)
        }
        val classObserverWrapper = objectWrapper.javaClass.superclass
        val fieldLastVersion =
            requireNotNull(classObserverWrapper?.getDeclaredField("mLastVersion")).accessible()
        // Get LiveData's version.
        val fieldVersion = classLiveData.getDeclaredField("mVersion").accessible()
        val objectVersion = fieldVersion.get(this)
        // Set wrapper's version.
        fieldLastVersion.set(objectWrapper, objectVersion)
    }
}

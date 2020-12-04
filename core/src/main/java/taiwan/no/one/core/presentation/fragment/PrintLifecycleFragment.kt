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

package taiwan.no.one.core.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class PrintLifecycleFragment : Fragment() {
    companion object Constant {
        private const val TAG = "Fragment Lifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "\uD83E\uDDEC onCreate: $this")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "\uD83E\uDDEC onCreateView: $this")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "\uD83E\uDDEC onViewCreated: $this")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "\uD83E\uDDEC onStart: $this")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "\uD83E\uDDEC onResume: $this")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "\uD83E\uDDEC onPause: $this")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "\uD83E\uDDEC onStop: $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "\uD83E\uDDEC onDestroyView: $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "\uD83E\uDDEC onDestroy: $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "\uD83E\uDDEC onDetach: $this")
    }
}

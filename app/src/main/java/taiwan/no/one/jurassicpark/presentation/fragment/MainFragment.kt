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

package taiwan.no.one.jurassicpark.presentation.fragment

import android.net.Uri
import androidx.navigation.fragment.findNavController
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.jurassicpark.databinding.ActivitySecondBinding
import taiwan.no.one.jurassicpark.presentation.activity.MainActivity

class MainFragment : BaseFragment<MainActivity, ActivitySecondBinding>() {
    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        binding.tvMsg.text = "321862189hfeuwih89d2h8923"
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        binding.btnClick.setOnClickListener {
            //            findNavController().navigate(MainFragmentDirections.actionFragmentSecondToActivitySecond(13))
            findNavController().navigate(Uri.parse("https://taiwan.no.one.dummy/dummy"))
        }
    }
}

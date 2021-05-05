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

package taiwan.no.one.widget.toast

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import androidx.annotation.DrawableRes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import taiwan.no.one.widget.R
import taiwan.no.one.widget.databinding.PartTopToastBinding

@ExperimentalCoroutinesApi
@FlowPreview
@SuppressLint("DiscouragedPrivateApi")
fun Context.showTopToast(text: String, @DrawableRes icon: Int = R.drawable.ic_close) = Toast(this).apply toast@{
    val binding = PartTopToastBinding
        .bind(LayoutInflater.from(this@showTopToast).inflate(R.layout.part_top_toast, null))
        .apply {
            ftvToastMsg.text = text
            ftvToastMsg.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)

            try {
                // Big trick for making the toast focusable and clickable.
                val clazz = Class.forName("android.widget.Toast")
                val method = clazz.getDeclaredMethod("getWindowParams")
                val param = method.invoke(this@toast) as LayoutParams
                param.flags = LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_KEEP_SCREEN_ON
            }
            catch (e: Exception) {
                println(e.printStackTrace())
            }

            callbackFlow {
                btnClose.setOnClickListener { offer(Unit) }
                awaitClose { btnClose.setOnClickListener(null) }
            }.debounce(150)
                .onEach {
                    btnClose.setOnClickListener(null)
                    this@toast.cancel()
                }
                .launchIn(GlobalScope)
        }
    duration = Toast.LENGTH_LONG
    setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
    view = binding.root
}.show()

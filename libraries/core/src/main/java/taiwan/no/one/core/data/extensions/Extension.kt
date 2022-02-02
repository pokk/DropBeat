/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
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

package taiwan.no.one.core.data.extensions

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.datetime.Instant
import okio.buffer
import okio.source
import taiwan.no.one.core.json.adapter.DateJsonAdapter

/**
 * Parse the json file to an object by [Moshi].
 * OPTIMIZE(jieyi): 7/6/21 we might use native way to parse instead of a third-party lib.
 *
 * @receiver Context
 * @param filePath String using full path on assets folder, eg. "json/xxx.json"
 * @return T?
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> Context.parseObjectFromJson(filePath: String): T? {
    var dataObj: T? = null
    try {
        val moshi = Moshi.Builder()
            .add(Instant::class.java, DateJsonAdapter())
            .build()
        val adapter = moshi.adapter<T>()
        applicationContext.assets.open(filePath).use { inputStream ->
            dataObj = adapter.fromJson(inputStream.source().buffer())
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        Log.e("data layer", "Error for parsing json account", e)
    }

    return dataObj
}

fun Context.readFileFromAssets(filePath: String) =
    applicationContext.assets.open(filePath).bufferedReader().use {
        it.readText()
    }

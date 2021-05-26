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

package taiwan.no.one.core.data.extensions

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

/**
 * Parse the json file to an object by [Gson].
 *
 * @receiver Context
 * @param filePath String using full path on assets folder, eg. "json/xxx.json"
 * @return T?
 */
inline fun <reified T> Context.parseObjectFromJson(filePath: String): T? {
    var dataObj: T? = null

    try {
        val gson = Gson().newBuilder().create()
        applicationContext.assets.open(filePath).use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val type = object : TypeToken<T>() {}.type
                dataObj = gson.fromJson<T>(jsonReader, type)
            }
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

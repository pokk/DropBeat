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

package taiwan.no.one.ext.utils

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object StorageUtils {
    fun createDir(dirPath: String) = File(dirPath).apply {
        if (!exists()) {
            mkdirs()
        }
    }

    fun saveFileToDisk(byteArray: ByteArray, file: File) = try {
        byteArray.inputStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
                output.flush()
            }
        }
        true
    }
    catch (ioe: IOException) {
        ioe.printStackTrace()
        false
    }

    fun readFileFromDisk(file: File) = file.inputStream().use { input ->
        try {
            input.bufferedReader().use { buffer ->
                buffer.readText()
            }
        }
        catch (ioe: IOException) {
            ioe.printStackTrace()
            null
        }
    }

    fun removeFileFromDisk(file: File) = file.delete()
}

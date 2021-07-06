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

package taiwan.no.one.dropbeat.core.helpers

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import okhttp3.ResponseBody

object DownloadHelper {
    fun downloadTrack(context: Context, uri: Uri, filename: String, songEntityStream: String) {
        val downloadManager = context.applicationContext.getSystemService(DownloadManager::class.java)
        val request = Request(uri).apply {
            setDestinationInExternalFilesDir(context.applicationContext, Environment.DIRECTORY_MUSIC, "$filename.mp3")
            setAllowedNetworkTypes(Request.NETWORK_MOBILE or Request.NETWORK_WIFI)
            setTitle(filename)
            setDescription(songEntityStream)
        }
        downloadManager.enqueue(request)
    }

    private fun writeResponseBodyToDisk(context: Context, body: ResponseBody): Boolean {
        return try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile =
                File(
                    context.getExternalFilesDir(null).toString() + File.separator.toString() + "Future Studio Icon.png"
                )
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize: Long = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("TAGGGGG", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            }
            catch (e: IOException) {
                false
            }
            finally {
                inputStream?.close()
                outputStream?.close()
            }
        }
        catch (e: IOException) {
            false
        }
    }
}

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

package taiwan.no.one.feat.library.data.local.services.storage.v1

import java.io.File
import java.io.IOException
import taiwan.no.one.ext.utils.StorageUtils
import taiwan.no.one.feat.library.data.local.services.storage.Constants

internal class LyricStorage : StorageService {
    override suspend fun retrieveLyric(uri: String) =
        StorageUtils.readFileFromDisk(File(uri)) ?: throw IOException("Reading Lyric failed.")

    override suspend fun insertLyric(bytes: ByteArray, filename: String): String {
        val dirPath = StorageUtils.createDir(Constants.LYRIC_DIRECTORY_PATH).path
        val file = File(dirPath, filename)
        val result = StorageUtils.saveFileToDisk(bytes, file)
        if (!result) throw IOException("Couldn't write the file into the local storage.")
        return file.path
    }
}

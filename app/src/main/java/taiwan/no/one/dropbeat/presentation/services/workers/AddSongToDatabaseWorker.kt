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

package taiwan.no.one.dropbeat.presentation.services.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.json.JSONArray
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.dropbeat.di.FeatModuleHelper
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_FILE_PATH
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_STREAM_DATA
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider

internal class AddSongToDatabaseWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), DIAware {
    override val di by DI.lazy { import(FeatModuleHelper.provide()) }
    private val libraryProvider by instance<LibraryMethodsProvider>()

    override suspend fun doWork(): Result {
        val stream = inputData.getString(PARAM_STREAM_DATA) ?: return Result.failure()
        val localFilePath = inputData.getStringArray(PARAM_FILE_PATH) ?: return Result.failure()
        val res = libraryProvider.downloadTrack(addAdditionInfo(stream, localFilePath))
        return if (res) Result.success() else Result.failure()
    }

    private fun addAdditionInfo(songStreams: String, path: Array<String>): String {
        // Do modify the string for mapping the song entity to local database entity column.
        val newStream = songStreams.replace("\"length\"", "\"duration\"")
            .replace("\"url\"", "\"uri\"")
            .replace("\"cdn_coverURL\"", "\"cover_uri\"")
        // Add extra information only for the local song entity into the json string data.
        return buildString {
            append('[')
            val jsonArray = JSONArray(newStream)
            repeat(jsonArray.length()) {
                append(buildString {
                    append(jsonArray[it].toString())
                    insert(1, "\"has_own\":true,")
                    insert(1, "\"local_uri\":\"${path[it]}\",")
                })
            }
            append(']')
        }
    }
}

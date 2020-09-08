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
import taiwan.no.one.dropbeat.di.FeatModuleHelper

internal class AddSongToDatabaseWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    companion object Constant {
        const val PARAM_STREAM_DATA = "songs streaming data"
        const val PARAM_FILE_PATH = "song file local path"
    }

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     * <p>
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        val stream = inputData.getString(PARAM_STREAM_DATA) ?: return Result.failure()
        val localPath = inputData.getStringArray(PARAM_FILE_PATH) ?: return Result.failure()
        val res = FeatModuleHelper.methodsProvider().downloadTrack(addAdditionInfo(stream, localPath))
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

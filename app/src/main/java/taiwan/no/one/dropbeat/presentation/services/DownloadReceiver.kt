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

package taiwan.no.one.dropbeat.presentation.services

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import org.kodein.di.DIAware
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.on
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.di.Constant.TAG_WORKER_ADD_SONG_TO_DB
import taiwan.no.one.dropbeat.di.Constant.TAG_WORKER_ADD_SONG_TO_PLAYLIST
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_FILE_PATH
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_PLAYLIST_ID
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_SONG_PATH
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_STREAM_DATA

internal class DownloadReceiver : BroadcastReceiver(), DIAware {
    override val di by lazy { (DropBeatApp.appContext as DropBeatApp).di }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (context?.applicationContext == null) return
            val downloadManager by di.on(context.applicationContext).instance<DownloadManager>()
            downloadManager.query(Query().setFilterById(downloadId)).use { cursor ->
                if (cursor.moveToFirst()) {
                    val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val songsStream = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                    val localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))

                    Toast.makeText(context, "Finished downloading $title", Toast.LENGTH_SHORT).show()
                    addSongsAndFavoriteList(songsStream, localUri)
                }
            }
        }
    }

    private fun addSongsAndFavoriteList(songsStream: String, localUri: String) {
        val dbData = Data.Builder()
            .putString(PARAM_STREAM_DATA, songsStream)
            .putStringArray(PARAM_FILE_PATH, arrayOf(localUri))
            .build()
        val playlistData = Data.Builder()
            .putInt(PARAM_PLAYLIST_ID, 1) // Download id is 1.
            .putString(PARAM_SONG_PATH, localUri)
            .build()
        val workManager by instance<WorkManager>()
        val dbWorker: (Data) -> OneTimeWorkRequest by factory(TAG_WORKER_ADD_SONG_TO_DB)
        val playlistWorker: (Data) -> OneTimeWorkRequest by factory(TAG_WORKER_ADD_SONG_TO_PLAYLIST)

        workManager
            .beginWith(dbWorker(dbData))
            .then(playlistWorker(playlistData))
            .enqueue()
    }
}

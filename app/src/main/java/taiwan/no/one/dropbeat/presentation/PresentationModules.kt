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

package taiwan.no.one.dropbeat.presentation

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.BuildConfig
import taiwan.no.one.dropbeat.presentation.services.workers.AddSongToDatabaseWorker
import taiwan.no.one.dropbeat.presentation.services.workers.CreateDefaultPlaylistWorker
import taiwan.no.one.dropbeat.provider.ModuleProvider

internal object PresentationModules : ModuleProvider {
    const val TAG_WORKER_INIT_DATA = "worker for initializing"
    const val TAG_WORKER_ADD_SONG = "worker for adding a song to the database"

    override fun provide(context: Context) = DI.Module("${BuildConfig.APPLICATION_ID} PreziModule") {
        bind<OneTimeWorkRequest>(TAG_WORKER_INIT_DATA) with singleton {
            OneTimeWorkRequestBuilder<CreateDefaultPlaylistWorker>().build()
        }
        bind<OneTimeWorkRequest>(TAG_WORKER_ADD_SONG) with factory { params: Data ->
            OneTimeWorkRequestBuilder<AddSongToDatabaseWorker>().setInputData(params).build()
        }
    }
}

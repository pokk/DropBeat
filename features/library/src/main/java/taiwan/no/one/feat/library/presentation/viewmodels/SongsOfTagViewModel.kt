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

package taiwan.no.one.feat.library.presentation.viewmodels

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.kodein.di.factory
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.di.Constant
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant
import taiwan.no.one.dropbeat.provider.ExploreMethodsProvider

internal class SongsOfTagViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val exploreProvider by instance<ExploreMethodsProvider>()
    private val workManager by instance<WorkManager>()
    private val oneTimeWorker: (Data) -> OneTimeWorkRequest by factory(Constant.TAG_WORKER_GET_SONGS_OF_TAG)
    private val gson by instance<Gson>()
    private val _songs by lazy { ResultLiveData<List<SimpleTrackEntity>>() }
    var songs: LiveData<Result<List<SimpleTrackEntity>>>? = null
        private set

    fun getSongs(tagName: String) = viewModelScope.launch {
        val request = Data.Builder().putString(WorkerConstant.PARAM_TAG_OF_NAME, tagName).build()
        val worker = oneTimeWorker(request)
        workManager.apply {
            songs = getWorkInfoByIdLiveData(worker.id).switchMap { workInfo ->
                when (workInfo.state) {
                    State.SUCCEEDED -> {
                        val json = workInfo.outputData.getString(WorkerConstant.PARAM_KEY_RESULT_OF_SONGS)
                        val result = gson.fromJson(json, Array<SimpleTrackEntity>::class.java).toList()
                        _songs.value = Result.success(result)
                    }
                    State.FAILED -> {
                        val errorMsg = workInfo.outputData.getString(WorkerConstant.KEY_EXCEPTION)
                        _songs.value = Result.failure(Exception(errorMsg))
                    }
                    else -> Unit
                }
                _songs
            }
            enqueue(worker)
        }
    }

    @WorkerThread
    fun getCoverThumb(entity: SimpleTrackEntity) = launchBehind {
        val newEntity = exploreProvider.getTrackCover(entity)
        val updatedList = _songs.value?.getOrNull()?.map {
            if (it.uri == entity.uri) newEntity else it
        } ?: return@launchBehind
        _songs.postValue(Result.success(updatedList))
    }

    override fun onCleared() {
        super.onCleared()
        songs = null
    }
}

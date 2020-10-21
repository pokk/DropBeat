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

package taiwan.no.one.feat.library.presentation.fragments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import com.google.gson.Gson
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.di.Constant.TAG_WORKER_GET_SONGS_OF_TAG
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant
import taiwan.no.one.feat.library.databinding.FragmentSongsOfTagBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.TrackAdapter
import java.lang.ref.WeakReference

internal class SongsOfTagPlaylistFragment : BaseFragment<BaseActivity<*>, FragmentSongsOfTagBinding>() {
    private val navArgs by navArgs<SongsOfTagPlaylistFragmentArgs>()
    private val workManager by instance<WorkManager>()
    private val worker: (Data) -> OneTimeWorkRequest by factory(TAG_WORKER_GET_SONGS_OF_TAG)
    private val gson by instance<Gson>()

    //region Variable of Recycler View
    private val adapter by lazy { TrackAdapter() }
    private val layoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()), RecyclerView.HORIZONTAL)
    }
    //endregion

    override fun bindLiveData() {
        workManager.getWorkInfosByTagLiveData(WorkerConstant.Tag.TAG_SONGS_OF_TAG).observe(this) {
            val workInfo = it.firstOrNull() ?: return@observe
            when (workInfo.state) {
                State.SUCCEEDED -> {
                    val json = workInfo.outputData.getString(WorkerConstant.PARAM_KEY_RESULT_OF_SONGS)
                    val result = gson.fromJson(json, Array<SimpleTrackEntity>::class.java).toList()
                    adapter.data = result
                }
                State.FAILED -> Unit
                else -> Unit
            }
        }
    }

    override fun viewComponentBinding() {
        binding.includeFavorite.rvMusics.apply {
            if (adapter == null) {
                adapter = this@SongsOfTagPlaylistFragment.adapter
            }
            if (layoutManager == null) {
                layoutManager = layoutManager()
            }
        }
    }

    override fun componentListenersBinding() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        val request = Data.Builder().putString(WorkerConstant.PARAM_TAG_OF_NAME, navArgs.name).build()
        workManager.enqueue(worker(request))
    }
}

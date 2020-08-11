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

package taiwan.no.one.feat.explore.presentation.fragments

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.databinding.FragmentExploreBinding
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.ExploreAdapter
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.TopChartAdapter
import taiwan.no.one.feat.explore.presentation.viewmodels.ExploreViewModel
import taiwan.no.one.ktx.livedata.obs
import taiwan.no.one.ktx.view.find
import taiwan.no.one.dropbeat.R as AppR

internal class ExploreFragment : BaseFragment<BaseActivity<*>, FragmentExploreBinding>() {
    private val vm by viewModels<ExploreViewModel>()

    // NOTE(Jieyi): 8/11/20 Because of some reasons, viewbinding can't use for `include` xml from other modules
    private val includeTopTrack by lazy { find<ConstraintLayout>(R.id.include_top_track) }
    private val includeTopArtist by lazy { find<ConstraintLayout>(R.id.include_top_artist) }

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        super.bindLiveData()
        vm.topTags.obs(this) { res ->
            res.onSuccess {
                binding.includeExplore.rvMusics.adapter = ExploreAdapter(it.tags.orEmpty())
            }.onFailure { }
        }
        vm.topArtists.obs(this) { res ->
            res.onSuccess {
                includeTopArtist.find<RecyclerView>(AppR.id.rv_musics).adapter =
                    TopChartAdapter(it.artists.subList(0, 4))
            }
        }
        vm.topTracks.obs(this) { res ->
            res.onSuccess {
                includeTopTrack.find<RecyclerView>(AppR.id.rv_musics).adapter = TopChartAdapter(it.tracks.subList(0, 4))
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        super.viewComponentBinding()
        binding.includeExplore.rvMusics.apply {
            if (layoutManager == null) {
                layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
            }
        }
        includeTopArtist.apply {
            find<RecyclerView>(AppR.id.rv_musics).apply {
                if (layoutManager == null) {
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
            find<TextView>(AppR.id.mtv_explore_title).text = "TopArtist"
        }
        includeTopTrack.apply {
            find<RecyclerView>(AppR.id.rv_musics).apply {
                if (layoutManager == null) {
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
            find<TextView>(AppR.id.mtv_explore_title).text = "TopTrack"
        }
    }
}

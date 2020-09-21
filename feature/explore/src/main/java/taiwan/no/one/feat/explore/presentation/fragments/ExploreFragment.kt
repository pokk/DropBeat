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

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.visible
import com.google.android.material.transition.MaterialSharedAxis
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.databinding.FragmentExploreBinding
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.ExploreAdapter
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.PlaylistAdapter
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.TopChartAdapter
import taiwan.no.one.feat.explore.presentation.viewmodels.ExploreViewModel
import taiwan.no.one.ktx.view.find
import java.lang.ref.WeakReference

internal class ExploreFragment : BaseFragment<BaseActivity<*>, FragmentExploreBinding>() {
    private val vm by viewModels<ExploreViewModel>()
    private val linearLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()))
    }
    private val playlistLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()), RecyclerView.HORIZONTAL)
    }

    // NOTE(Jieyi): 8/11/20 Because of some reasons, viewbinding can't use for `include` xml from other modules
    private val includePlaylist get() = find<ConstraintLayout>(R.id.include_playlist)
    private val includeTopTrack get() = find<ConstraintLayout>(R.id.include_top_track)
    private val includeTopArtist get() = find<ConstraintLayout>(R.id.include_top_artist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        addStatusBarHeightMarginTop(binding.mtvTitle)
        binding.includeExplore.rvMusics.apply {
            if (layoutManager == null) {
                layoutManager = GridLayoutManager(requireActivity(), 2, RecyclerView.HORIZONTAL, false)
            }
        }
        includePlaylist.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                if (layoutManager == null) {
                    layoutManager = playlistLayoutManager()
                }
            }
            find<TextView>(AppResId.mtv_explore_title).text = "Playlist"
            find<View>(AppResId.btn_play_all).visible()
            find<View>(AppResId.btn_play_all).gone()
        }
        includeTopArtist.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                if (layoutManager == null) {
                    layoutManager = linearLayoutManager()
                }
            }
            find<TextView>(AppResId.mtv_explore_title).text = "TopArtist"
        }
        includeTopTrack.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                if (layoutManager == null) {
                    layoutManager = linearLayoutManager()
                }
            }
            find<TextView>(AppResId.mtv_explore_title).text = "TopTrack"
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.playlists.observe(viewLifecycleOwner) { res ->
            res.onSuccess {
                includePlaylist.find<RecyclerView>(AppResId.rv_musics).adapter = PlaylistAdapter(it)
            }.onFailure { loge(it) }
        }
        vm.topTags.observe(viewLifecycleOwner) { res ->
            res.onSuccess {
                binding.includeExplore.rvMusics.adapter = ExploreAdapter(it.tags.orEmpty())
            }.onFailure { loge(it) }
        }
        vm.topArtists.observe(viewLifecycleOwner) { res ->
            res.onSuccess {
                includeTopArtist.find<RecyclerView>(AppResId.rv_musics).adapter = TopChartAdapter(it.subList(0, 4))
            }.onFailure { loge(it) }
        }
        vm.topTracks.observe(viewLifecycleOwner) { res ->
            res.onSuccess {
                includeTopTrack.find<RecyclerView>(AppResId.rv_musics).adapter =
                    TopChartAdapter(it.tracks.subList(0, 4))
            }.onFailure { loge(it) }
        }
    }
}

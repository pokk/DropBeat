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
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.devrapid.kotlinknifer.visible
import com.google.android.material.transition.MaterialSharedAxis
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity.TracksEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity
import taiwan.no.one.feat.explore.data.mappers.EntityMapper
import taiwan.no.one.feat.explore.databinding.FragmentExploreBinding
import taiwan.no.one.feat.explore.domain.usecases.ArtistWithMoreDetailEntities
import taiwan.no.one.feat.explore.domain.usecases.ArtistWithMoreDetailEntity
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

    // FIXME(jieyi): 10/30/20 This still has bug after update the layout manager.
    //    private val playlistLayoutManager get() = FirstBigSizeLayoutManager()
    private val playlistLayoutManager get() = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
    private val exploreAdapter by lazy { ExploreAdapter() }
    private val playlistAdapter by lazy { PlaylistAdapter() }
    private val topArtistAdapter by lazy { TopChartAdapter() }
    private val topTrackAdapter by lazy { TopChartAdapter() }

    // NOTE(Jieyi): 8/11/20 Because the layout xml is not in the module,
    //  viewbinding can't use for `include` xml from other modules.
    private val includePlaylist get() = find<ConstraintLayout>(R.id.include_playlist)
    private val includeTopTrack get() = find<ConstraintLayout>(R.id.include_top_track)
    private val includeTopArtist get() = find<ConstraintLayout>(R.id.include_top_artist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onPause() {
        super.onPause()
        // Keep the recyclerview state.
        vm.playlistState = includePlaylist.find<RecyclerView>(AppResId.rv_musics).layoutManager?.onSaveInstanceState()
    }

    override fun bindLiveData() {
        vm.playlists.observe(this) { res ->
            res.onSuccess {
                playlistAdapter.data = it
                // Restore the recyclerview state.
                includePlaylist.find<RecyclerView>(AppResId.rv_musics)
                    .layoutManager
                    ?.onRestoreInstanceState(vm.playlistState)
            }.onFailure { loge(it) }
        }
        vm.topTags.observe(this) { res ->
            res.onSuccess {
                it.tags?.takeIf { it.isNotEmpty() }?.also {
                    exploreAdapter.data = it
                }
            }.onFailure(::loge)
        }
        vm.topArtists.observe(this) { res ->
            res.onSuccess {
                topArtistAdapter.setDataset(it)
                setOnTopViewAllClick(includeTopArtist, it)
            }.onFailure(::loge)
            includeTopArtist.find<View>(AppResId.pb_progress).gone()
        }
        vm.topTracks.observe(this) { res ->
            res.onSuccess {
                topTrackAdapter.setDataset(it)
                setOnTopViewAllClick(includeTopTrack, it)
            }.onFailure(::loge)
            includeTopTrack.find<View>(AppResId.pb_progress).gone()
        }
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.mtvTitle)
        binding.includeExplore.rvMusics.apply {
            if (adapter == null) {
                adapter = exploreAdapter
            }
            if (layoutManager == null) {
                layoutManager = GridLayoutManager(requireActivity(), 2, RecyclerView.HORIZONTAL, false)
            }
        }
        includePlaylist.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                if (adapter == null) {
                    adapter = playlistAdapter
                }
                if (layoutManager == null) {
                    layoutManager = playlistLayoutManager
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
                if (adapter == null) {
                    adapter = topArtistAdapter
                }
            }
            find<TextView>(AppResId.mtv_explore_title).text = "TopArtist"
        }
        includeTopTrack.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                if (layoutManager == null) {
                    layoutManager = linearLayoutManager()
                }
                if (adapter == null) {
                    adapter = topTrackAdapter
                }
            }
            find<TextView>(AppResId.mtv_explore_title).text = "TopTrack"
        }
    }

    override fun componentListenersBinding() {
        binding.mtvTitle.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.actionExploreToPlayer())
        }
        exploreAdapter.setOnClickListener {
            it.name?.takeIf { it.isNotEmpty() }?.also {
                findNavController().navigate(ExploreFragmentDirections.actionExploreToPlaylistSongsOfTag(it))
            }
        }
        playlistAdapter.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.actionExploreToPlaylist(it.id))
        }
        listOf(topTrackAdapter, topArtistAdapter).forEach {
            it.setOnClickListener { }
            it.setOptionClickListener { }
            it.setFavoriteClickListener { vm.updateSong(it, it.isFavorite) }
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.getPlaylists()
        vm.getTopTracks()
        vm.getTopArtists()
    }

    private fun setOnTopViewAllClick(layout: ConstraintLayout, entities: Any) {
        var isTopArtist = false
        val list =
            ((entities as? ArtistWithMoreDetailEntities)
                 ?.apply { isTopArtist = true }
                 ?.map(EntityMapper::artistToSimpleTrackEntity)
                 ?.toTypedArray() ?: (entities as? TracksEntity)?.tracks
                 ?.map(EntityMapper::exploreToSimpleTrackEntity)
                 ?.toTypedArray()) ?: return
        layout.find<Button>(AppResId.btn_more).setOnClickListener {
            if (isTopArtist) {
                topArtistAdapter.data.forEachIndexed { index, entity ->
                    list[index].isFavorite =
                        (entity as ArtistWithMoreDetailEntity).second?.popularTrackThisWeek?.isFavorite ?: false
                }
            }
            else {
                topTrackAdapter.data.forEachIndexed { index, entity ->
                    logw(entity)
                    list[index].isFavorite = (entity as TrackEntity).isFavorite ?: false
                }
            }
            findNavController().navigate(ExploreFragmentDirections.actionExploreToPlaylist(
                songs = list,
                title = layout.find<TextView>(AppResId.mtv_explore_title).text.toString(),
            ))
        }
    }
}

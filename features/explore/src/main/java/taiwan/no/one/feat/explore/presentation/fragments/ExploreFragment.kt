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
import com.devrapid.kotlinknifer.visible
import com.google.android.material.transition.MaterialSharedAxis
import java.lang.ref.WeakReference
import org.kodein.di.provider
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.AppResMenu
import taiwan.no.one.dropbeat.core.PlaylistConstant
import taiwan.no.one.dropbeat.di.Constant as DiConstant
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.dropbeat.presentation.activities.MainActivity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.databinding.FragmentExploreBinding
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.ExploreAdapter
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.PlaylistAdapter
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.TopChartAdapter
import taiwan.no.one.feat.explore.presentation.viewmodels.AnalyticsViewModel
import taiwan.no.one.feat.explore.presentation.viewmodels.ExploreViewModel
import taiwan.no.one.feat.ranking.presentation.fragments.RankingFragment
import taiwan.no.one.ktx.intent.shareText
import taiwan.no.one.ktx.view.find
import taiwan.no.one.widget.popupmenu.popupMenuWithIcon

internal class ExploreFragment : BaseFragment<MainActivity, FragmentExploreBinding>() {
    private val vm by viewModels<ExploreViewModel>()
    private val analyticsVm by viewModels<AnalyticsViewModel>()
    private val linearLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()))
    }
    private val noneEdgeEffectFactory by provider<RecyclerView.EdgeEffectFactory>(DiConstant.TAG_EDGE_FACTORY_NONE)

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

    override fun onResume() {
        super.onResume()
        // HACK(jieyi): 5/2/21 Will set a callback function to [RankingFragment] until we find the
        //  best solution to set the navigation destination.
        findRankingFragment()?.navigationCallback = { title, rankId ->
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreToPlaylist(
                    rankId = rankId,
                    title = title,
                    isFixed = true,
                )
            )
            analyticsVm.navigatedToPlaylist("playlist name: $title")
        }
    }

    override fun onPause() {
        super.onPause()
        // Keep the recyclerview state.
        vm.playlistState = includePlaylist.find<RecyclerView>(AppResId.rv_musics).layoutManager?.onSaveInstanceState()
        findRankingFragment()?.navigationCallback = null
    }

    override fun onDestroyView() {
        // Note that this recyclerView is an old one and different instance from the one in onViewCreated.
        binding.includeExplore.rvMusics.adapter = null
        includePlaylist.find<RecyclerView>(AppResId.rv_musics).adapter = null
        includeTopArtist.find<RecyclerView>(AppResId.rv_musics).adapter = null
        includeTopTrack.find<RecyclerView>(AppResId.rv_musics).adapter = null
        super.onDestroyView()
    }

    override fun bindLiveData() {
        vm.playlists.observe(this) { res ->
            res.onSuccess {
                playlistAdapter.submitList(it)
                // Restore the recyclerview state.
                includePlaylist.find<RecyclerView>(AppResId.rv_musics)
                    .layoutManager
                    ?.onRestoreInstanceState(vm.playlistState)
            }.onFailure(::loge)
        }
        vm.topArtists.observe(this) { res ->
            res.onSuccess(topArtistAdapter::setDataset).onFailure(::loge)
        }
        vm.topTracks.observe(this) { res ->
            res.onSuccess(topTrackAdapter::setDataset).onFailure(::loge)
        }
        vm.topSimpleEntities.observe(this, ::setTopTracksClickEvent)
        vm.resultOfFavorite.observe(this) {
            if (!it) return@observe
            // Update the playlist information.
            vm.getPlaylists()
        }
    }

    private fun setTopTracksClickEvent(pair: Pair<Int, Array<SimpleTrackEntity>>) {
        val (type, entities) = pair
        val layout = when (type) {
            ExploreViewModel.TOP_TRACK -> includeTopTrack
            ExploreViewModel.TOP_ARTIST -> includeTopArtist
            else -> return
        }
        layout.apply {
            find<View>(AppResId.pb_progress).gone()
            find<Button>(AppResId.btn_more).setOnClickListener {
                val playlistName = layout.find<TextView>(AppResId.mtv_explore_title).text.toString()
                findNavController().navigate(
                    ExploreFragmentDirections.actionExploreToPlaylist(
                        songs = entities,
                        title = playlistName,
                        isFixed = true,
                    )
                )
                analyticsVm.navigatedToPlaylist("playlist name: $playlistName")
            }
        }
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.mtvTitle)
        binding.includeExplore.rvMusics.apply {
            adapter = exploreAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2, RecyclerView.HORIZONTAL, false)
            edgeEffectFactory = noneEdgeEffectFactory()
        }
        includePlaylist.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                adapter = playlistAdapter
                layoutManager = playlistLayoutManager
                edgeEffectFactory = noneEdgeEffectFactory()
            }
            find<TextView>(AppResId.mtv_explore_title).text = "Playlist"
            find<View>(AppResId.btn_play_all).visible()
            find<View>(AppResId.btn_play_all).gone()
        }
        includeTopArtist.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                layoutManager = linearLayoutManager()
                adapter = topArtistAdapter
            }
            find<TextView>(AppResId.mtv_explore_title).text = "TopArtist"
        }
        includeTopTrack.apply {
            find<RecyclerView>(AppResId.rv_musics).apply {
                layoutManager = linearLayoutManager()
                adapter = topTrackAdapter
            }
            find<TextView>(AppResId.mtv_explore_title).text = "TopTrack"
        }
    }

    override fun componentListenersBinding() {
        exploreAdapter.setOnClickListener {
            it.name?.takeIf { it.isNotEmpty() }?.also {
                findNavController().navigate(ExploreFragmentDirections.actionExploreToPlaylistSongsOfTag(it))
                analyticsVm.navigatedToPlaylist("song tag: $it")
            }
        }
        playlistAdapter.setOnClickListener {
            // 1: favorite playlist, 2: downloaded playlist
            val isFixedPlaylist = it.id in listOf(PlaylistConstant.DOWNLOADED, PlaylistConstant.FAVORITE)
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreToPlaylist(it.id, isFixed = isFixedPlaylist)
            )
            analyticsVm.navigatedToPlaylist("playlist name: ${it.name}")
        }
        listOf(topTrackAdapter, topArtistAdapter).forEach {
            it.setOnClickListener {
                analyticsVm.clickedPlayAMusic(it.obtainTrackAndArtistName())
            }
            it.setOptionClickListener { v, entity ->
                showMenu(v, entity)
                analyticsVm.clickedOption(entity.obtainTrackAndArtistName())
            }
            it.setFavoriteClickListener {
                vm.updateSong(it, it.isFavorite)
                analyticsVm.clickedFavorite(it.isFavorite, it.obtainTrackAndArtistName())
            }
        }
        binding.mtvTitle.setOnClickListener { parent.displayPlayer() }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.getPlaylists()
        vm.getTopTracks()
        vm.getTopArtists()
        vm.topTags.observe(viewLifecycleOwner) { res ->
            res.onSuccess(exploreAdapter::submitList).onFailure(::loge)
            binding.includeExplore.pbProgress.gone()
        }
    }

    private fun findRankingFragment() =
        childFragmentManager.findFragmentByTag("part_explore") as? RankingFragment

    private fun showMenu(anchor: View, entity: SimpleTrackEntity) =
        popupMenuWithIcon(requireActivity(), anchor, AppResMenu.menu_more_track).apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    AppResId.item_information -> {
                        findNavController().navigate(ExploreFragmentDirections.actionExploreToNavArtist(entity))
                        analyticsVm.navigatedToArtist(entity.artist)
                    }
                    AppResId.item_share -> {
                        shareText(requireActivity(), entity.uri)
                        analyticsVm.clickedShare(entity.uri)
                    }
                }
                true
            }
        }.show()
}

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

package taiwan.no.one.feat.library.presentation.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.getDimen
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.visible
import java.lang.ref.WeakReference
import org.kodein.di.factory
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.library.R
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.mappers.EntityMapper
import taiwan.no.one.feat.library.databinding.FragmentPlaylistBinding
import taiwan.no.one.feat.library.databinding.StubNoSongsBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.TrackAdapter
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel
import taiwan.no.one.ktx.view.find
import taiwan.no.one.widget.WidgetResDimen
import taiwan.no.one.widget.popupmenu.popupMenuWithIcon

internal class PlaylistFragment : BaseLibraryFragment<BaseActivity<*>, FragmentPlaylistBinding>() {
    private var willRemoveEntity: SimpleTrackEntity? = null
    private var playlist: PlayListEntity? = null
    private var prevSavedState: SavedStateHandle? = null

    //region Variable of View Binding
    private val noSongsBinding get() = StubNoSongsBinding.bind(binding.root)
    //endregion

    //region Variable of View Model
    private val vm by viewModels<PlaylistViewModel>()
    //endregion

    //region Variable of Recycler View
    private val playlistAdapter by lazy { TrackAdapter() }
    private val layoutManager: (LayoutManagerParams) -> LinearLayoutManager by factory()
    //endregion

    private val navArgs by navArgs<PlaylistFragmentArgs>()

    //region Lifecycle
    override fun onResume() {
        super.onResume()
        // NOTE(jieyi): 11/26/20 [previousBackStackEntry?.savedStateHandle] will be null after onPause() so it
        //  should be kept. Still don't know the reason why it will be null after onPause().
        prevSavedState = findNavController().previousBackStackEntry?.savedStateHandle
    }

    override fun onDestroyView() {
        find<RecyclerView>(AppResId.rv_musics).adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        prevSavedState?.set("playlist", playlistAdapter.data)
        prevSavedState = null
    }
    //endregion

    override fun bindLiveData() {
        vm.playlist.observe(this) { res ->
            res.onSuccess {
                playlist = it
                binding.mtvTitle.text = it.name
                if (it.songs.isEmpty()) {
                    displayNoSongs()
                }
                else {
                    displaySongs(it.songs.map(EntityMapper::libraryToSimpleTrackEntity))
                }
            }.onFailure(::loge)
        }
        vm.tracks.observe(this) { res ->
            res.onSuccess {
                if (it.isEmpty()) displayNoSongs() else displaySongs(it)
            }.onFailure(::loge)
        }
        vm.resultOfFavorite.observe(this) { res ->
            res.onSuccess {
                if (it && navArgs.playlistId == 2) {
                    willRemoveEntity?.let(playlistAdapter::removeItem)
                    willRemoveEntity = null
                    if (playlistAdapter.data.isEmpty()) {
                        displayNoSongs()
                    }
                    else {
                        displaySongs(playlistAdapter.data)
                    }
                }
            }
        }
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.btnBack)
    }

    override fun componentListenersBinding() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
            analyticsVm.navigatedGoBackFromPlaylist()
        }
        binding.btnMore.setOnClickListener { showMoreMenu(binding.btnMore) }
        playlistAdapter.apply {
            setOnClickListener {
                analyticsVm.clickedPlayAMusic(it.obtainTrackAndArtistName())
            }
            setOptionClickListener { v, entity ->
                showOptionMenu(v, entity) { navigateToArtist(entity) }
                analyticsVm.clickedOption(entity.obtainTrackAndArtistName())
            }
            setFavoriteClickListener {
                willRemoveEntity = it
                vm.updateSong(it, it.isFavorite)
                analyticsVm.clickedFavorite(it.isFavorite, it.obtainTrackAndArtistName())
            }
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        // TODO(jieyi): 6/21/21 To have the isFavorite value correctly, we will process isFavorite after we get
        //  the list.
        when {
            navArgs.playlistId != -1 -> {
                vm.getSongs(navArgs.playlistId)
            }
            navArgs.rankId != -1 -> {
                binding.mtvTitle.text = navArgs.title
                binding.pbAllProgress.visible()
                vm.getRankSongs(navArgs.rankId)
            }
            navArgs.songs != null -> {
                displaySongs(navArgs.songs?.toList().orEmpty())
            }
        }
        vm.playlistDuration.observe(viewLifecycleOwner) {
            // Set the visibility for this fragment.
            binding.mtvSubtitle.text = it
        }
    }

    private fun displaySongs(songs: List<SimpleTrackEntity>) {
        // Calculate the total of all songs' duration.
        vm.cumulateDuration(songs)
        binding.pbAllProgress.gone()
        find<View>(AppResId.pb_progress).gone()
        find<View>(R.id.include_favorite).visible()
        // Set the recycler view.
        find<RecyclerView>(AppResId.rv_musics).apply {
            // Make the height to fit the view height.
            updateLayoutParams { height = getDimen(WidgetResDimen.md_zero_unit).toInt() }
            adapter = playlistAdapter
            layoutManager = layoutManager(LayoutManagerParams(WeakReference(requireActivity())))
        }
        // Set the song into the adapter.
        playlistAdapter.data = songs
        // Set the section title.
        find<TextView>(AppResId.mtv_explore_title).text = "All Songs"
        // Hide the view more button.
        find<View>(AppResId.btn_more).gone()
        binding.btnPlayAll.visible()
    }

    private fun displayNoSongs() {
        find<View>(AppResId.pb_progress).gone()
        find<View>(R.id.include_favorite).gone()
        binding.btnPlayAll.gone()
        binding.vsNoSongs.takeUnless(ViewStub::isVisible)?.inflate()
        noSongsBinding.btnSearch.setOnClickListener {
            // Go to the search page.
            analyticsVm.navigatedToSearch()
        }
        binding.mtvSubtitle.text = "0 Songs"
    }

    private fun navigateToArtist(entity: SimpleTrackEntity) {
        findNavController().navigate(PlaylistFragmentDirections.actionPlaylistToNavArtist(entity))
        analyticsVm.navigatedFromPlaylistToArtist(entity.name)
    }

    private fun showMoreMenu(anchor: View) =
        popupMenuWithIcon(requireActivity(), anchor, R.menu.menu_more_playlist).apply {
            // If the playlist can't be modified, those button should be removed.
            if (navArgs.isFixed) {
                listOf(R.id.item_rename, R.id.item_delete, R.id.item_duplicate).forEach(menu::removeItem)
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_duplicate -> {
                        playlist?.let {
                            val simplePlaylist = EntityMapper.playlistToSimplePlaylistEntity(it)
                            findNavController()
                                .navigate(PlaylistFragmentDirections.actionPlaylistToCreate(simplePlaylist))
                        }
                        analyticsVm.navigatedToCreate()
                    }
                    R.id.item_share -> Unit
                    R.id.item_download_all -> Unit
                    R.id.item_rename -> {
                        findNavController()
                            .navigate(PlaylistFragmentDirections.actionPlaylistToRename(navArgs.playlistId))
                        analyticsVm.navigatedToRename()
                    }
                    R.id.item_delete -> Unit
                }
                true
            }
        }.show()
}

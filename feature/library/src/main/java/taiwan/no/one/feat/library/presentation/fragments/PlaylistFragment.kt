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
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.getDimen
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.visible
import com.google.android.material.transition.MaterialSharedAxis
import org.kodein.di.factory
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.core.helpers.StringUtil
import taiwan.no.one.dropbeat.data.entities.SimplePlaylistEntity
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
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
import java.lang.ref.WeakReference

internal class PlaylistFragment : BaseFragment<BaseActivity<*>, FragmentPlaylistBinding>() {
    private var willRemoveEntity: SimpleTrackEntity? = null
    private var playlist: PlayListEntity? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

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
        vm.resultOfFavorite.observe(this) { res ->
            res.onSuccess {
                if (it && navArgs.playlistId == 2) {
                    willRemoveEntity?.let(playlistAdapter::removeItem)
                    willRemoveEntity = null
                    if (playlistAdapter.data.isEmpty()) {
                        displayNoSongs()
                    }
                }
            }
        }
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.btnBack)
    }

    override fun componentListenersBinding() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnMore.setOnClickListener { showMoreMenu(binding.btnMore) }
        playlistAdapter.apply {
            setOnClickListener {}
            setOptionClickListener { }
            setFavoriteClickListener {
                willRemoveEntity = it
                vm.updateSong(it, it.isFavorite)
            }
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        if (navArgs.playlistId != -1 && navArgs.songs == null) {
            vm.getSongs(navArgs.playlistId)
        }
        if (navArgs.playlistId == -1 && navArgs.songs != null) {
            navArgs.songs?.also { displaySongs(it.toList()) } ?: displayNoSongs()
        }
    }

    private fun displaySongs(songs: List<SimpleTrackEntity>) {
        find<View>(AppResId.pb_progress).gone()
        find<View>(R.id.include_favorite).visible()
        // Set the recycler view.
        find<RecyclerView>(AppResId.rv_musics).apply {
            // Make the height to fit the view height.
            updateLayoutParams {
                height = getDimen(WidgetResDimen.md_zero_unit).toInt()
            }
            if (adapter == null) {
                adapter = playlistAdapter
            }
            if (layoutManager == null) {
                layoutManager = layoutManager(LayoutManagerParams(WeakReference(requireActivity())))
            }
            (adapter as? TrackAdapter)?.data = songs
        }
        // Set the section title.
        find<TextView>(AppResId.mtv_explore_title).text = "All Songs"
        // Hide the view more button.
        find<View>(AppResId.btn_more).gone()
        val duration = songs.fold(0) { acc, song -> acc + song.duration }
        // Set the visibility for this fragment.
        binding.mtvSubtitle.text =
            "${songs.size} Songs・${StringUtil.buildDurationToTime(duration.toLong())}・30 mins ago played"
        binding.btnPlayAll.visible()
    }

    private fun displayNoSongs() {
        find<View>(AppResId.pb_progress).gone()
        find<View>(R.id.include_favorite).gone()
        binding.btnPlayAll.gone()
        binding.vsNoSongs.takeIf { !it.isVisible }?.inflate()
        noSongsBinding.btnSearch.setOnClickListener {
            // Go to the search page.
        }
        binding.mtvSubtitle.text = "0 Songs"
    }

    private fun showMoreMenu(anchor: View) =
        popupMenuWithIcon(requireActivity(), anchor, R.menu.menu_more_playlist).apply {
            if (navArgs.playlistId in listOf(1, 2)) {
                menu.removeItem(R.id.item_rename)
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_duplicate -> playlist?.let {
                        val simplePlaylist = SimplePlaylistEntity(it.id, it.name, it.songIds, "")
                        findNavController()
                            .navigate(PlaylistFragmentDirections.actionPlaylistToCreate(simplePlaylist))
                    }
                    R.id.item_share -> Unit
                    R.id.item_download_all -> Unit
                    R.id.item_rename -> findNavController()
                        .navigate(PlaylistFragmentDirections.actionPlaylistToRename(navArgs.playlistId))
                    R.id.item_delete -> Unit
                }
                true
            }
        }.show()
}

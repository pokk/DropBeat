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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.gone
import java.lang.ref.WeakReference
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.library.databinding.FragmentSongsOfTagBinding
import taiwan.no.one.feat.library.databinding.MergeLayoutSongsOfTypeBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.TrackAdapter
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel
import taiwan.no.one.feat.library.presentation.viewmodels.SongsOfTagViewModel

internal class SongsOfTagPlaylistFragment : BaseLibraryFragment<BaseActivity<*>, FragmentSongsOfTagBinding>() {
    //region Variable of View Binding
    private val merge get() = MergeLayoutSongsOfTypeBinding.bind(binding.root)
    //endregion

    //region Variable of View Model
    private val vm by viewModels<SongsOfTagViewModel>()
    private val playlistViewModel by viewModels<PlaylistViewModel>()
    //endregion

    //region Variable of Recycler View
    private val adapter by lazy { TrackAdapter() }
    private val layoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()), RecyclerView.VERTICAL)
    }
    //endregion

    private val navArgs by navArgs<SongsOfTagPlaylistFragmentArgs>()

    override fun onDestroyView() {
        merge.rvMusics.adapter = null
        super.onDestroyView()
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.btnBack)
        binding.mtvTitle.text = navArgs.name
        merge.rvMusics.apply {
            adapter = this@SongsOfTagPlaylistFragment.adapter
            layoutManager = layoutManager()
        }
    }

    override fun componentListenersBinding() {
        adapter.apply {
            setOnClickListener {
                analyticsVm.clickedPlayAMusic(it.obtainTrackAndArtistName())
            }
            setOptionClickListener { v, entity ->
                showOptionMenu(v, entity) { navigateToArtist(entity) }
                analyticsVm.clickedOption(entity.obtainTrackAndArtistName())
            }
            setFavoriteClickListener {
                playlistViewModel.updateSong(it, it.isFavorite)
                analyticsVm.clickedFavorite(it.isFavorite, it.obtainTrackAndArtistName())
            }
            setRequestPicCallback {
                // TODO(jieyi): 1/13/21 Send the pic request to the backend server thru a viewmodel.
                vm.getCoverThumb(it)
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
            analyticsVm.navigatedGoBackFromTagPlaylist()
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.apply {
            if (songs?.value == null) getSongs(navArgs.name)
            songs?.observe(viewLifecycleOwner) { res ->
                res.onSuccess {
                    adapter.data = it
                }.onFailure {
                    showError(it.localizedMessage.orEmpty())
                }
                merge.pbProgress.gone()
            }
        }
    }

    private fun navigateToArtist(entity: SimpleTrackEntity) {
        findNavController().navigate(SongsOfTagPlaylistFragmentDirections.actionSongsOfTagToNavArtist(entity))
        analyticsVm.navigatedFromPlaylistToArtist(entity.artist)
    }
}

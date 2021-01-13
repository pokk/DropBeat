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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.gone
import com.google.android.material.transition.MaterialSharedAxis
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.AppResMenu
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.feat.library.databinding.FragmentSongsOfTagBinding
import taiwan.no.one.feat.library.databinding.MergeLayoutSongsOfTypeBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.TrackAdapter
import taiwan.no.one.feat.library.presentation.viewmodels.AnalyticsViewModel
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel
import taiwan.no.one.feat.library.presentation.viewmodels.SongsOfTagViewModel
import taiwan.no.one.widget.popupmenu.popupMenuWithIcon
import java.lang.ref.WeakReference

internal class SongsOfTagPlaylistFragment : BaseFragment<BaseActivity<*>, FragmentSongsOfTagBinding>() {
    //region Variable of View Binding
    private val merge get() = MergeLayoutSongsOfTypeBinding.bind(binding.root)
    //endregion

    //region Variable of View Model
    private val vm by viewModels<SongsOfTagViewModel>()
    private val playlistViewModel by viewModels<PlaylistViewModel>()
    private val analyticsVm by viewModels<AnalyticsViewModel>()
    //endregion

    //region Variable of Recycler View
    private val adapter by lazy { TrackAdapter() }
    private val layoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()), RecyclerView.VERTICAL)
    }
    //endregion

    private val navArgs by navArgs<SongsOfTagPlaylistFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onDestroyView() {
        merge.rvMusics.adapter = null
        super.onDestroyView()
    }

    override fun viewComponentBinding() {
        addStatusBarHeightMarginTop(binding.btnBack)
        binding.mtvTitle.text = navArgs.name
        merge.rvMusics.apply {
            if (adapter == null) {
                adapter = this@SongsOfTagPlaylistFragment.adapter
            }
            if (layoutManager == null) {
                layoutManager = layoutManager()
            }
        }
    }

    override fun componentListenersBinding() {
        adapter.apply {
            setOnClickListener {
                analyticsVm.clickedPlayAMusic(it.obtainTrackAndArtistName())
            }
            setOptionClickListener { v, entity ->
                showOptionMenu(v)
                analyticsVm.clickedOption(entity.obtainTrackAndArtistName())
            }
            setFavoriteClickListener {
                playlistViewModel.updateSong(it, it.isFavorite)
                analyticsVm.clickedFavorite(it.isFavorite, it.obtainTrackAndArtistName())
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
            analyticsVm.navigatedGoBackFromTagPlaylist()
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.apply {
            getSongs(navArgs.name)
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

    private fun showOptionMenu(anchor: View) =
        popupMenuWithIcon(requireActivity(), anchor, AppResMenu.menu_more_track).apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    AppResId.item_information -> Unit
                    AppResId.item_share -> Unit
                }
                true
            }
        }.show()
}

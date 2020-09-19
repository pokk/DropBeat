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
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.visible
import com.devrapid.kotlinshaver.isNotNull
import com.google.android.material.transition.MaterialSharedAxis
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.dropbeat.presentation.viewmodels.PrivacyViewModel
import taiwan.no.one.feat.library.R
import taiwan.no.one.feat.library.databinding.FragmentMyPageBinding
import taiwan.no.one.feat.library.databinding.MergeTopControllerBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.PlaylistAdapter
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.TrackAdapter
import taiwan.no.one.feat.library.presentation.viewmodels.MyHomeViewModel
import taiwan.no.one.ktx.view.find
import java.lang.ref.WeakReference

class MyHomeFragment : BaseFragment<BaseActivity<*>, FragmentMyPageBinding>() {
    private var _mergeTopControllerBinding: MergeTopControllerBinding? = null
    private val mergeTopControllerBinding get() = checkNotNull(_mergeTopControllerBinding)
    private val includePlaylist get() = find<ConstraintLayout>(R.id.include_playlist)
    private val includeFavorite get() = find<ConstraintLayout>(R.id.include_favorite)
    private val includeDownloaded get() = find<ConstraintLayout>(R.id.include_download)
    private val includeHistory get() = find<ConstraintLayout>(R.id.include_history)
    private val userEntity get() = privacyVm.userInfo.value?.getOrNull()
    private val privacyVm by activityViewModels<PrivacyViewModel>()
    private val vm by viewModels<MyHomeViewModel>()
    private val linearLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()))
    }
    private val playlistLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()), RecyclerView.HORIZONTAL)
    }
    private val playlistAdapter by lazy { PlaylistAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
    }

    override fun bindLiveData() {
        vm.playlists.observe(this) { res ->
            res.onSuccess {
                playlistAdapter.data = it
                vm.extractMainPlaylist(it)
            }.onFailure {
                loge(it)
            }
        }
        vm.downloaded.observe(this) {
            if (it.songs.isEmpty()) {
                includeDownloaded.find<TextView>(AppResId.mtv_no_music).visible()
            }
            else {
                (includeDownloaded.find<RecyclerView>(AppResId.rv_musics).adapter as? TrackAdapter)?.data = it.songs
                    .let { songs -> if (songs.size <= 4) songs else songs.subList(0, 4) }
            }
        }
        vm.favorites.observe(this) {
            if (it.songs.isEmpty()) {
                includeFavorite.find<TextView>(AppResId.mtv_no_music).visible()
            }
            else {
                (includeFavorite.find<RecyclerView>(AppResId.rv_musics).adapter as? TrackAdapter)?.data = it.songs
                    .let { songs -> if (songs.size <= 4) songs else songs.subList(0, 4) }
            }
        }
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        _mergeTopControllerBinding = MergeTopControllerBinding.bind(binding.root)
        includePlaylist.apply {
            find<TextView>(AppResId.mtv_explore_title).text = "Playlist"
            find<View>(AppResId.btn_play_all).gone()
            find<RecyclerView>(AppResId.rv_musics).apply {
                if (adapter == null) {
                    adapter = playlistAdapter
                }
                if (layoutManager == null) {
                    layoutManager = playlistLayoutManager()
                }
            }
        }
        includeFavorite.find<TextView>(AppResId.mtv_explore_title).text = "Favorite"
        includeDownloaded.find<TextView>(AppResId.mtv_explore_title).text = "Downloaded"
        includeHistory.find<TextView>(AppResId.mtv_explore_title).text = "History"
        includeFavorite.find<RecyclerView>(AppResId.rv_musics).apply {
            if (adapter == null) {
                adapter = TrackAdapter()
            }
            if (layoutManager == null) {
                layoutManager = linearLayoutManager()
            }
        }
        includeDownloaded.find<RecyclerView>(AppResId.rv_musics).apply {
            if (adapter == null) {
                adapter = TrackAdapter()
            }
            if (layoutManager == null) {
                layoutManager = linearLayoutManager()
            }
        }
    }

    override fun componentListenersBinding() {
        mergeTopControllerBinding.apply {
            btnLogin.setOnClickListener {
                findNavController().navigate(MyHomeFragmentDirections.actionMyHomeToLoginGraph())
            }
            btnSetting.setOnClickListener {
                findNavController().navigate(MyHomeFragmentDirections.actionMyHomeToSettingGraph())
            }
        }
        listOf(
            includeFavorite.find<Button>(AppResId.btn_more) to 2,
            includeDownloaded.find<Button>(AppResId.btn_more) to 1,
            includeHistory.find<Button>(AppResId.btn_more) to 3,
        ).forEach { (button, id) ->
            button.setOnClickListener {
                findNavController().navigate(MyHomeFragmentDirections.actionMyHomeToPlaylist(id))
            }
        }
        (includeFavorite.find<RecyclerView>(AppResId.rv_musics).adapter as? TrackAdapter)?.setOnClickListener {
        }
        (includeDownloaded.find<RecyclerView>(AppResId.rv_musics).adapter as? TrackAdapter)?.setOnClickListener {
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        userEntity?.takeIf { it.uid.isNotNull() }?.let {
            mergeTopControllerBinding.mtvTitle.text = it.displayName ?: it.email
            mergeTopControllerBinding.btnLogin.gone()
        }
        vm.getAllPlaylists()
    }
}

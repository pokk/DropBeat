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
import com.devrapid.kotlinknifer.logw
import com.devrapid.kotlinknifer.visible
import com.devrapid.kotlinshaver.isNotNull
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
import taiwan.no.one.feat.library.presentation.viewmodels.MyHomeViewModel
import taiwan.no.one.ktx.view.find
import java.lang.ref.WeakReference

class MyHomeFragment : BaseFragment<BaseActivity<*>, FragmentMyPageBinding>() {
    private var _mergeTopControllerBinding: MergeTopControllerBinding? = null
    private val mergeTopControllerBinding get() = checkNotNull(_mergeTopControllerBinding)
    private val includeFavorite by lazy { find<ConstraintLayout>(R.id.include_favorite) }
    private val includeDownloaded by lazy { find<ConstraintLayout>(R.id.include_download) }
    private val includeHistory by lazy { find<ConstraintLayout>(R.id.include_history) }
    private val userEntity get() = privacyVm.userInfo.value?.getOrNull()
    private val privacyVm by activityViewModels<PrivacyViewModel>()
    private val vm by viewModels<MyHomeViewModel>()
    private val linearLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()))
    }

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.playlists.observe(this) { res ->
            res.onSuccess {
                logw(it)
                vm.extractMainPlaylist(it)
            }.onFailure {
                loge(it)
            }
        }
        vm.downloaded.observe(this) {
            if (it.songs.isEmpty()) {
                includeDownloaded.find<TextView>(AppResId.mtv_no_music).visible()
            }
            (includeDownloaded.find<RecyclerView>(AppResId.rv_musics).adapter as? PlaylistAdapter)?.data = it.songs
        }
        vm.favorites.observe(this) {
            if (it.songs.isEmpty()) {
                includeFavorite.find<TextView>(AppResId.mtv_no_music).visible()
            }
            (includeFavorite.find<RecyclerView>(AppResId.rv_musics).adapter as? PlaylistAdapter)?.data = it.songs
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        super.viewComponentBinding()
        _mergeTopControllerBinding = MergeTopControllerBinding.bind(binding.root)
        includeFavorite.find<TextView>(AppResId.mtv_explore_title).text = "Favorite"
        includeDownloaded.find<TextView>(AppResId.mtv_explore_title).text = "Downloaded"
        includeHistory.find<TextView>(AppResId.mtv_explore_title).text = "History"
        includeFavorite.find<RecyclerView>(AppResId.rv_musics).apply {
            if (adapter == null) {
                adapter = PlaylistAdapter()
            }
            if (layoutManager == null) {
                layoutManager = linearLayoutManager()
            }
        }
        includeDownloaded.find<RecyclerView>(AppResId.rv_musics).apply {
            if (adapter == null) {
                adapter = PlaylistAdapter()
            }
            if (layoutManager == null) {
                layoutManager = linearLayoutManager()
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
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
            includeFavorite.find<Button>(AppResId.mtv_more) to 2,
            includeDownloaded.find<Button>(AppResId.mtv_more) to 1,
            includeHistory.find<Button>(AppResId.mtv_more) to 3,
        ).forEach { (button, id) ->
            button.setOnClickListener {
                findNavController().navigate(MyHomeFragmentDirections.actionMyHomeToPlaylist(id))
            }
        }
    }

    /**
     * Initialize doing some methods or actions here.
     *
     * @param savedInstanceState previous status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
        userEntity?.takeIf { it.uid.isNotNull() }?.let {
            mergeTopControllerBinding.mtvTitle.text = it.displayName
            mergeTopControllerBinding.btnLogin.gone()
        }
        vm.getAllPlaylists()
    }
}

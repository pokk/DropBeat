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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.kodein.di.factory
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.AppResId
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.feat.library.databinding.FragmentPlaylistBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.PlaylistAdapter
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel
import taiwan.no.one.ktx.view.find
import java.lang.ref.WeakReference

internal class PlaylistFragment : BaseFragment<BaseActivity<*>, FragmentPlaylistBinding>() {
    private val vm by viewModels<PlaylistViewModel>()
    private val playlistAdapter by lazy { PlaylistAdapter() }
    private val layoutManager: (LayoutManagerParams) -> LinearLayoutManager by factory()

    override fun bindLiveData() {
        vm.playlist.observe(this) { res ->
            res.onSuccess {
                (find<RecyclerView>(AppResId.rv_musics).adapter as? PlaylistAdapter)?.data = it.songs
            }.onFailure {
            }
        }
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        find<RecyclerView>(AppResId.rv_musics).apply {
            if (adapter == null) {
                adapter = playlistAdapter
            }
            if (layoutManager == null) {
                layoutManager = layoutManager(LayoutManagerParams(WeakReference(requireActivity())))
            }
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
    }
}

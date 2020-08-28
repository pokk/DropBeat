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

package taiwan.no.one.feat.ranking.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.presentation.activity.MainActivity
import taiwan.no.one.feat.ranking.databinding.FragmentRankingDetailBinding
import taiwan.no.one.feat.ranking.presentation.recyclerviews.adapters.SongAdapter
import taiwan.no.one.feat.ranking.presentation.viewmodels.RankViewModel

internal class DetailFragment : BaseFragment<MainActivity, FragmentRankingDetailBinding>() {
    private val vm by viewModels<RankViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.musics.observe(this) { res ->
            res.onSuccess {
                (binding.rvSongs.adapter as? SongAdapter)?.addExtraEntities(it)
                parent.hideLoading()
            }.onFailure {
                parent.showError(it.message.toString())
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        binding.rvSongs.apply {
            if (adapter == null) {
                adapter = SongAdapter()
            }
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    /**
     * Initialize doing some methods or actions here.
     *
     * @param savedInstanceState previous status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
        vm.getMusics(args.id)
        parent.showLoading()
    }
}

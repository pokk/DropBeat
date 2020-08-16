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

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.presentation.activity.MainActivity
import taiwan.no.one.feat.ranking.databinding.FragmentRankingBinding
import taiwan.no.one.feat.ranking.presentation.recyclerviews.adapters.RankAdapter
import taiwan.no.one.feat.ranking.presentation.viewmodels.RankViewModel
import taiwan.no.one.ktx.livedata.obs

class RankingFragment : BaseFragment<MainActivity, FragmentRankingBinding>() {
    private val vm by viewModels<RankViewModel>()

//    init {
//        lifecycleScope.launchWhenCreated {
//            parent.showLoading()
//        }
//    }

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.rankings.obs(this) { res ->
            res.onSuccess {
                (binding.rvMusics.adapter as? RankAdapter)?.data = it
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
        binding.rvMusics.apply {
            if (adapter == null) {
                adapter = RankAdapter()
            }
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        (binding.rvMusics.adapter as? RankAdapter)?.setOnClickListener {

//            findNavController().navigate(IndexFragmentDirections.actionIndexFragmentToDetailFragment(it.toString()))
        }
    }
}

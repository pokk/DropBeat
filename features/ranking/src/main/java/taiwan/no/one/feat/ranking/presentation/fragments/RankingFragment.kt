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

package taiwan.no.one.feat.ranking.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.logw
import java.lang.ref.WeakReference
import org.kodein.di.provider
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.di.Constant as DiConstant
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.dropbeat.presentation.activities.MainActivity
import taiwan.no.one.feat.ranking.databinding.FragmentRankingBinding
import taiwan.no.one.feat.ranking.presentation.recyclerviews.adapters.RankAdapter
import taiwan.no.one.feat.ranking.presentation.viewmodels.RankViewModel

class RankingFragment : BaseFragment<MainActivity, FragmentRankingBinding>() {
    var navigationCallback: ((title: String, rankId: Int) -> Unit)? = null
    private val vm by viewModels<RankViewModel>()
    private val rankAdapter by lazy(::RankAdapter)
    private val linearLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()), RecyclerView.HORIZONTAL)
    }
    private val noneEdgeEffectFactory by provider<RecyclerView.EdgeEffectFactory>(DiConstant.TAG_EDGE_FACTORY_NONE)

    override fun onDestroyView() {
        binding.rvMusics.adapter = null
        super.onDestroyView()
    }

    override fun viewComponentBinding() {
        binding.rvMusics.apply {
            adapter = rankAdapter
            layoutManager = linearLayoutManager()
            edgeEffectFactory = noneEdgeEffectFactory()
        }
    }

    override fun componentListenersBinding() {
        rankAdapter.setOnClickListener { id, title ->
            navigationCallback?.invoke(title, id)
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        vm.rankings.observe(viewLifecycleOwner) { res ->
            res.onSuccess {
                rankAdapter.submitList(it)
                binding.pbProgress.gone()
            }.onFailure(::logw)
        }
    }
}

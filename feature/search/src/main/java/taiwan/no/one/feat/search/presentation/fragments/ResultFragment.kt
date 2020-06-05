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

package taiwan.no.one.feat.search.presentation.fragments

import android.os.Bundle
import android.view.ViewStub
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.search.R
import taiwan.no.one.feat.search.databinding.FragmentSearchResultBinding
import taiwan.no.one.feat.search.databinding.StubSearchHasResultBinding
import taiwan.no.one.feat.search.databinding.StubSearchNoResultBinding
import taiwan.no.one.feat.search.presentation.recyclerviews.adapters.ResultAdapter
import taiwan.no.one.feat.search.presentation.viewmodels.ResultViewModel
import taiwan.no.one.ktx.view.findOptional
import androidx.lifecycle.observe as obs

internal class ResultFragment : BaseFragment<BaseActivity<*>, FragmentSearchResultBinding>() {
    private var stubHasResultBinding: StubSearchHasResultBinding? = null
    private val vm by viewModels<ResultViewModel> { vmFactory }
    private val args by navArgs<ResultFragmentArgs>()

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.musics.obs(this) { res ->
            res.onSuccess {
                if (it.isEmpty()) {
                    // TODO(jieyi): 6/1/20 it should be => binding.vsNoResult.viewStub?.inflate()
                    findOptional<ViewStub>(R.id.vs_no_result)?.inflate()
                }
                else {
                    // TODO(jieyi): 6/1/20 it should be => binding.vsHasResult.viewStub?.inflate()
                    findOptional<ViewStub>(R.id.vs_has_result)?.inflate()
                    (stubHasResultBinding?.rvMusics?.adapter as? ResultAdapter)?.addExtraEntities(it)
                }
            }
        }
        vm.addOrUpdateResult.obs(this) {
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        binding.vsHasResult.setOnInflateListener { _, inflated ->
            stubHasResultBinding = StubSearchHasResultBinding.bind(inflated).apply {
                rvMusics.apply {
                    if (adapter == null) {
                        adapter = ResultAdapter()
                    }
                    if (layoutManager == null) {
                        layoutManager = LinearLayoutManager(requireActivity())
                    }
                }
            }
        }
        binding.vsNoResult.setOnInflateListener { _, inflated ->
            StubSearchNoResultBinding.bind(inflated).apply {
                btnSearch.setOnClickListener {
                    findNavController().navigate(ResultFragmentDirections.actionResultToRecent(true))
                }
            }
        }
    }

    /**
     * Initialize doing some methods or actions here.
     *
     * @param savedInstanceState previous status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
        vm.search(args.keyword)
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after [.onStop] and before [.onDetach].
     */
    override fun onDestroy() {
        super.onDestroy()
        stubHasResultBinding = null
    }
}

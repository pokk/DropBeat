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

import android.view.KeyEvent
import android.widget.EditText
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.kotlinknifer.hideSoftKeyboard
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.devrapid.kotlinknifer.recyclerview.itemdecorator.VerticalItemDecorator
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.search.R
import taiwan.no.one.feat.search.databinding.FragmentSearchIndexBinding
import taiwan.no.one.feat.search.databinding.MergeSearchHasResultBinding
import taiwan.no.one.feat.search.presentation.recyclerviews.adapters.HistoryAdapter
import taiwan.no.one.feat.search.presentation.recyclerviews.adapters.ResultAdapter
import taiwan.no.one.feat.search.presentation.viewmodels.RecentViewModel
import taiwan.no.one.feat.search.presentation.viewmodels.ResultViewModel
import taiwan.no.one.ktx.recyclerview.contains
import taiwan.no.one.ktx.view.afterTextChanges
import taiwan.no.one.widget.WidgetResDimen

internal class IndexFragment : BaseFragment<BaseActivity<*>, FragmentSearchIndexBinding>() {
    private val mergeBinding by lazy { MergeSearchHasResultBinding.bind(binding.root) }
    private val vm by viewModels<RecentViewModel>()
    private val searchVm by viewModels<ResultViewModel>()
    private val searchHistoryAdapter by lazy { HistoryAdapter() }
    private val musicAdapter by lazy { ResultAdapter() }
    private val musicItemDecoration by lazy {
        VerticalItemDecorator(resources.getDimension(WidgetResDimen.md_three_unit).toInt(), 0)
    }
    private val rvMusics get() = mergeBinding.rvMusics

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.histories.observe(this) {
            logw(it)
            if (it.isEmpty()) {
                // TODO(Jieyi): 8/5/20 The action needs to be confirmed again.
            }
            else {
                searchHistoryAdapter.data = it
                rvMusics.smoothScrollToPosition(0)
                // Remove the item decoration
                if (musicItemDecoration !in rvMusics) return@observe
                rvMusics.removeItemDecoration(musicItemDecoration)
            }
        }
        searchVm.musics.observe(this) { res ->
            res.onSuccess {
                if (it.isEmpty()) {
                    loge("result is empty")
                }
                else {
                    musicAdapter.addExtraEntities(it)
                    displaySearchResultDataList()
                    // Add the item decoration
                    if (musicItemDecoration in rvMusics) return@onSuccess
                    rvMusics.addItemDecoration(musicItemDecoration)
                }
            }.onFailure {
                loge(it)
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        super.viewComponentBinding()
        mergeBinding.mtvRvTitle.doOnPreDraw {
            val halfWidth = it.width / 2
            // anchor 3 is top margin, it didn't define inside setMargin
            binding.layoutParent.getConstraintSet(R.id.expanded)?.setMargin(R.id.mtv_rv_title, 3, halfWidth)
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        rvMusics.apply {
            if (adapter == null) {
                adapter = searchHistoryAdapter
            }
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
        searchHistoryAdapter.setOnClickListener(::clickedOnHistoryItem)
        binding.apply {
            // Click the search icon
            tilSearchBar.setEndIconOnClickListener {
                searchMusic(tietSearch.text.toString())
            }
            // Hit the enter key on the soft keyword or the physical keyboard
            tietSearch.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchMusic((v as EditText).text.toString())
                    return@setOnKeyListener true
                }
                false
            }
            tietSearch.afterTextChanges().debounce(300).onEach {
                if (it.isNullOrBlank()) {
                    displayHistoryDataList()
                }
            }.launchIn(lifecycleScope)
        }
    }

    private fun searchMusic(keyword: String) {
        vm.add(keyword)
        searchVm.search(keyword)
        // post action for hiding the soft keyboard.
        view?.hideSoftKeyboard()
    }

    private fun clickedOnHistoryItem(keyword: String) {
        binding.tietSearch.setText(keyword)
        searchMusic(keyword)
    }

    private fun displayHistoryDataList() {
        rvMusics.adapter = searchHistoryAdapter
        mergeBinding.mtvRvTitle.text = "History Search "
    }

    private fun displaySearchResultDataList() {
        rvMusics.adapter = musicAdapter
        mergeBinding.mtvRvTitle.text = "Search Result "
    }
}

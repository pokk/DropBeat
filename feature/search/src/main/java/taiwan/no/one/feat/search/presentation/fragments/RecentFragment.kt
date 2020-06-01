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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.toggleSoftKeyboard
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.search.databinding.FragmentSearchRecentBinding
import taiwan.no.one.feat.search.databinding.MergeTabSearchBinding
import taiwan.no.one.feat.search.presentation.recyclerviews.adapters.HistoryAdapter
import taiwan.no.one.feat.search.presentation.viewmodels.RecentViewModel
import taiwan.no.one.widget.components.recyclerviews.helpers.AdapterItemTouchHelper
import taiwan.no.one.widget.components.recyclerviews.helpers.DefaultItemTouchCallback
import androidx.lifecycle.observe as obs

class RecentFragment : BaseFragment<BaseActivity<*>, FragmentSearchRecentBinding>() {
    private val mergeBinding by lazy { MergeTabSearchBinding.bind(binding.root) }
    private val vm by viewModels<RecentViewModel> { vmFactory }
    private var selectedKeyword: String? = null

    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        vm.histories.obs(this) {
            (binding.rvHistories.adapter as? HistoryAdapter)?.data = it
            binding.rvHistories.smoothScrollToPosition(0)
        }
        vm.addOrUpdateResult.obs(this) {
            it.onSuccess {
                findNavController().navigate(RecentFragmentDirections.actionRecentToResult(selectedKeyword.orEmpty()))
            }
        }
        vm.deleteResult.obs(this) {
            it.onSuccess {}
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all view components here.
     */
    override fun viewComponentBinding() {
        mergeBinding.mtvTitle.gone()
        binding.rvHistories.apply {
            if (adapter == null) {
                adapter = HistoryAdapter()
            }
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(requireActivity())
            }
            ItemTouchHelper(DefaultItemTouchCallback(0,
                                                     ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                                                     adapter as AdapterItemTouchHelper)).also {
                it.attachToRecyclerView(this)
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
        (binding.rvHistories.adapter as? HistoryAdapter)?.apply {
            setOnClickListener {
                selectedKeyword = it
                searchMusic()
            }
            setOnSwipeListener { entity, _ ->
                vm.delete(null, entity)
            }
        }
        mergeBinding.tilSearchBar.setEndIconOnClickListener {
            selectedKeyword = mergeBinding.tietSearch.text.toString()
            vm.add(mergeBinding.tietSearch.text.toString())
        }
        mergeBinding.tietSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                requireContext().toggleSoftKeyboard()
                selectedKeyword = (v as EditText).text.toString()
                searchMusic()
                true
            }
            false
        }
    }

    private fun searchMusic() {
        vm.add(selectedKeyword.orEmpty())
    }
}

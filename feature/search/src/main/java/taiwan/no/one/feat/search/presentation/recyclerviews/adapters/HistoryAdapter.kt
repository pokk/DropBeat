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

package taiwan.no.one.feat.search.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.feat.search.R
import taiwan.no.one.feat.search.data.entities.local.SearchHistoryEntity
import taiwan.no.one.feat.search.databinding.ItemRencentSearchBinding
import taiwan.no.one.feat.search.presentation.recyclerviews.viewholders.HistoryViewHolder
import taiwan.no.one.widget.recyclerviews.AutoUpdatable
import taiwan.no.one.widget.recyclerviews.helpers.AdapterItemTouchHelper
import kotlin.properties.Delegates

internal class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder>(),
                                AutoUpdatable,
                                AdapterItemTouchHelper {
    private var onSwipeListener: ((entity: SearchHistoryEntity, direction: Int) -> Unit)? = null
    var data: List<SearchHistoryEntity> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(oldValue, newValue) { o, n -> o.id == n.id }
    }
    var onClickListener: ((keyword: String) -> Unit)? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_rencent_search, parent, false)
        .let { HistoryViewHolder(ItemRencentSearchBinding.bind(it)) }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.initView(data[position], this)
    }

    override fun onItemSwiped(position: Int, direction: Int) {
        onSwipeListener?.invoke(data[position], direction)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) = UnsupportedOperation()

    fun setOnClickListener(listener: (keyword: String) -> Unit) {
        onClickListener = listener
    }

    fun setOnSwipeListener(listener: (entity: SearchHistoryEntity, direction: Int) -> Unit) {
        onSwipeListener = listener
    }
}

/*
 * MIT License
 *
 * Copyright (c) 2022 Jieyi
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
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.devrapid.kotlinknifer.getDimen
import taiwan.no.one.feat.search.R
import taiwan.no.one.feat.search.data.entities.remote.NetworkCommonMusic.NetworkSong
import taiwan.no.one.feat.search.databinding.ItemSearchResultBinding
import taiwan.no.one.feat.search.presentation.recyclerviews.viewholders.ResultViewHolder
import taiwan.no.one.widget.WidgetResDimen

internal class ResultAdapter : ListAdapter<NetworkSong, ResultViewHolder>(DiffItemCallback) {
    var onClickListener: ((NetworkSong) -> Unit)? = null
        private set

    private object DiffItemCallback : DiffUtil.ItemCallback<NetworkSong>() {
        override fun areItemsTheSame(oldItem: NetworkSong, newItem: NetworkSong) = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: NetworkSong, newItem: NetworkSong) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_search_result, parent, false)
        .let { ResultViewHolder(ItemSearchResultBinding.bind(it)) }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        // Adjust the margin
        holder.itemView.updateLayoutParams<MarginLayoutParams> {
            topMargin = holder.itemView
                .context
                .applicationContext
                .getDimen(if (position == 0) WidgetResDimen.md_two_unit else WidgetResDimen.md_two_half_unit)
                .toInt()
        }
        holder.initView(getItem(position), this)
    }

    fun setOnClickListener(block: (songEntity: NetworkSong) -> Unit) {
        onClickListener = block
    }

    fun clear() {
        submitList(emptyList())
    }
}

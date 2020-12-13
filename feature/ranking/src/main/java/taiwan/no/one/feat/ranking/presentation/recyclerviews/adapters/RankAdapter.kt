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

package taiwan.no.one.feat.ranking.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import taiwan.no.one.dropbeat.AppResLayout
import taiwan.no.one.dropbeat.databinding.ItemTrendBinding
import taiwan.no.one.feat.ranking.data.entities.remote.MusicRankListEntity.BriefRankEntity
import taiwan.no.one.feat.ranking.presentation.recyclerviews.viewholders.RankViewHolder

internal class RankAdapter : ListAdapter<BriefRankEntity, RankViewHolder>(DiffItemCallback) {
    var onClickListener: ((rankId: Int) -> Unit)? = null
        private set

    private object DiffItemCallback : DiffUtil.ItemCallback<BriefRankEntity>() {
        override fun areItemsTheSame(oldItem: BriefRankEntity, newItem: BriefRankEntity) =
            oldItem.rankId == newItem.rankId

        override fun areContentsTheSame(oldItem: BriefRankEntity, newItem: BriefRankEntity) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater.from(parent.context)
        .inflate(AppResLayout.item_trend, parent, false)
        .let { RankViewHolder(ItemTrendBinding.bind(it)) }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.initView(getItem(position), this)
    }

    fun setOnClickListener(listener: (rankId: Int) -> Unit) {
        onClickListener = listener
    }
}

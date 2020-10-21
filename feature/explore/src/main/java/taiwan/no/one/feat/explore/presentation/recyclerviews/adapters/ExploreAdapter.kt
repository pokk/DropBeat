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

package taiwan.no.one.feat.explore.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import taiwan.no.one.feat.explore.R
import taiwan.no.one.feat.explore.data.entities.remote.TagInfoEntity.TagEntity
import taiwan.no.one.feat.explore.databinding.ItemExploreBinding
import taiwan.no.one.feat.explore.presentation.recyclerviews.viewholders.ExploreViewHolder
import taiwan.no.one.widget.recyclerviews.AutoUpdatable
import kotlin.properties.Delegates

internal class ExploreAdapter(
    private val itemList: List<TagEntity>,
) : RecyclerView.Adapter<ExploreViewHolder>(), AutoUpdatable {
    var data by Delegates.observable(emptyList<TagEntity>()) { _, oldValue, newValue ->
        autoNotify(oldValue, newValue) { o, n -> o.name == n.name }
    }
    var clickListener: ((TagEntity) -> Unit)? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_explore, parent, false)
        .let { ExploreViewHolder(ItemExploreBinding.bind(it)) }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) =
        holder.initView(itemList[position], this)

    override fun getItemCount() = itemList.size

    fun setOnClickListener(listener: (entity: TagEntity) -> Unit) {
        clickListener = listener
    }
}

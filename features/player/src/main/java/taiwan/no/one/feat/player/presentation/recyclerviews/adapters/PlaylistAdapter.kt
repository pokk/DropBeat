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

package taiwan.no.one.feat.player.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import taiwan.no.one.dropbeat.data.entities.SimplePlaylistEntity
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.databinding.ItemPlaylistBinding
import taiwan.no.one.feat.player.presentation.recyclerviews.viewholders.PlaylistViewHolder

internal class PlaylistAdapter : ListAdapter<SimplePlaylistEntity, PlaylistViewHolder>(DiffItemCallback) {
    var onClickListener: ((entity: SimplePlaylistEntity) -> Unit)? = null
        private set

    private object DiffItemCallback : DiffUtil.ItemCallback<SimplePlaylistEntity>() {
        override fun areItemsTheSame(oldItem: SimplePlaylistEntity, newItem: SimplePlaylistEntity) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: SimplePlaylistEntity, newItem: SimplePlaylistEntity) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
            .let { PlaylistViewHolder(ItemPlaylistBinding.bind(it)) }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) =
        holder.initView(getItem(position), this)

    fun setOnClickListener(listener: (entity: SimplePlaylistEntity) -> Unit) {
        onClickListener = listener
    }
}

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

package taiwan.no.one.feat.player.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.databinding.ItemLyricBinding
import taiwan.no.one.feat.player.presentation.recyclerviews.viewholders.LyricViewHolder
import taiwan.no.one.mediaplayer.lyric.LrcRowEntity
import taiwan.no.one.mediaplayer.lyric.LyricListener

internal class LyricAdapter(
    private val lyrics: List<LrcRowEntity>,
) : RecyclerView.Adapter<LyricViewHolder>(), LyricListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(R.layout.item_lyric, parent, false)
            .let { LyricViewHolder(ItemLyricBinding.bind(it)) }

    override fun onBindViewHolder(holder: LyricViewHolder, position: Int) = holder.initView(lyrics[position], this)

    override fun getItemCount() = lyrics.size

    override fun seekLrcToTime(entity: LrcRowEntity) = Unit

    override fun setLrcSoughtTimeListener(listener: (pos: Int, lrcEntity: LrcRowEntity) -> Unit) = Unit
}

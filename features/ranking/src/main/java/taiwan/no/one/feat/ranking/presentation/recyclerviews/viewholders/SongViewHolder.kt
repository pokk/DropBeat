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

package taiwan.no.one.feat.ranking.presentation.recyclerviews.viewholders

import coil.load
import taiwan.no.one.feat.ranking.data.entities.remote.CommonMusicEntity.NetworkSong
import taiwan.no.one.feat.ranking.databinding.ItemRankSongBinding
import taiwan.no.one.feat.ranking.presentation.recyclerviews.adapters.SongAdapter
import taiwan.no.one.widget.recyclerviews.ViewHolderBinding

internal class SongViewHolder(
    private val binding: ItemRankSongBinding,
) : ViewHolderBinding<NetworkSong, SongAdapter>(binding.root) {
    override fun initView(entity: NetworkSong, adapter: SongAdapter) {
        binding.apply {
            mtvNumber.text = "#${absoluteAdapterPosition + 1}"
            mtvAlbumName.text = entity.title
            mtvArtistName.text = entity.artist
            sivAlbumThumb.load(entity.cdnCoverUrl)
        }
        binding.clItem.setOnClickListener {}
    }
}

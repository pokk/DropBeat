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

package taiwan.no.one.feat.search.presentation.recyclerviews.viewholders

import coil.api.loadAny
import taiwan.no.one.core.presentation.recyclerviews.ViewHolderBinding
import taiwan.no.one.feat.search.data.entities.remote.CommonMusicEntity.SongEntity
import taiwan.no.one.feat.search.databinding.ItemSearchResultBinding
import taiwan.no.one.feat.search.presentation.recyclerviews.adapters.ResultAdapter

internal class ResultViewHolder(
    private val binding: ItemSearchResultBinding
) : ViewHolderBinding<SongEntity, ResultAdapter>(binding.root) {
    override fun initView(entity: SongEntity, position: Int, adapter: ResultAdapter) {
        binding.apply {
            mtvNumber.text = "#${position + 1}"
            mtvAlbumName.text = entity.title
            mtvArtistName.text = entity.artist
            sivAlbumThumb.loadAny(entity.cdnCoverUrl)
        }
        binding.clItem.setOnClickListener {}
    }
}

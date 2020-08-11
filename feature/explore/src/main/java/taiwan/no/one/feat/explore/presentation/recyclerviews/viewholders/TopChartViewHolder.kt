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

package taiwan.no.one.feat.explore.presentation.recyclerviews.viewholders

import coil.api.loadAny
import taiwan.no.one.dropbeat.databinding.ItemTypeOfMusicBinding
import taiwan.no.one.feat.explore.data.entities.remote.ArtistInfoEntity.ArtistEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.TopChartAdapter
import taiwan.no.one.widget.recyclerviews.ViewHolderBinding

internal class TopChartViewHolder(
    private val binding: ItemTypeOfMusicBinding
) : ViewHolderBinding<Any, TopChartAdapter>(binding.root) {
    override fun initView(entity: Any, position: Int, adapter: TopChartAdapter) {
        when (entity) {
            is TrackEntity -> initTrackType(entity)
            is ArtistEntity -> initArtistType(entity)
        }
        binding.apply {
            mtvNumber.text = (position + 1).toString()
        }
    }

    private fun initTrackType(entity: TrackEntity) {
        binding.apply {
            mtvArtistName.text = entity.artist?.name.orEmpty()
            mtvAlbumName.text = entity.album?.name.orEmpty()
            sivAlbumThumb.loadAny(entity.images?.get(0)?.text.orEmpty())
        }
    }

    private fun initArtistType(entity: ArtistEntity) {
        binding.apply {
            mtvArtistName.text = entity.name.orEmpty()
            sivAlbumThumb.loadAny(entity.images?.get(0)?.text.orEmpty())
        }
    }
}

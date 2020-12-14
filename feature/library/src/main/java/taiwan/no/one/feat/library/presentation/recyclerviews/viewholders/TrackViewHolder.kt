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

package taiwan.no.one.feat.library.presentation.recyclerviews.viewholders

import androidx.core.content.ContextCompat
import coil.loadAny
import taiwan.no.one.dropbeat.AppResDrawable
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.databinding.ItemTypeOfMusicBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.adapters.TrackAdapter
import taiwan.no.one.widget.recyclerviews.ViewHolderBinding

internal class TrackViewHolder(
    private val binding: ItemTypeOfMusicBinding,
) : ViewHolderBinding<SimpleTrackEntity, TrackAdapter>(binding.root) {
    override fun initView(entity: SimpleTrackEntity, adapter: TrackAdapter) {
        binding.apply {
            mtvNumber.text = "#${absoluteAdapterPosition + 1}"
            sivAlbumThumb.loadAny(entity.thumbUri)
            mtvAlbumName.text = entity.name
            mtvArtistName.text = entity.artist
            setFavoriteIcon(entity.isFavorite)
            btnOption.setOnClickListener { adapter.optionListener?.invoke(entity) }
            btnFavorite.setOnClickListener {
                entity.isFavorite = !entity.isFavorite
                setFavoriteIcon(entity.isFavorite)
                adapter.favoriteListener?.invoke(entity)
            }
            root.setOnClickListener { adapter.clickListener?.invoke(entity) }
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        binding.btnFavorite.icon = ContextCompat.getDrawable(
            context,
            if (isFavorite) AppResDrawable.ic_heart_solid else AppResDrawable.ic_heart
        )
    }
}

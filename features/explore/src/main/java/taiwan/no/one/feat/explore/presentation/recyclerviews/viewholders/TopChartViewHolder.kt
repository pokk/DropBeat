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

package taiwan.no.one.feat.explore.presentation.recyclerviews.viewholders

import androidx.core.content.ContextCompat
import coil.load
import com.devrapid.kotlinshaver.castOrNull
import taiwan.no.one.dropbeat.AppResDrawable
import taiwan.no.one.dropbeat.databinding.ItemTypeOfMusicBinding
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo.NetworkTrack
import taiwan.no.one.feat.explore.data.mappers.EntityMapper
import taiwan.no.one.feat.explore.domain.usecases.ArtistWithMoreDetailEntity
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.TopChartAdapter
import taiwan.no.one.widget.recyclerviews.ViewHolderBinding

internal class TopChartViewHolder(
    private val binding: ItemTypeOfMusicBinding,
) : ViewHolderBinding<Any, TopChartAdapter>(binding.root) {
    override fun initView(entity: Any, adapter: TopChartAdapter) {
        val track = castOrNull<NetworkTrack>(entity)
        val artist = castOrNull<ArtistWithMoreDetailEntity>(entity)
        if (track != null || artist != null) {
            binding.mtvNumber.text = (absoluteAdapterPosition + 1).toString()
        }
        // XXX(jieyi): 10/31/20 We might be able to do better.
        if (track != null) {
            initTrackType(track)
            binding.apply {
                btnFavorite.setOnClickListener {
                    track.isFavorite = !(track.isFavorite ?: false)
                    setFavoriteIcon(requireNotNull(track.isFavorite))
                    adapter.favoriteListener?.invoke(EntityMapper.exploreToSimpleTrackEntity(track))
                }
                btnOption.setOnClickListener {
                    adapter.optionListener?.invoke(it, EntityMapper.exploreToSimpleTrackEntity(track))
                }
            }
        }
        else if (artist != null) {
            initArtistType(artist)
            binding.apply {
                btnFavorite.setOnClickListener {
                    artist.second?.popularTrackThisWeek?.isFavorite =
                        !(artist.second?.popularTrackThisWeek?.isFavorite ?: false)
                    setFavoriteIcon(requireNotNull(artist.second?.popularTrackThisWeek?.isFavorite))
                    adapter.favoriteListener?.invoke(EntityMapper.artistToSimpleTrackEntity(artist))
                }
                btnOption.setOnClickListener {
                    adapter.optionListener?.invoke(it, EntityMapper.artistToSimpleTrackEntity(artist))
                }
            }
        }
    }

    private fun initTrackType(entity: NetworkTrack) {
        binding.apply {
            mtvArtistName.text = entity.artist?.name.orEmpty()
            mtvAlbumName.text = entity.name.orEmpty()
            entity.isFavorite?.let(this@TopChartViewHolder::setFavoriteIcon)
            sivAlbumThumb.load(
                (entity.images?.find { it.size == "cover" }?.text ?: entity.images?.get(0)?.text).orEmpty()
            )
        }
    }

    private fun initArtistType(entity: ArtistWithMoreDetailEntity) {
        binding.apply {
            mtvArtistName.text = entity.first.name.orEmpty()
            entity.second?.apply {
                popularTrackThisWeek.isFavorite?.let(this@TopChartViewHolder::setFavoriteIcon)
                mtvAlbumName.text = popularTrackThisWeek.name.orEmpty()
                sivAlbumThumb.load(coverPhotoUrl)
            }
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        binding.btnFavorite.icon = ContextCompat.getDrawable(
            context,
            if (isFavorite) AppResDrawable.ic_heart_solid else AppResDrawable.ic_heart
        )
    }
}

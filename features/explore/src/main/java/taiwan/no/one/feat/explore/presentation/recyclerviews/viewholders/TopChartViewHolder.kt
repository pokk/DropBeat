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
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity
import taiwan.no.one.feat.explore.data.mappers.EntityMapper
import taiwan.no.one.feat.explore.domain.usecases.ArtistWithMoreDetailEntity
import taiwan.no.one.feat.explore.presentation.recyclerviews.adapters.TopChartAdapter
import taiwan.no.one.widget.recyclerviews.ViewHolderBinding

internal class TopChartViewHolder(
    private val binding: ItemTypeOfMusicBinding,
) : ViewHolderBinding<Any, TopChartAdapter>(binding.root) {
    override fun initView(entity: Any, adapter: TopChartAdapter) {
        castOrNull<TrackEntity>(entity)?.let(::initTrackType)
        castOrNull<ArtistWithMoreDetailEntity>(entity)?.let(::initArtistType)
        binding.apply {
            mtvNumber.text = (absoluteAdapterPosition + 1).toString()
            // XXX(jieyi): 10/31/20 We might be able to do better.
            btnFavorite.setOnClickListener {
                castOrNull<TrackEntity>(entity)?.let { trackEntity ->
                    trackEntity.isFavorite = !(trackEntity.isFavorite ?: false)
                    setFavoriteIcon(requireNotNull(trackEntity.isFavorite))
                    adapter.favoriteListener?.invoke(EntityMapper.exploreToSimpleTrackEntity(trackEntity))
                }
                castOrNull<ArtistWithMoreDetailEntity>(entity)?.let { artistWithMoreDetailEntity ->
                    artistWithMoreDetailEntity.second?.popularTrackThisWeek?.isFavorite =
                        !(artistWithMoreDetailEntity.second?.popularTrackThisWeek?.isFavorite ?: false)
                    setFavoriteIcon(requireNotNull(artistWithMoreDetailEntity.second?.popularTrackThisWeek?.isFavorite))
                    adapter.favoriteListener?.invoke(EntityMapper.artistToSimpleTrackEntity(artistWithMoreDetailEntity))
                }
            }
            btnOption.setOnClickListener {
                castOrNull<TrackEntity>(entity)?.let { trackEntity ->
                    adapter.optionListener?.invoke(it, EntityMapper.exploreToSimpleTrackEntity(trackEntity))
                }
                castOrNull<ArtistWithMoreDetailEntity>(entity)?.let { artistWithMoreDetailEntity ->
                    adapter.optionListener?.invoke(
                        it,
                        EntityMapper.artistToSimpleTrackEntity(artistWithMoreDetailEntity)
                    )
                }
            }
        }
    }

    private fun initTrackType(entity: TrackEntity) {
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

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

package taiwan.no.one.feat.explore.data.remote.services.retrofit.v1

import org.jsoup.Jsoup
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistMoreDetailEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistPhotosEntity
import taiwan.no.one.feat.explore.data.entities.remote.CommonLastFmEntity.ImageEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity

internal class LastFmExtraImpl : LastFmExtraService {
    companion object {
        private const val LASTFM_DOMAIN_URL = "https://www.last.fm"
        private const val LASTFM_DOMAIN_MUSIC = "$LASTFM_DOMAIN_URL/music/"
        private const val LASTFM_IMAGE_REQUEST = "/+images?"

        private const val LASTFM_IMAGE_URL = "https://lastfm-img2.akamaized.net/i/u/770x0/%s.jpg"
    }

    override suspend fun retrieveArtistPhotosInfo(artistName: String, page: Int): ArtistPhotosEntity {
        val doc = Jsoup.connect("$LASTFM_DOMAIN_MUSIC$artistName$LASTFM_IMAGE_REQUEST?page=$page").get()

        val hasNext = doc.select("li.pagination-next").select("a").toString().isNotBlank()
        val photos = doc.select("ul.image-list").select("li")
            .map { it.select("img.image-list-image").attr("src") }
            .map { it.split("/").last() }
            .map { ArtistPhotosEntity.ArtistPhotoEntity(LASTFM_IMAGE_URL.format(it), it) }

        return ArtistPhotosEntity(photos, hasNext)
    }

    override suspend fun retrieveArtistMoreDetail(artistName: String): ArtistMoreDetailEntity {
        val doc = Jsoup.connect("$LASTFM_DOMAIN_MUSIC$artistName").get()
        val trackEntity = TrackEntity(null)

        val photo = doc.select("div.header-new-background-image").attr("content")
        doc.select("h3.artist-header-featured-items-item-name")[1].apply {
            trackEntity.name = text()
            trackEntity.url = "${LASTFM_DOMAIN_URL}${select("a").attr("href")}"
        }
        trackEntity.listeners = doc.select("p.artist-header-featured-items-item-listeners").text()

        return ArtistMoreDetailEntity(photo, trackEntity)
    }

    override suspend fun retrieveTrackCover(url: String, trackEntity: TrackEntity): TrackEntity {
        val photoUrl = retrieveCoverThumbnail(url)

        val images = trackEntity.images.orEmpty().toMutableList().apply {
            add(ImageEntity(photoUrl, "cover"))
        }
        trackEntity.images = images
        return trackEntity
    }

    override suspend fun retrieveTrackCover(url: String, simpleTrackEntity: SimpleTrackEntity): SimpleTrackEntity {
        val photoUrl = retrieveCoverThumbnail(url)
        return simpleTrackEntity.copy(thumbUri = photoUrl)
    }

    private fun retrieveCoverThumbnail(url: String) =
        Jsoup.connect(url).get().select("div.header-new-background-image").attr("content")
}

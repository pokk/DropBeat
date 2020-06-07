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

package taiwan.no.one.feat.explore.data.repositories

import taiwan.no.one.feat.explore.data.stores.LocalStore
import taiwan.no.one.feat.explore.data.stores.RemoteStore
import taiwan.no.one.feat.explore.domain.repositories.LastFmRepo

internal class LastFmRepository(
    private val local: LocalStore,
    private val remote: RemoteStore
) : LastFmRepo {
    override suspend fun fetchAlbum() = remote.getAlbumInfo().album ?: throw Exception()

    override suspend fun fetchArtist() = remote.getArtistInfo().artist ?: throw Exception()

    override suspend fun fetchArtistTopAlbum() = remote.getArtistTopAlbum().topAlbums

    override suspend fun fetchArtistTopTrack() = remote.getArtistTopTrack().topTracks

    override suspend fun fetchSimilarArtistInfo() = remote.getSimilarArtistInfo().similarArtist

    override suspend fun fetchArtistPhotoInfo() = remote.getArtistPhotosInfo().photos

    override suspend fun fetchTrack() = remote.getTrackInfo().track ?: throw Exception()

    override suspend fun fetchSimilarTrackInfo() = remote.getSimilarTrackInfo().similarTracks

    override suspend fun fetchChartTopTrack() = remote.getChartTopTrack().track

    override suspend fun fetchChartTopArtist() = remote.getChartTopArtist().artists

    override suspend fun fetchChartTopTag() = remote.getChartTopTag().tag

    override suspend fun fetchTag() = remote.getTagInfo().tag

    override suspend fun fetchTagTopAlbum() = remote.getTagTopAlbum().albums

    override suspend fun fetchTagTopArtist() = remote.getTagTopArtist().topArtists

    override suspend fun fetchTagTopTrack() = remote.getTagTopTrack().track
}

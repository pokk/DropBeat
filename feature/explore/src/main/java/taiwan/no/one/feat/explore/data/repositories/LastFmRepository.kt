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

import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.domain.repositories.LastFmRepo

internal class LastFmRepository(
    private val local: DataStore,
    private val remote: DataStore
) : LastFmRepo {
    override suspend fun fetchAlbum(mbid: String) = remote.getAlbumInfo(mbid).album ?: throw Exception()

    override suspend fun fetchArtist(mbid: String) = remote.getArtistInfo(mbid).artist ?: throw Exception()

    override suspend fun fetchArtistTopAlbum(mbid: String) = remote.getArtistTopAlbum(mbid).topAlbums

    override suspend fun fetchArtistTopTrack(mbid: String) = remote.getArtistTopTrack(mbid).topTracks

    override suspend fun fetchSimilarArtistInfo(mbid: String) = remote.getSimilarArtistInfo(mbid).similarArtist

    override suspend fun fetchTrack(mbid: String) = remote.getTrackInfo(mbid).track ?: throw Exception()

    override suspend fun fetchSimilarTrackInfo(mbid: String) = remote.getSimilarTrackInfo(mbid).similarTracks

    override suspend fun fetchChartTopTrack(page: Int, limit: Int) = remote.getChartTopTrack(page, limit).track

    override suspend fun fetchChartTopArtist(page: Int, limit: Int) = remote.getChartTopArtist(page, limit).artists

    override suspend fun fetchChartTopTag(page: Int, limit: Int) = remote.getChartTopTag(page, limit).tag

    override suspend fun fetchTag(mbid: String) = remote.getTagInfo(mbid).tag

    override suspend fun fetchTagTopAlbum(mbid: String) = remote.getTagTopAlbum(mbid).albums

    override suspend fun fetchTagTopArtist(mbid: String) = remote.getTagTopArtist(mbid).topArtists

    override suspend fun fetchTagTopTrack(mbid: String) = remote.getTagTopTrack(mbid).track
}

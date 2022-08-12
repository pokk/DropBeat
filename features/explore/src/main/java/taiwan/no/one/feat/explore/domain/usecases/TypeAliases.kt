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

package taiwan.no.one.feat.explore.domain.usecases

import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.entity.SimpleArtistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.explore.data.entities.local.ArtistWithImageAndBioEntityAndStats
import taiwan.no.one.feat.explore.data.entities.remote.NetworkAlbumInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistMoreDetail
import taiwan.no.one.feat.explore.data.entities.remote.NetworkArtistPhotos.NetworkArtistPhoto
import taiwan.no.one.feat.explore.data.entities.remote.NetworkCommonLastFm
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTagInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopArtistInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTopTrackInfo
import taiwan.no.one.feat.explore.data.entities.remote.NetworkTrackInfo

internal typealias ArtistWithMoreDetailEntity = Pair<NetworkArtistInfo.NetworkArtist, NetworkArtistMoreDetail?>
internal typealias ArtistWithMoreDetailEntities = List<ArtistWithMoreDetailEntity>

internal typealias FetchAlbumCase = OneShotUsecase<NetworkAlbumInfo.NetworkAlbum, FetchAlbumReq>
internal typealias FetchAlbumReq = FetchAlbumOneShotCase.Request
internal typealias FetchArtistInfoCase = OneShotUsecase<ArtistWithImageAndBioEntityAndStats, FetchArtistReq>
internal typealias FetchArtistReq = FetchArtistInfoOneShotCase.Request
internal typealias FetchArtistPhotoCase = OneShotUsecase<List<NetworkArtistPhoto>, FetchArtistPhotoReq>
internal typealias FetchArtistPhotoReq = FetchArtistPhotoOneShotCase.Request
internal typealias FetchArtistTopAlbumCase = OneShotUsecase<NetworkCommonLastFm.NetworkTopAlbums, FetchArtistTopAlbumReq>
internal typealias FetchArtistTopAlbumReq = FetchArtistTopAlbumOneShotCase.Request
internal typealias FetchArtistTopTrackCase = OneShotUsecase<NetworkTopTrackInfo.NetworkTracks, FetchArtistTopTrackReq>
internal typealias FetchArtistTopTrackReq = FetchArtistTopTrackOneShotCase.Request
internal typealias FetchArtistCompleteInfoCase = OneShotUsecase<SimpleArtistEntity, FetchArtistCompleteInfoReq>
internal typealias FetchArtistCompleteInfoReq = FetchArtistCompleteInfoOneShotCase.Request
internal typealias FetchChartTopArtistCase = OneShotUsecase<ArtistWithMoreDetailEntities, FetchChartTopArtistReq>
internal typealias FetchChartTopArtistReq = FetchChartTopArtistOneShotCase.Request
internal typealias FetchChartTopTagCase = OneShotUsecase<NetworkCommonLastFm.NetworkTags, FetchChartTopTagReq>
internal typealias FetchChartTopTagReq = FetchChartTopTagOneShotCase.Request
internal typealias FetchChartTopTrackCase = OneShotUsecase<NetworkTopTrackInfo.NetworkTracks, FetchChartTopTrackReq>
internal typealias FetchChartTopTrackReq = FetchChartTopTrackOneShotCase.Request
internal typealias FetchSimilarArtistCase = OneShotUsecase<NetworkTopArtistInfo.NetworkArtists, FetchSimilarArtistReq>
internal typealias FetchSimilarArtistReq = FetchSimilarArtistOneShotCase.Request
internal typealias FetchSimilarTrackCase = OneShotUsecase<NetworkTopTrackInfo.NetworkTracks, FetchSimilarTrackReq>
internal typealias FetchSimilarTrackReq = FetchSimilarTrackOneShotCase.Request
internal typealias FetchTagCase = OneShotUsecase<NetworkTagInfo.NetworkTag, FetchTagReq>
internal typealias FetchTagReq = FetchTagOneShotCase.Request
internal typealias FetchTagTopAlbumCase = OneShotUsecase<NetworkCommonLastFm.NetworkTopAlbums, FetchTagTopAlbumReq>
internal typealias FetchTagTopAlbumReq = FetchTagTopAlbumOneShotCase.Request
internal typealias FetchTagTopArtistCase = OneShotUsecase<NetworkTopArtistInfo.NetworkArtists, FetchTagTopArtistReq>
internal typealias FetchTagTopArtistReq = FetchTagTopArtistOneShotCase.Request
internal typealias FetchTagTopTrackCase = OneShotUsecase<NetworkTopTrackInfo.NetworkTracks, FetchTagTopTrackReq>
internal typealias FetchTagTopTrackReq = FetchTagTopTrackOneShotCase.Request
internal typealias FetchTrackCase = OneShotUsecase<NetworkTrackInfo.NetworkTrack, FetchTrackReq>
internal typealias FetchTrackReq = FetchTrackOneShotCase.Request
internal typealias FetchTrackCoverCase = OneShotUsecase<SimpleTrackEntity, FetchTrackCoverReq>
internal typealias FetchTrackCoverReq = FetchTrackCoverOneShotCase.Request

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

package taiwan.no.one.feat.explore.domain.usecases

import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.feat.explore.data.entities.remote.AlbumInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistMoreDetailEntity
import taiwan.no.one.feat.explore.data.entities.remote.ArtistPhotosEntity.ArtistPhotoEntity
import taiwan.no.one.feat.explore.data.entities.remote.CommonLastFmEntity
import taiwan.no.one.feat.explore.data.entities.remote.TagInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopArtistInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TopTrackInfoEntity
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity

internal typealias ArtistWithMoreDetailEntity = Pair<ArtistInfoEntity.ArtistEntity, ArtistMoreDetailEntity?>
internal typealias ArtistWithMoreDetailEntities = List<ArtistWithMoreDetailEntity>

internal typealias FetchAlbumCase = OneShotUsecase<AlbumInfoEntity.AlbumEntity, FetchAlbumReq>
internal typealias FetchAlbumReq = FetchAlbumOneShotCase.Request
internal typealias FetchArtistInfoCase = OneShotUsecase<ArtistInfoEntity.ArtistEntity, FetchArtistReq>
internal typealias FetchArtistReq = FetchArtistInfoOneShotCase.Request
internal typealias FetchArtistPhotoCase = OneShotUsecase<List<ArtistPhotoEntity>, FetchArtistPhotoReq>
internal typealias FetchArtistPhotoReq = FetchArtistPhotoOneShotCase.Request
internal typealias FetchArtistTopAlbumCase = OneShotUsecase<CommonLastFmEntity.TopAlbumsEntity, FetchArtistTopAlbumReq>
internal typealias FetchArtistTopAlbumReq = FetchArtistTopAlbumOneShotCase.Request
internal typealias FetchArtistTopTrackCase = OneShotUsecase<TopTrackInfoEntity.TracksEntity, FetchArtistTopTrackReq>
internal typealias FetchArtistTopTrackReq = FetchArtistTopTrackOneShotCase.Request
internal typealias FetchChartTopArtistCase = OneShotUsecase<ArtistWithMoreDetailEntities, FetchChartTopArtistReq>
internal typealias FetchChartTopArtistReq = FetchChartTopArtistOneShotCase.Request
internal typealias FetchChartTopTagCase = OneShotUsecase<CommonLastFmEntity.TagsEntity, FetchChartTopTagReq>
internal typealias FetchChartTopTagReq = FetchChartTopTagOneShotCase.Request
internal typealias FetchChartTopTrackCase = OneShotUsecase<TopTrackInfoEntity.TracksEntity, FetchChartTopTrackReq>
internal typealias FetchChartTopTrackReq = FetchChartTopTrackOneShotCase.Request
internal typealias FetchSimilarArtistCase = OneShotUsecase<TopArtistInfoEntity.ArtistsEntity, FetchSimilarArtistReq>
internal typealias FetchSimilarArtistReq = FetchSimilarArtistOneShotCase.Request
internal typealias FetchSimilarTrackCase = OneShotUsecase<TopTrackInfoEntity.TracksEntity, FetchSimilarTrackReq>
internal typealias FetchSimilarTrackReq = FetchSimilarTrackOneShotCase.Request
internal typealias FetchTagCase = OneShotUsecase<TagInfoEntity.TagEntity, FetchTagReq>
internal typealias FetchTagReq = FetchTagOneShotCase.Request
internal typealias FetchTagTopAlbumCase = OneShotUsecase<CommonLastFmEntity.TopAlbumsEntity, FetchTagTopAlbumReq>
internal typealias FetchTagTopAlbumReq = FetchTagTopAlbumOneShotCase.Request
internal typealias FetchTagTopArtistCase = OneShotUsecase<TopArtistInfoEntity.ArtistsEntity, FetchTagTopArtistReq>
internal typealias FetchTagTopArtistReq = FetchTagTopArtistOneShotCase.Request
internal typealias FetchTagTopTrackCase = OneShotUsecase<TopTrackInfoEntity.TracksEntity, FetchTagTopTrackReq>
internal typealias FetchTagTopTrackReq = FetchTagTopTrackOneShotCase.Request
internal typealias FetchTrackCase = OneShotUsecase<TrackInfoEntity.TrackEntity, FetchTrackReq>
internal typealias FetchTrackReq = FetchTrackOneShotCase.Request
internal typealias FetchTrackCoverCase = OneShotUsecase<SimpleTrackEntity, FetchTrackCoverReq>
internal typealias FetchTrackCoverReq = FetchTrackCoverOneShotCase.Request

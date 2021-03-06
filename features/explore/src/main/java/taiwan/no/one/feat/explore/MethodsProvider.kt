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

package taiwan.no.one.feat.explore

import com.google.auto.service.AutoService
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.provider.ExploreMethodsProvider
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.explore.data.mappers.EntityMapper
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistCompleteInfoCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistCompleteInfoReq
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopTrackReq
import taiwan.no.one.feat.explore.domain.usecases.FetchTrackCoverCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTrackCoverReq

@AutoService(ExploreMethodsProvider::class)
class MethodsProvider : ExploreMethodsProvider, DIAware {
    override val di by lazy { (DropBeatApp.appContext as DropBeatApp).di }
    private val fetchTagTopTrackCase by instance<FetchTagTopTrackCase>()
    private val fetchTrackCoverCase by instance<FetchTrackCoverCase>()
    private val fetchArtistCompleteCase by instance<FetchArtistCompleteInfoCase>()

    override suspend fun getTopTracksOfTag(tagName: String) =
        fetchTagTopTrackCase.execute(FetchTagTopTrackReq(tagName)).tracks.map(EntityMapper::exploreToSimpleTrackEntity)

    override suspend fun getTrackCover(simpleTrackEntity: SimpleTrackEntity) =
        fetchTrackCoverCase.execute(FetchTrackCoverReq(simpleTrackEntity))

    override suspend fun getArticleInfo(name: String) =
        fetchArtistCompleteCase.execute(FetchArtistCompleteInfoReq(name))
}

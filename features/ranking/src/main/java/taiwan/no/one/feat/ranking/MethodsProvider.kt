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

package taiwan.no.one.feat.ranking

import com.google.auto.service.AutoService
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.core.PlaylistConstant
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.dropbeat.provider.RankingMethodsProvider
import taiwan.no.one.feat.ranking.data.mappers.EntityMapper
import taiwan.no.one.feat.ranking.domain.usecases.FetchMusicRankCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchMusicRankReq
import taiwan.no.one.mediaplayer.utils.MediaUtil

@AutoService(RankingMethodsProvider::class)
class MethodsProvider : RankingMethodsProvider, DIAware {
    override val di by lazy { (DropBeatApp.appContext as DropBeatApp).di }
    private val fetchMusicRankCase by instance<FetchMusicRankCase>()
    private val libraryProvider by instance<LibraryMethodsProvider>()

    override suspend fun getRankingSongs(rankId: Int) = runCatching {
        fetchMusicRankCase.execute(FetchMusicRankReq(rankId.toString()))
            .map(EntityMapper::songToSimpleTrackEntity)
            .map {
                // TODO(jieyi): 6/18/21 move to the business logic.
                if (it.duration == 0) {
                    val duration = MediaUtil.obtainDuration(it.uri)
                    it.copy(duration = duration.toInt())
                }
                else {
                    it
                }
            }
            .onEach {
                val isFavorite = try {
                    libraryProvider.isFavoriteTrack(it.uri, PlaylistConstant.FAVORITE)
                }
                catch (e: Exception) {
                    return@onEach
                }
                it.isFavorite = isFavorite.getOrNull() ?: false
            }
    }
}

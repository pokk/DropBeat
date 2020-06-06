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

package taiwan.no.one.feat.ranking.domain

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.ranking.FeatModules.FEAT_NAME
import taiwan.no.one.feat.ranking.domain.usecases.AddRankIdsCase
import taiwan.no.one.feat.ranking.domain.usecases.AddRankIdsOneShotCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchDetailOfRankingsCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchDetailOfRankingsOneShotCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchMusicRankCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchMusicRankOneShotCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchRankIdsCase
import taiwan.no.one.feat.ranking.domain.usecases.FetchRankIdsOneShotCase
import taiwan.no.one.feat.ranking.domain.usecases.UpdateRankItemCase
import taiwan.no.one.feat.ranking.domain.usecases.UpdateRankItemOneShotCase

internal object DomainModules : ModuleProvider {
    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DomainModule") {
        bind<FetchMusicRankCase>() with singleton { FetchMusicRankOneShotCase(instance()) }
        bind<FetchDetailOfRankingsCase>() with singleton { FetchDetailOfRankingsOneShotCase(instance()) }
        bind<FetchRankIdsCase>() with singleton { FetchRankIdsOneShotCase(instance()) }
        bind<AddRankIdsCase>() with singleton { AddRankIdsOneShotCase(instance()) }
        bind<UpdateRankItemCase>() with singleton { UpdateRankItemOneShotCase(instance()) }
    }
}

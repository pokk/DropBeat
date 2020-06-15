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

package taiwan.no.one.feat.explore.domain

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.explore.FeatModules.FEAT_NAME
import taiwan.no.one.feat.explore.domain.usecases.FetchAlbumCase
import taiwan.no.one.feat.explore.domain.usecases.FetchAlbumOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistPhotoCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistPhotoOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistTopAlbumCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistTopAlbumOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistTopTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchArtistTopTrackOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopArtistCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopArtistOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTagCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTagOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchChartTopTrackOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchSimilarArtistCase
import taiwan.no.one.feat.explore.domain.usecases.FetchSimilarArtistOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchSimilarTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchSimilarTrackOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopAlbumCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopAlbumOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopArtistCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopArtistOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTagTopTrackOneShotCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTrackCase
import taiwan.no.one.feat.explore.domain.usecases.FetchTrackOneShotCase

internal object DomainModules : ModuleProvider {
    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DomainModule") {
        bind<FetchAlbumCase>() with singleton { FetchAlbumOneShotCase(instance()) }
        bind<FetchArtistCase>() with singleton { FetchArtistOneShotCase(instance()) }
        bind<FetchArtistPhotoCase>() with singleton { FetchArtistPhotoOneShotCase(instance()) }
        bind<FetchArtistTopAlbumCase>() with singleton { FetchArtistTopAlbumOneShotCase(instance()) }
        bind<FetchArtistTopTrackCase>() with singleton { FetchArtistTopTrackOneShotCase(instance()) }
        bind<FetchChartTopArtistCase>() with singleton { FetchChartTopArtistOneShotCase(instance()) }
        bind<FetchChartTopTagCase>() with singleton { FetchChartTopTagOneShotCase(instance()) }
        bind<FetchChartTopTrackCase>() with singleton { FetchChartTopTrackOneShotCase(instance()) }
        bind<FetchSimilarArtistCase>() with singleton { FetchSimilarArtistOneShotCase(instance()) }
        bind<FetchSimilarTrackCase>() with singleton { FetchSimilarTrackOneShotCase(instance()) }
        bind<FetchTagCase>() with singleton { FetchTagOneShotCase(instance()) }
        bind<FetchTagTopAlbumCase>() with singleton { FetchTagTopAlbumOneShotCase(instance()) }
        bind<FetchTagTopArtistCase>() with singleton { FetchTagTopArtistOneShotCase(instance()) }
        bind<FetchTagTopTrackCase>() with singleton { FetchTagTopTrackOneShotCase(instance()) }
        bind<FetchTrackCase>() with singleton { FetchTrackOneShotCase(instance()) }
    }
}

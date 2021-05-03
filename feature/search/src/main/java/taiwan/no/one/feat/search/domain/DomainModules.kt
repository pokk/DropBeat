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

package taiwan.no.one.feat.search.domain

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.search.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.search.domain.usecases.AddOrUpdateHistoryCase
import taiwan.no.one.feat.search.domain.usecases.DeleteHistoryCase
import taiwan.no.one.feat.search.domain.usecases.FetchHistoryCase
import taiwan.no.one.feat.search.domain.usecases.FetchMusicCase
import taiwan.no.one.feat.search.domain.usecases.history.AddOrUpdateHistoryOneShotCase
import taiwan.no.one.feat.search.domain.usecases.history.DeleteHistoryOneShotCase
import taiwan.no.one.feat.search.domain.usecases.history.FetchHistoryObservableCase
import taiwan.no.one.feat.search.domain.usecases.music.FetchMusicOneShotCase

internal object DomainModules : ModuleProvider {
    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DomainModule") {
        bindSingleton<FetchMusicCase> { FetchMusicOneShotCase(instance()) }
        bindSingleton<FetchHistoryCase> { FetchHistoryObservableCase(instance()) }
        bindSingleton<AddOrUpdateHistoryCase> { AddOrUpdateHistoryOneShotCase(instance()) }
        bindSingleton<DeleteHistoryCase> { DeleteHistoryOneShotCase(instance()) }
    }
}

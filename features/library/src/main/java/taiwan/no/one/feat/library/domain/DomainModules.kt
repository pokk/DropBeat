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

package taiwan.no.one.feat.library.domain

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.library.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddPlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsAndPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsAndPlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsCase
import taiwan.no.one.feat.library.domain.usecases.AddSongsOneShotCase
import taiwan.no.one.feat.library.domain.usecases.CreateDefaultPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.CreateDefaultPlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.DeletePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.DeletePlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.FetchAllPlaylistsCase
import taiwan.no.one.feat.library.domain.usecases.FetchAllPlaylistsOneShotCase
import taiwan.no.one.feat.library.domain.usecases.FetchIsInThePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchIsInThePlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.FetchLyricCase
import taiwan.no.one.feat.library.domain.usecases.FetchLyricOneShotCase
import taiwan.no.one.feat.library.domain.usecases.FetchPlaylistCase
import taiwan.no.one.feat.library.domain.usecases.FetchPlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.FetchSongCase
import taiwan.no.one.feat.library.domain.usecases.FetchSongOneShotCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistCase
import taiwan.no.one.feat.library.domain.usecases.UpdatePlaylistOneShotCase
import taiwan.no.one.feat.library.domain.usecases.UpdateSongCase
import taiwan.no.one.feat.library.domain.usecases.UpdateSongOneShotCase

internal object DomainModules : ModuleProvider {
    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DomainModule") {
        bindSingleton<FetchPlaylistCase> { FetchPlaylistOneShotCase(instance()) }
        bindSingleton<FetchAllPlaylistsCase> { FetchAllPlaylistsOneShotCase(instance()) }
        bindSingleton<AddPlaylistCase> { AddPlaylistOneShotCase(instance()) }
        bindSingleton<UpdatePlaylistCase> { UpdatePlaylistOneShotCase(instance(), instance()) }
        bindSingleton<DeletePlaylistCase> { DeletePlaylistOneShotCase(instance()) }
        bindSingleton<FetchSongCase> { FetchSongOneShotCase(instance()) }
        bindSingleton<FetchIsInThePlaylistCase> { FetchIsInThePlaylistOneShotCase(instance(), instance()) }
        bindSingleton<AddSongsCase> { AddSongsOneShotCase(instance()) }
        bindSingleton<AddSongsAndPlaylistCase> { AddSongsAndPlaylistOneShotCase(instance(), instance()) }
        bindSingleton<UpdateSongCase> { UpdateSongOneShotCase(instance(), instance()) }
        bindSingleton<CreateDefaultPlaylistCase> { CreateDefaultPlaylistOneShotCase(instance()) }
        bindSingleton<FetchLyricCase> { FetchLyricOneShotCase(instance(), instance()) }
    }
}

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

package taiwan.no.one.sync.domain

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.sync.domain.usecases.AddAccountOneShotCase
import taiwan.no.one.sync.domain.usecases.AddAccountRequest
import taiwan.no.one.sync.domain.usecases.AddPlaylistOneShotCase
import taiwan.no.one.sync.domain.usecases.AddPlaylistRequest
import taiwan.no.one.sync.domain.usecases.AddSongOneShotCase
import taiwan.no.one.sync.domain.usecases.AddSongRequest
import taiwan.no.one.sync.domain.usecases.UpdatePlaylistsOneShotCase
import taiwan.no.one.sync.domain.usecases.UpdatePlaylistsRequest

internal object SyncDomainModules {
    private const val FEAT_NAME = "Sync"

    // NOTE(jieyi): [taiwan.no.one.dropbeat.domain.usecases.TypeAliasesKt] will be aliased here.
    fun provide() = DI.Module("${FEAT_NAME}DomainModule") {
        bindSingleton<OneShotUsecase<Boolean, AddAccountRequest>> { AddAccountOneShotCase(instance()) }
        bindSingleton<OneShotUsecase<Boolean, AddPlaylistRequest>> { AddPlaylistOneShotCase(instance()) }
        bindSingleton<OneShotUsecase<Boolean, AddSongRequest>> { AddSongOneShotCase(instance()) }
        bindSingleton<OneShotUsecase<Boolean, UpdatePlaylistsRequest>> {
            UpdatePlaylistsOneShotCase(instance(), instance())
        }
    }
}

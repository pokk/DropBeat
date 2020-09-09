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

package taiwan.no.one.feat.library.domain.usecases

import taiwan.no.one.core.domain.usecase.OneShotUsecase
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity

internal typealias FetchPlaylistCase = OneShotUsecase<PlayListEntity, FetchPlaylistReq>
internal typealias FetchPlaylistReq = FetchPlaylistOneShotCase.Request
internal typealias FetchAllPlaylistsCase = OneShotUsecase<List<PlayListEntity>, FetchAllPlaylistsReq>
internal typealias FetchAllPlaylistsReq = FetchAllPlaylistsOneShotCase.Request
internal typealias AddPlaylistCase = OneShotUsecase<Boolean, AddPlaylistReq>
internal typealias AddPlaylistReq = AddPlaylistOneShotCase.Request
internal typealias UpdatePlaylistCase = OneShotUsecase<Boolean, UpdatePlaylistReq>
internal typealias UpdatePlaylistReq = UpdatePlaylistOneShotCase.Request
internal typealias DeletePlaylistCase = OneShotUsecase<Boolean, DeletePlaylistReq>
internal typealias DeletePlaylistReq = DeletePlaylistOneShotCase.Request
internal typealias AddSongsCase = OneShotUsecase<Boolean, AddSongsReq>
internal typealias AddSongsReq = AddSongsOneShotCase.Request

internal typealias CreateDefaultPlaylistCase = OneShotUsecase<Boolean, CreateDefaultPlaylistReq>
internal typealias CreateDefaultPlaylistReq = CreateDefaultPlaylistOneShotCase.Request

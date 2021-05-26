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

package taiwan.no.one.feat.setting.domain.usecases

import taiwan.no.one.core.domain.parameter.NonRequest
import taiwan.no.one.core.domain.usecase.OneShotUsecase

internal typealias FetchSleepingTimerCase = OneShotUsecase<Boolean, NonRequest>
internal typealias FetchLockScreenPlayerCase = OneShotUsecase<Boolean, NonRequest>
internal typealias FetchPlayOfflineOnlyCase = OneShotUsecase<Boolean, NonRequest>
internal typealias FetchNotificationPlayerCase = OneShotUsecase<Boolean, NonRequest>
internal typealias FetchAutoDisplayMvCase = OneShotUsecase<Boolean, NonRequest>

internal typealias AddSleepingTimerCase = OneShotUsecase<Boolean, AddSleepingTimerReq>
internal typealias AddSleepingTimerReq = AddSleepingTimerOneShotCase.Request
internal typealias AddLockScreenPlayerCase = OneShotUsecase<Boolean, AddLockScreenPlayerReq>
internal typealias AddLockScreenPlayerReq = AddLockScreenPlayerOneShotCase.Request
internal typealias AddPlayOfflineOnlyCase = OneShotUsecase<Boolean, AddPlayOfflineOnlyReq>
internal typealias AddPlayOfflineOnlyReq = AddPlayOfflineOnlyOneShotCase.Request
internal typealias AddNotificationPlayerCase = OneShotUsecase<Boolean, AddNotificationPlayerReq>
internal typealias AddNotificationPlayerReq = AddNotificationPlayerOneShotCase.Request
internal typealias AddAutoDisplayMvCase = OneShotUsecase<Boolean, AddAutoDisplayMvReq>
internal typealias AddAutoDisplayMvReq = AddAutoDisplayMvOneShotCase.Request

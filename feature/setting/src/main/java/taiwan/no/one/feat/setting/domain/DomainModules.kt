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

package taiwan.no.one.feat.setting.domain

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.setting.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.setting.domain.usecases.AddAutoDisplayMvCase
import taiwan.no.one.feat.setting.domain.usecases.AddAutoDisplayMvOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.AddLockScreenPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.AddLockScreenPlayerOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.AddNotificationPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.AddNotificationPlayerOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.AddPlayOfflineOnlyCase
import taiwan.no.one.feat.setting.domain.usecases.AddPlayOfflineOnlyOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.AddSleepingTimerCase
import taiwan.no.one.feat.setting.domain.usecases.AddSleepingTimerOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.FetchAutoDisplayMvCase
import taiwan.no.one.feat.setting.domain.usecases.FetchAutoDisplayMvObservableCase
import taiwan.no.one.feat.setting.domain.usecases.FetchLockScreenPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchLockScreenPlayerObservableCase
import taiwan.no.one.feat.setting.domain.usecases.FetchNotificationPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchNotificationPlayerObservableCase
import taiwan.no.one.feat.setting.domain.usecases.FetchPlayOfflineOnlyCase
import taiwan.no.one.feat.setting.domain.usecases.FetchPlayOfflineOnlyObservableCase
import taiwan.no.one.feat.setting.domain.usecases.FetchSleepingTimerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchSleepingTimerObservableCase

internal object DomainModules : ModuleProvider {
    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DomainModule") {
        bind<FetchSleepingTimerCase>() with singleton { FetchSleepingTimerObservableCase(instance()) }
        bind<FetchLockScreenPlayerCase>() with singleton { FetchLockScreenPlayerObservableCase(instance()) }
        bind<FetchPlayOfflineOnlyCase>() with singleton { FetchPlayOfflineOnlyObservableCase(instance()) }
        bind<FetchNotificationPlayerCase>() with singleton { FetchNotificationPlayerObservableCase(instance()) }
        bind<FetchAutoDisplayMvCase>() with singleton { FetchAutoDisplayMvObservableCase(instance()) }
        bind<AddSleepingTimerCase>() with singleton { AddSleepingTimerOneShotCase(instance()) }
        bind<AddLockScreenPlayerCase>() with singleton { AddLockScreenPlayerOneShotCase(instance()) }
        bind<AddPlayOfflineOnlyCase>() with singleton { AddPlayOfflineOnlyOneShotCase(instance()) }
        bind<AddNotificationPlayerCase>() with singleton { AddNotificationPlayerOneShotCase(instance()) }
        bind<AddAutoDisplayMvCase>() with singleton { AddAutoDisplayMvOneShotCase(instance()) }
    }
}

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
import org.kodein.di.bindSingleton
import org.kodein.di.instance
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
import taiwan.no.one.feat.setting.domain.usecases.FetchAutoDisplayMvOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.FetchLockScreenPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchLockScreenPlayerOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.FetchNotificationPlayerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchNotificationPlayerOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.FetchPlayOfflineOnlyCase
import taiwan.no.one.feat.setting.domain.usecases.FetchPlayOfflineOnlyOneShotCase
import taiwan.no.one.feat.setting.domain.usecases.FetchSleepingTimerCase
import taiwan.no.one.feat.setting.domain.usecases.FetchSleepingTimerOneShotCase

internal object DomainModules : ModuleProvider {
    const val TAG_SLEEPING_TIMER = "sleeping timer"
    const val TAG_LOCK_SCREEN_PLAYER = "lock screen player"
    const val TAG_PLAY_OFFLINE_ONLY = "play offline only"
    const val TAG_NOTIFICATION_PLAYER = "notification player"
    const val TAG_AUTO_DISPLAY_MV = "auto display mv"

    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DomainModule") {
        bindSingleton<FetchSleepingTimerCase>(TAG_SLEEPING_TIMER) { FetchSleepingTimerOneShotCase(instance()) }
        bindSingleton<FetchLockScreenPlayerCase>(TAG_LOCK_SCREEN_PLAYER) {
            FetchLockScreenPlayerOneShotCase(instance())
        }
        bindSingleton<FetchPlayOfflineOnlyCase>(TAG_PLAY_OFFLINE_ONLY) { FetchPlayOfflineOnlyOneShotCase(instance()) }
        bindSingleton<FetchNotificationPlayerCase>(TAG_NOTIFICATION_PLAYER) {
            FetchNotificationPlayerOneShotCase(instance())
        }
        bindSingleton<FetchAutoDisplayMvCase>(TAG_AUTO_DISPLAY_MV) { FetchAutoDisplayMvOneShotCase(instance()) }
        bindSingleton<AddSleepingTimerCase> { AddSleepingTimerOneShotCase(instance()) }
        bindSingleton<AddLockScreenPlayerCase> { AddLockScreenPlayerOneShotCase(instance()) }
        bindSingleton<AddPlayOfflineOnlyCase> { AddPlayOfflineOnlyOneShotCase(instance()) }
        bindSingleton<AddNotificationPlayerCase> { AddNotificationPlayerOneShotCase(instance()) }
        bindSingleton<AddAutoDisplayMvCase> { AddAutoDisplayMvOneShotCase(instance()) }
    }
}

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

package taiwan.no.one.feat.setting.data.stores

import taiwan.no.one.feat.setting.data.contracts.DataStore
import taiwan.no.one.feat.setting.data.local.services.SettingPreference

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
internal class LocalStore(
    private val preference: SettingPreference,
) : DataStore {
    override fun getSleepingTimer() = preference.getSleepingTimer()

    override suspend fun setSleepingTimer(enable: Boolean) = preference.setSleepingTimer(enable)

    override fun getLockScreenPlayer() = preference.getLockScreenPlayer()

    override suspend fun setLockScreenPlayer(enable: Boolean) = preference.setLockScreenPlayer(enable)

    override fun getPlayOfflineOnly() = preference.getPlayOfflineOnly()

    override suspend fun setPlayOfflineOnly(enable: Boolean) = preference.setPlayOfflineOnly(enable)

    override fun getNotificationPlayer() = preference.getNotificationPlayer()

    override suspend fun setNotificationPlayer(enable: Boolean) = preference.setNotificationPlayer(enable)

    override fun getAutoDisplayMv() = preference.getAutoDisplayMv()

    override suspend fun setAutoDisplayMv(enable: Boolean) = preference.setAutoDisplayMv(enable)
}

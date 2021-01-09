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

package taiwan.no.one.feat.setting.data.repositories

import taiwan.no.one.feat.setting.data.contracts.DataStore
import taiwan.no.one.feat.setting.domain.repositories.SettingRepo

internal class SettingRepository(
    private val local: DataStore,
    private val remote: DataStore,
) : SettingRepo {
    override fun fetchSleepingTimer() = local.getSleepingTimer()

    override suspend fun addSleepingTimer(isCheck: Boolean) = local.setSleepingTimer(isCheck)

    override fun fetchLockScreenPlayer() = local.getLockScreenPlayer()

    override suspend fun addLockScreenPlayer(isCheck: Boolean) = local.setLockScreenPlayer(isCheck)

    override fun fetchPlayOfflineOnly() = local.getPlayOfflineOnly()

    override suspend fun addPlayOfflineOnly(isCheck: Boolean) = local.setPlayOfflineOnly(isCheck)

    override fun fetchNotificationPlayer() = local.getNotificationPlayer()

    override suspend fun addNotificationPlayer(isCheck: Boolean) = local.setNotificationPlayer(isCheck)

    override fun fetchAutoDisplayMv() = local.getAutoDisplayMv()

    override suspend fun addAutoDisplayMv(isCheck: Boolean) = local.setAutoDisplayMv(isCheck)
}

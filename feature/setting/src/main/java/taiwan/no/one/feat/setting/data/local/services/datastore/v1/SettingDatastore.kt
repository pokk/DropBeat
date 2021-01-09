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

package taiwan.no.one.feat.setting.data.local.services.datastore.v1

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.map
import taiwan.no.one.feat.setting.data.local.services.SettingPreference

internal class SettingDatastore(
    private val dataStore: DataStore<Preferences>,
) : SettingPreference {
    private val keySleepingTimer = preferencesKey<Boolean>("sleeping timer")
    private val keyLockScreenPlayer = preferencesKey<Boolean>("lock screen player")
    private val keyPlayOfflineOnly = preferencesKey<Boolean>("play offline only")
    private val keyNotificationPlayer = preferencesKey<Boolean>("notification player")
    private val keyAutoDisplayMv = preferencesKey<Boolean>("auto display mv")

    override fun getSleepingTimer() = getBooleanValue(keySleepingTimer)

    override suspend fun setSleepingTimer(enable: Boolean) = setBooleanValue(keySleepingTimer, enable)

    override fun getLockScreenPlayer() = getBooleanValue(keyLockScreenPlayer)

    override suspend fun setLockScreenPlayer(enable: Boolean) = setBooleanValue(keyLockScreenPlayer, enable)

    override fun getPlayOfflineOnly() = getBooleanValue(keyPlayOfflineOnly)

    override suspend fun setPlayOfflineOnly(enable: Boolean) = setBooleanValue(keyPlayOfflineOnly, enable)

    override fun getNotificationPlayer() = getBooleanValue(keyNotificationPlayer)

    override suspend fun setNotificationPlayer(enable: Boolean) = setBooleanValue(keyNotificationPlayer, enable)

    override fun getAutoDisplayMv() = getBooleanValue(keyAutoDisplayMv)

    override suspend fun setAutoDisplayMv(enable: Boolean) = setBooleanValue(keyAutoDisplayMv, enable)

    private inline fun getBooleanValue(key: Preferences.Key<Boolean>) = dataStore.data.map { it[key] ?: false }

    private suspend inline fun setBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit {
            it[key] = value
        }
    }
}

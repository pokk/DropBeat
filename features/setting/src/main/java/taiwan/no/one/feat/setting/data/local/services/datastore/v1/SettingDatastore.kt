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
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import taiwan.no.one.feat.setting.data.local.services.SettingPreference

internal class SettingDatastore(
    private val dataStore: DataStore<Preferences>,
) : SettingPreference {
    private val keySleepingTimer = booleanPreferencesKey("sleeping timer")
    private val keyLockScreenPlayer = booleanPreferencesKey("lock screen player")
    private val keyPlayOfflineOnly = booleanPreferencesKey("play offline only")
    private val keyNotificationPlayer = booleanPreferencesKey("notification player")
    private val keyAutoDisplayMv = booleanPreferencesKey("auto display mv")

    override suspend fun getSleepingTimer() = getBooleanValue(keySleepingTimer)

    override suspend fun setSleepingTimer(isCheck: Boolean) = setBooleanValue(keySleepingTimer, isCheck)

    override suspend fun getLockScreenPlayer() = getBooleanValue(keyLockScreenPlayer)

    override suspend fun setLockScreenPlayer(isCheck: Boolean) = setBooleanValue(keyLockScreenPlayer, isCheck)

    override suspend fun getPlayOfflineOnly() = getBooleanValue(keyPlayOfflineOnly)

    override suspend fun setPlayOfflineOnly(isCheck: Boolean) = setBooleanValue(keyPlayOfflineOnly, isCheck)

    override suspend fun getNotificationPlayer() = getBooleanValue(keyNotificationPlayer)

    override suspend fun setNotificationPlayer(isCheck: Boolean) = setBooleanValue(keyNotificationPlayer, isCheck)

    override suspend fun getAutoDisplayMv() = getBooleanValue(keyAutoDisplayMv)

    override suspend fun setAutoDisplayMv(isCheck: Boolean) = setBooleanValue(keyAutoDisplayMv, isCheck)

    private suspend inline fun getBooleanValue(key: Preferences.Key<Boolean>) =
        dataStore.data.map { it[key] ?: false }.first()

    private suspend inline fun setBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit {
            it[key] = value
        }
    }
}

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

package taiwan.no.one.feat.setting.data.local.services

import kotlinx.coroutines.flow.Flow

/**
 * Using the local [androidx.datastore.core.DataStore] to set the setting flag.
 * Using prefix name (get), (set), (update), (remove) as following
 * the rule of [androidx.datastore.core.DataStore]'s operations.
 */
internal interface SettingPreference {
    suspend fun getSleepingTimer(): Flow<Boolean>

    suspend fun setSleepingTimer(enable: Boolean)

    suspend fun getLockScreenPlayer(): Flow<Boolean>

    suspend fun setLockScreenPlayer(enable: Boolean)

    suspend fun getPlayOfflineOnly(): Flow<Boolean>

    suspend fun setPlayOfflineOnly(enable: Boolean)

    suspend fun getNotificationPlayer(): Flow<Boolean>

    suspend fun setNotificationPlayer(enable: Boolean)

//    suspend fun getQualityOfMusic(): Flow<Int>

//    suspend fun getQualityOfMusic(quality: Int)

    suspend fun getAutoDisplayMv(): Flow<Boolean>

    suspend fun setAutoDisplayMv(enable: Boolean)
}

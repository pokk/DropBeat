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

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore : DataStore {
    override suspend fun getSleepingTimer() = TODO()

    override suspend fun setSleepingTimer(isCheck: Boolean) = TODO()

    override suspend fun getLockScreenPlayer() = TODO()

    override suspend fun setLockScreenPlayer(isCheck: Boolean) = TODO()

    override suspend fun getPlayOfflineOnly() = TODO()

    override suspend fun setPlayOfflineOnly(isCheck: Boolean) = TODO()

    override suspend fun getNotificationPlayer() = TODO()

    override suspend fun setNotificationPlayer(isCheck: Boolean) = TODO()

    override suspend fun getAutoDisplayMv() = TODO()

    override suspend fun setAutoDisplayMv(isCheck: Boolean) = TODO()
}

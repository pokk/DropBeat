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

package taiwan.no.one.dropbeat.data.local.services.v1

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import taiwan.no.one.dropbeat.data.entities.UserInfoEntity
import taiwan.no.one.dropbeat.data.local.services.PrivacyService

internal class MmkvService(
    private val mmkv: MMKV,
    private val gson: Gson,
) : PrivacyService {
    companion object Constant {
        private const val CODE_LOGIN_INFO = "user login info"
    }

    override suspend fun retrieveLoginInfo(): UserInfoEntity {
        val infoString = mmkv.getString(CODE_LOGIN_INFO, null) ?: throw NullPointerException()
        return gson.fromJson(infoString, UserInfoEntity::class.java)
    }

    override suspend fun insertLoginInfo(entity: UserInfoEntity): Boolean {
        val infoString = gson.toJson(entity)
        return mmkv.encode(CODE_LOGIN_INFO, infoString)
    }

    override suspend fun releaseLoginInfo(uid: String): Boolean {
        mmkv.removeValueForKey(CODE_LOGIN_INFO)
        return true
    }
}

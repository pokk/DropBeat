/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
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

package taiwan.no.one.dropbeat.data.repositories

import taiwan.no.one.dropbeat.data.contracts.DataStore
import taiwan.no.one.dropbeat.domain.repositories.PrivacyRepo
import taiwan.no.one.entity.UserInfoEntity

internal class PrivacyRepository(
    private val local: DataStore,
    private val remote: DataStore,
) : PrivacyRepo {
    override suspend fun fetchLoginInfo() = local.getLoginInfo()

    override suspend fun keepLoginInfo(entity: UserInfoEntity) = local.createLoginInfo(entity)

    override suspend fun deleteLoginInfo(uid: String) = local.removeLoginInfo(uid)
}

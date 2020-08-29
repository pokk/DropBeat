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

package taiwan.no.one.feat.login.data.remote.services.firebase.v1

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import taiwan.no.one.feat.login.data.entities.remote.UserInfoEntity
import taiwan.no.one.feat.login.data.remote.services.AuthService
import taiwan.no.one.feat.login.data.remote.services.firebase.Credential

internal class FirebaseAuthService(
    private val auth: FirebaseAuth,
) : AuthService {
    override suspend fun getLogin(email: String, password: String): UserInfoEntity {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return extractUserInfoEntity(result)
    }

    override suspend fun getLogin(credential: Credential): UserInfoEntity {
        val result = auth.signInWithCredential(credential.getAuthCredential()).await()
        return extractUserInfoEntity(result)
    }

    override suspend fun createUser(email: String, password: String): UserInfoEntity {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return extractUserInfoEntity(result)
    }

    override suspend fun modifyPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    private fun extractUserInfoEntity(result: AuthResult): UserInfoEntity {
        val user = result.user
        val additionalUserInfo = result.additionalUserInfo
        return UserInfoEntity(
            user?.uid,
            user?.providerId,
            user?.displayName,
            user?.photoUrl?.toString(),
            user?.email,
            user?.phoneNumber,
            user?.isEmailVerified,
            additionalUserInfo?.username,
            additionalUserInfo?.isNewUser
        )
    }
}

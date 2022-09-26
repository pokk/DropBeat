/*
 * MIT License
 *
 * Copyright (c) 2022 Jieyi
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.feat.login.data.remote.services.AuthService
import taiwan.no.one.feat.login.data.remote.services.firebase.Credential

// NOTE(jieyi): 5/26/21
//  For one-shot async calls, use the [suspendCancellableCoroutine] API.
//  For streaming data, use the [callbackFlow] API.
internal class FirebaseAuthService(
    private val auth: FirebaseAuth,
) : AuthService {
    override suspend fun getLogin(email: String, password: String) = suspendCancellableCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            continuation.resume(extractUserInfoEntity(it))
        }.addOnFailureListener(continuation::resumeWithException)
    }

    override suspend fun getLogin(credential: Credential) = suspendCancellableCoroutine { continuation ->
        auth.signInWithCredential(credential.getAuthCredential()).addOnSuccessListener {
            continuation.resume(extractUserInfoEntity(it))
        }.addOnFailureListener(continuation::resumeWithException)
    }

    override suspend fun getLogout(): Boolean {
        auth.signOut()
        return true
    }

    override suspend fun createUser(email: String, password: String) = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            continuation.resume(extractUserInfoEntity(it))
        }.addOnFailureListener(continuation::resumeWithException)
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

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

package taiwan.no.one.sync.data.remote.services.firebase.v1

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.asDeferred
import taiwan.no.one.sync.data.remote.services.SyncService

internal class FirebaseSyncService(
    private val firestore: FirebaseFirestore,
) : SyncService {
    companion object Constant {
        private const val ACCOUNT_COLLECTION_PROVIDER = "provider"
        private const val ACCOUNT_COLLECTION_EMAIL = "email"
    }

    override suspend fun createAccount(): Boolean {
        firestore.collection(ACCOUNT_COLLECTION_PROVIDER)
            .document()
            .collection(ACCOUNT_COLLECTION_EMAIL)
            .document()
            .set("")
            .asDeferred()
        return true
    }

    override suspend fun getPlaylists() = TODO()

    override suspend fun modifyPlaylist() = TODO()

    override suspend fun createPlaylist() = TODO()

    override suspend fun removePlaylist() = TODO()

    override suspend fun getSongs() = TODO()

    override suspend fun modifySong() = TODO()

    override suspend fun createSong() = TODO()
}

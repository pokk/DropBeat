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

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.entity.UserInfoEntity
import taiwan.no.one.sync.data.remote.services.SyncService

// NOTE(jieyi): 5/26/21
//  For one-shot async calls, use the [suspendCancellableCoroutine] API.
//  For streaming data, use the [callbackFlow] API.
internal class FirebaseSyncService(
    private val firestore: FirebaseFirestore,
) : SyncService {
    companion object Constant {
        private const val COLLECTION_PROVIDER = "provider"
        private const val COLLECTION_EMAIL = "email"
        private const val FIELD_PLAYLIST = "playlists"
        private const val COLLECTION_PLAYLIST = "playlist"
        private const val FIELD_NAME = "name"
        private const val FIELD_SONGS = "songs"
        private const val COLLECTION_SONG = "song"
    }

    override suspend fun createAccount(userInfo: UserInfoEntity) =
        suspendCancellableCoroutine<Boolean> { continuation ->
            val data = mapOf(FIELD_PLAYLIST to listOf<DocumentReference>())
            getUserInfoDocument(userInfo).set(data)
                .addOnSuccessListener { continuation.resume(false) }
                .addOnFailureListener(continuation::resumeWithException)
        }

    override suspend fun getPlaylists(userInfo: UserInfoEntity) = coroutineScope {
        suspendCancellableCoroutine<List<SimplePlaylistEntity>> { continuation ->
            getUserInfoDocument(userInfo).get()
                .addOnSuccessListener {
                    launch {
                        val playlists = (it[FIELD_PLAYLIST] as? List<DocumentReference>)?.mapNotNull {
                            it.get().await().toObject(SimplePlaylistEntity::class.java)
                        }
                        continuation.resume(playlists.orEmpty())
                    }
                }
                .addOnFailureListener(continuation::resumeWithException)
        }
    }

    override suspend fun modifyPlaylist() = TODO()

    override suspend fun createPlaylist(name: String) = suspendCancellableCoroutine<String> { continuation ->
        val playlist = mapOf(
            FIELD_NAME to name,
            FIELD_SONGS to emptyList<DocumentReference>()
        )
        val doc = firestore.collection(COLLECTION_PLAYLIST).document()
        doc.set(playlist)
            .addOnSuccessListener { continuation.resume(doc.path) }
            .addOnFailureListener(continuation::resumeWithException)
    }

    override suspend fun removePlaylist() = TODO()

    override suspend fun getSongs() = TODO()

    override suspend fun modifySong() = TODO()

    override suspend fun createSong(song: SimpleTrackEntity) = suspendCancellableCoroutine<String> { continuation ->
        val doc = firestore.collection(COLLECTION_SONG).document(song.obtainTrackAndArtistName())
        doc.set(song.toSet())
            .addOnSuccessListener { continuation.resume(doc.path) }
            .addOnFailureListener(continuation::resumeWithException)
    }

    private fun getUserInfoDocument(userInfo: UserInfoEntity) = firestore.collection(COLLECTION_PROVIDER)
        .document(userInfo.providerId.orEmpty())
        .collection(COLLECTION_EMAIL)
        .document(userInfo.email.orEmpty())
}

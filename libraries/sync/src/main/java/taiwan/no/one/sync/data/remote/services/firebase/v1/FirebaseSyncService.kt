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
import com.google.firebase.firestore.FieldValue
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
        private const val FIELD_ID = "id"
        private const val FIELD_NAME = "name"
        private const val FIELD_SONGS = "songs"
        private const val COLLECTION_SONG = "song"
    }
    /**
     * Create a complete playlist flow,
     *
     * 1. Create songs' document and get all reference path of songs.
     * 2. Create playlist document.
     * 3. Attach the songs' path to the playlist field.
     */
    override suspend fun createAccount(userInfo: UserInfoEntity) =
        suspendCancellableCoroutine<Boolean> { continuation ->
            val data = mapOf(FIELD_PLAYLIST to listOf<DocumentReference>())
            getUserInfoDocument(userInfo).set(data)
                .addOnSuccessListener { continuation.resume(true) }
                .addOnFailureListener(continuation::resumeWithException)
        }

    override suspend fun getPlaylists(userInfo: UserInfoEntity) = coroutineScope {
        suspendCancellableCoroutine<List<SimplePlaylistEntity>> { continuation ->
            getUserInfoDocument(userInfo).get()
                .addOnSuccessListener {
                    launch {
                        val playlists = castDocList(it[FIELD_PLAYLIST])?.mapNotNull {
                            it.get().await().toObject(SimplePlaylistEntity::class.java)
                        }
                        continuation.resume(playlists.orEmpty())
                    }
                }
                .addOnFailureListener(continuation::resumeWithException)
        }
    }

    override suspend fun modifyPlaylist(userInfo: UserInfoEntity, playlist: SimplePlaylistEntity) = TODO()

    override suspend fun createPlaylist(playlist: SimplePlaylistEntity) =
        suspendCancellableCoroutine<String> { continuation ->
            // Create a document of the playlist.
            val doc = firestore.collection(COLLECTION_PLAYLIST).document()
            // Set the field detail.
            doc.set(playlist.toFieldMap())
                .addOnSuccessListener { continuation.resume(doc.path) }
                .addOnFailureListener(continuation::resumeWithException)
        }

    override suspend fun removePlaylist(playlistPath: String) = suspendCancellableCoroutine<Boolean> { continuation ->
        firestore.document(playlistPath)
            .delete()
            .addOnSuccessListener { continuation.resume(true) }
            .addOnFailureListener(continuation::resumeWithException)
    }

    override suspend fun getSongs(playlistPath: String) = coroutineScope {
        suspendCancellableCoroutine<List<SimpleTrackEntity>> { continuation ->
            firestore.document(playlistPath).get()
                .addOnSuccessListener {
                    launch {
                        val songs = castDocList(it[FIELD_SONGS])?.mapNotNull {
                            it.get().await().toObject(SimpleTrackEntity::class.java)
                        }
                        continuation.resume(songs.orEmpty())
                    }
                }
                .addOnFailureListener(continuation::resumeWithException)
        }
    }

    override suspend fun createSong(song: SimpleTrackEntity) = suspendCancellableCoroutine<String> { continuation ->
        // Create a document of a song.
        val doc = firestore.collection(COLLECTION_SONG).document(song.obtainTrackAndArtistName())
        // Set the field detail.
        doc.set(song.toSet())
            .addOnSuccessListener { continuation.resume(doc.path) }
            .addOnFailureListener(continuation::resumeWithException)
    }

    override suspend fun createPlaylistRefToAccount(userInfo: UserInfoEntity, refPlaylistPaths: List<String>) =
        suspendCancellableCoroutine<Boolean> { continuation ->
            val paths = refPlaylistPaths.map(firestore::document)
            getUserInfoDocument(userInfo)
                .update(FIELD_PLAYLIST, FieldValue.arrayUnion(paths))
                .addOnSuccessListener { continuation.resume(true) }
                .addOnFailureListener(continuation::resumeWithException)
        }

    override suspend fun createSongRefToPlaylist(
        userInfo: UserInfoEntity,
        refPlaylistPath: String,
        refSongsPath: List<String>,
    ) = suspendCancellableCoroutine<Boolean> { continuation ->
        val paths = refSongsPath.map(firestore::document)
        firestore.document(refPlaylistPath)
            .update(FIELD_SONGS, FieldValue.arrayUnion(paths))
            .addOnSuccessListener { continuation.resume(true) }
            .addOnFailureListener(continuation::resumeWithException)
    }

    private fun getUserInfoDocument(userInfo: UserInfoEntity) = firestore.collection(COLLECTION_PROVIDER)
        .document(userInfo.providerId.orEmpty())
        .collection(COLLECTION_EMAIL)
        .document(userInfo.email.orEmpty())

    private fun castDocList(data: Any?) = data as? List<DocumentReference>
}

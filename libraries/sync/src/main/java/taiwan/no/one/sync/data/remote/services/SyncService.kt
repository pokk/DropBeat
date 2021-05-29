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

package taiwan.no.one.sync.data.remote.services

import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.entity.UserInfoEntity

/**
 * This interface will be the same as all data stores.
 * Using prefix name (get), (create), (modify), (remove), (store)
 */
internal interface SyncService {
    suspend fun createAccount(userInfo: UserInfoEntity): Boolean

    suspend fun getPlaylists(userInfo: UserInfoEntity): List<SimplePlaylistEntity>

    suspend fun modifyPlaylist(userInfo: UserInfoEntity, playlistEntity: SimplePlaylistEntity): Boolean

    suspend fun createPlaylist(name: String): String

    suspend fun removePlaylist(playlistPath: String): Boolean

    suspend fun getSongs(playlistPath: String): List<SimpleTrackEntity>

    suspend fun modifySong(): Boolean

    suspend fun createSong(song: SimpleTrackEntity): String

    suspend fun createPlaylistRefToAccount(userInfo: UserInfoEntity, refPlaylistPath: String): Boolean

    suspend fun createSongRefToPlaylist(userInfo: UserInfoEntity, playlistName: String, refSongPath: String): Boolean
}

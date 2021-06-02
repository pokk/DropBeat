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

package taiwan.no.one.feat.library.data.repositories

import taiwan.no.one.feat.library.data.contracts.DataStore
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class SongRepository(
    private val local: DataStore,
    private val remote: DataStore,
) : SongRepo {
    override suspend fun getMusic(songId: Int) = local.getMusic(songId)

    override suspend fun getMusic(remoteUri: String?, localUri: String?) = local.getMusic(remoteUri, localUri)

    override suspend fun addMusic(song: SongEntity) = local.createMusic(song)

    override suspend fun addMusics(songs: List<SongEntity>) = local.createMusics(songs)

    override suspend fun updateMusic(song: SongEntity) = local.modifyMusic(song)

    override suspend fun updateMusic(songId: Int, isFavorite: Boolean) = local.modifyMusic(songId, isFavorite)
}

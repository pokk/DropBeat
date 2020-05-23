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

package taiwan.no.one.featSearchMusic.domain.repositories

import taiwan.no.one.core.domain.repository.Repository
import taiwan.no.one.featSearchMusic.data.entities.remote.MusicInfoEntity.MusicEntity
import taiwan.no.one.featSearchMusic.domain.models.Dummy

/**
 * This interface will be the similar to [taiwan.no.one.featSearchMusic.data.contracts.DataStore].
 * Using prefix name (fetch), (add), (update), (delete), (keep)
 */
internal interface SearchMusicRepo : Repository {
    suspend fun fetchDummies(): List<Dummy>

    suspend fun fetchMusic(): MusicEntity
}

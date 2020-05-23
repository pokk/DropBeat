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

package taiwan.no.one.featSearchMusic.data.stores

import taiwan.no.one.ext.exceptions.UnsupportedOperation
import taiwan.no.one.featSearchMusic.BuildConfig
import taiwan.no.one.featSearchMusic.data.contracts.DataStore
import taiwan.no.one.featSearchMusic.data.entities.local.SearchHistoryEntity
import taiwan.no.one.featSearchMusic.data.entities.remote.MusicInfoEntity
import taiwan.no.one.featSearchMusic.data.remote.services.SeekerBankService

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
internal class RemoteStore(
    private val seekerBankService: SeekerBankService
) : DataStore {
    override suspend fun getMusic(): MusicInfoEntity {
        val queries = hashMapOf(
            BuildConfig.SearchMusicQuery to BuildConfig.SearchMusicParameter
        )
        return seekerBankService.retrieveSearchMusic(queries)
    }

    override suspend fun createOrModifySearchHistory(keyword: String) = throw UnsupportedOperation()

    override suspend fun getSearchHistories(count: Int) = throw UnsupportedOperation()

    override suspend fun removeSearchHistory(
        keyword: String?,
        entity: SearchHistoryEntity?
    ) = throw UnsupportedOperation()
}

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

package taiwan.no.one.feat.search.data

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import taiwan.no.one.core.data.remote.DefaultRetrofitConfig
import taiwan.no.one.dropbeat.di.Constant
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.search.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.search.data.contracts.DataStore
import taiwan.no.one.feat.search.data.local.configs.BankDatabase
import taiwan.no.one.feat.search.data.local.services.database.v1.SearchHistoryDao
import taiwan.no.one.feat.search.data.remote.RestfulApiFactory
import taiwan.no.one.feat.search.data.remote.configs.SeekerConfig
import taiwan.no.one.feat.search.data.remote.services.retrofit.v1.SeekerBankService
import taiwan.no.one.feat.search.data.repositories.HistoryRepository
import taiwan.no.one.feat.search.data.repositories.SearchMusicRepository
import taiwan.no.one.feat.search.data.stores.LocalStore
import taiwan.no.one.feat.search.data.stores.RemoteStore
import taiwan.no.one.feat.search.domain.repositories.HistoryRepo
import taiwan.no.one.feat.search.domain.repositories.SearchMusicRepo

internal object DataModules : ModuleProvider {
    private const val TAG_LOCAL_DATA_STORE = "$FEAT_NAME local data store"
    private const val TAG_REMOTE_DATA_STORE = "$FEAT_NAME remote data store"

    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<DataStore>(TAG_LOCAL_DATA_STORE) with singleton { LocalStore(instance(), instance(), instance()) }
        bind<DataStore>(TAG_REMOTE_DATA_STORE) with singleton { RemoteStore(instance()) }

        bind<SearchMusicRepo>() with singleton {
            SearchMusicRepository(instance(TAG_REMOTE_DATA_STORE), instance(TAG_LOCAL_DATA_STORE))
        }
        bind<HistoryRepo>() with singleton { HistoryRepository(instance(TAG_LOCAL_DATA_STORE)) }
    }

    private fun localProvide() = DI.Module("${FEAT_NAME}LocalModule") {
        bind<BankDatabase>() with singleton { BankDatabase.getDatabase(instance()) }

        bind<SearchHistoryDao>() with singleton { instance<BankDatabase>().createSearchHistoryDao() }
    }

    private fun remoteProvide(context: Context) = DI.Module("${FEAT_NAME}RemoteModule") {
        bind<SeekerConfig>() with instance(RestfulApiFactory().createSeekerConfig())

        bind<Retrofit>(Constant.TAG_FEAT_SEARCH_RETROFIT) with singleton {
            DefaultRetrofitConfig(context, instance<SeekerConfig>().apiBaseUrl).provideRetrofitBuilder().build()
        }

        bind<SeekerBankService>() with singleton {
            instance<Retrofit>(Constant.TAG_FEAT_SEARCH_RETROFIT).create(SeekerBankService::class.java)
        }
    }
}

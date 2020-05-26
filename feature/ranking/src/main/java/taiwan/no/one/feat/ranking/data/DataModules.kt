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

package taiwan.no.one.feat.ranking.data

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import taiwan.no.one.core.data.remote.DefaultRetrofitConfig
import taiwan.no.one.dropbeat.di.Constant
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.ranking.FeatModules.FEAT_NAME
import taiwan.no.one.feat.ranking.data.local.configs.BankDatabase
import taiwan.no.one.feat.ranking.data.local.services.database.v1.DummyDao
import taiwan.no.one.feat.ranking.data.local.services.json.v1.DummyFile
import taiwan.no.one.feat.ranking.data.remote.RestfulApiFactory
import taiwan.no.one.feat.ranking.data.remote.configs.SeekerConfig
import taiwan.no.one.feat.ranking.data.remote.services.SeekerBankService
import taiwan.no.one.feat.ranking.data.repositories.SearchMusicRepository
import taiwan.no.one.feat.ranking.data.stores.LocalStore
import taiwan.no.one.feat.ranking.data.stores.RemoteStore
import taiwan.no.one.feat.ranking.domain.repositories.SearchMusicRepo

internal object DataModules : ModuleProvider {
    override fun provide(context: Context) = Kodein.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<LocalStore>() with singleton { LocalStore(instance(), instance()) }
        bind<RemoteStore>() with singleton { RemoteStore(instance()) }

        bind<SearchMusicRepo>() with singleton { SearchMusicRepository(instance(), instance()) }
    }

    private fun localProvide() = Kodein.Module("${FEAT_NAME}LocalModule") {
        bind<BankDatabase>() with singleton { BankDatabase.getDatabase(instance()) }

        bind<DummyFile>() with singleton { DummyFile(instance()) }
        bind<DummyDao>() with singleton { instance<BankDatabase>().createDummyDao() }
    }

    private fun remoteProvide(context: Context) = Kodein.Module("${FEAT_NAME}RemoteModule") {
        bind<SeekerConfig>() with instance(RestfulApiFactory().createSeekerConfig())

        bind<Retrofit>(Constant.TAG_FEAT_RANKING_RETROFIT) with singleton {
            DefaultRetrofitConfig(context, instance<SeekerConfig>().apiBaseUrl).provideRetrofitBuilder().build()
        }

        bind<SeekerBankService>() with singleton {
            instance<Retrofit>(Constant.TAG_FEAT_RANKING_RETROFIT).create(SeekerBankService::class.java)
        }
    }
}

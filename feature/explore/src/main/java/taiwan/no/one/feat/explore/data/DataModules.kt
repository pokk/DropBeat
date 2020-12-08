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

package taiwan.no.one.feat.explore.data

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import taiwan.no.one.core.data.remote.DefaultRetrofitConfig
import taiwan.no.one.dropbeat.di.Constant
import taiwan.no.one.dropbeat.di.Constant.TAG_FEAT_REPO_SHARED_PREFS
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.explore.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.explore.data.contracts.DataStore
import taiwan.no.one.feat.explore.data.remote.RestfulApiFactory
import taiwan.no.one.feat.explore.data.remote.configs.LastFmConfig
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmExtraImpl
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmExtraService
import taiwan.no.one.feat.explore.data.remote.services.retrofit.v1.LastFmService
import taiwan.no.one.feat.explore.data.repositories.LastFmExtraRepository
import taiwan.no.one.feat.explore.data.repositories.LastFmRepository
import taiwan.no.one.feat.explore.data.stores.LocalStore
import taiwan.no.one.feat.explore.data.stores.RemoteStore
import taiwan.no.one.feat.explore.domain.repositories.LastFmExtraRepo
import taiwan.no.one.feat.explore.domain.repositories.LastFmRepo

internal object DataModules : ModuleProvider {
    private const val TAG_LOCAL_DATA_STORE = "$FEAT_NAME local data store"
    private const val TAG_REMOTE_DATA_STORE = "$FEAT_NAME remote data store"

    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<DataStore>(TAG_LOCAL_DATA_STORE) with singleton { LocalStore(instance()) }
        bind<DataStore>(TAG_REMOTE_DATA_STORE) with singleton { RemoteStore(instance(), instance(), context) }

        bind<LastFmRepo>() with singleton {
            LastFmRepository(instance(TAG_LOCAL_DATA_STORE),
                             instance(TAG_REMOTE_DATA_STORE),
                             instance(TAG_FEAT_REPO_SHARED_PREFS))
        }
        bind<LastFmExtraRepo>() with singleton {
            LastFmExtraRepository(instance(TAG_LOCAL_DATA_STORE),
                                  instance(TAG_REMOTE_DATA_STORE),
                                  instance(TAG_FEAT_REPO_SHARED_PREFS))
        }
    }

    private fun localProvide() = DI.Module("${FEAT_NAME}LocalModule") {
    }

    private fun remoteProvide(context: Context) = DI.Module("${FEAT_NAME}RemoteModule") {
        bind<LastFmConfig>() with instance(RestfulApiFactory().createLastFmConfig())

        bind<Retrofit>(Constant.TAG_FEAT_EXPLORE_RETROFIT) with singleton {
            DefaultRetrofitConfig(context, instance<LastFmConfig>().apiBaseUrl).provideRetrofitBuilder().build()
        }

        bind<LastFmService>() with singleton {
            instance<Retrofit>(Constant.TAG_FEAT_EXPLORE_RETROFIT).create(LastFmService::class.java)
        }
        bind<LastFmExtraService>() with singleton { LastFmExtraImpl() }
    }
}

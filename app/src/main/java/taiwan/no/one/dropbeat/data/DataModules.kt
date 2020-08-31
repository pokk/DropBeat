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

package taiwan.no.one.dropbeat.data

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.BuildConfig
import taiwan.no.one.dropbeat.data.contracts.DataStore
import taiwan.no.one.dropbeat.data.local.services.PrivacyService
import taiwan.no.one.dropbeat.data.local.services.v1.MmkvService
import taiwan.no.one.dropbeat.data.repositories.PrivacyRepository
import taiwan.no.one.dropbeat.data.stores.LocalStore
import taiwan.no.one.dropbeat.data.stores.RemoteStore
import taiwan.no.one.dropbeat.domain.repositories.PrivacyRepo
import taiwan.no.one.dropbeat.provider.ModuleProvider

internal object DataModules : ModuleProvider {
    private const val TAG_LOCAL_DATA_STORE = "${BuildConfig.APPLICATION_ID} local data store"
    private const val TAG_REMOTE_DATA_STORE = "${BuildConfig.APPLICATION_ID} remote data store"

    override fun provide(context: Context) = DI.Module("${BuildConfig.APPLICATION_ID} DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<DataStore>(TAG_LOCAL_DATA_STORE) with singleton { LocalStore(instance()) }
        bind<DataStore>(TAG_REMOTE_DATA_STORE) with singleton { RemoteStore() }
    }

    private fun localProvide() = DI.Module("${BuildConfig.APPLICATION_ID}LocalModule") {
        bind<PrivacyRepo>() with singleton {
            PrivacyRepository(instance(TAG_LOCAL_DATA_STORE), instance(TAG_REMOTE_DATA_STORE))
        }
        bind<PrivacyService>() with singleton { MmkvService(instance(), instance()) }
    }

    private fun remoteProvide(context: Context) = DI.Module("${BuildConfig.APPLICATION_ID}RemoteModule") {
    }
}

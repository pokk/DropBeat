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

package taiwan.no.one.feat.setting.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.setting.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.setting.data.contracts.DataStore
import taiwan.no.one.feat.setting.data.local.services.SettingPreference
import taiwan.no.one.feat.setting.data.local.services.datastore.v1.SettingDatastore
import taiwan.no.one.feat.setting.data.repositories.SettingRepository
import taiwan.no.one.feat.setting.data.stores.LocalStore
import taiwan.no.one.feat.setting.data.stores.RemoteStore
import taiwan.no.one.feat.setting.domain.repositories.SettingRepo
import androidx.datastore.core.DataStore as AndroidDataStore

internal object DataModules : ModuleProvider {
    private const val TAG_LOCAL_DATA_STORE = "$FEAT_NAME local data store"
    private const val TAG_REMOTE_DATA_STORE = "$FEAT_NAME remote data store"
    private const val TAG_DATASTORE_SETTING = "the datastore of setting"
    private const val NAME_OF_DATASTORE_SETTING = "app_setting"
    private val Context.dataStore: AndroidDataStore<Preferences> by preferencesDataStore(NAME_OF_DATASTORE_SETTING)

    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide(context))
        import(remoteProvide(context))

        bind<DataStore>(TAG_LOCAL_DATA_STORE) with singleton { LocalStore(instance()) }
        bind<DataStore>(TAG_REMOTE_DATA_STORE) with singleton { RemoteStore() }

        bind<SettingRepo>() with singleton {
            SettingRepository(instance(TAG_LOCAL_DATA_STORE), instance(TAG_REMOTE_DATA_STORE))
        }
    }

    private fun localProvide(context: Context) = DI.Module("${FEAT_NAME}LocalModule") {
        bind<AndroidDataStore<Preferences>>(TAG_DATASTORE_SETTING) with singleton { context.dataStore }
        bind<SettingPreference>() with singleton { SettingDatastore(instance(TAG_DATASTORE_SETTING)) }
    }

    private fun remoteProvide(context: Context) = DI.Module("${FEAT_NAME}RemoteModule") {
    }
}

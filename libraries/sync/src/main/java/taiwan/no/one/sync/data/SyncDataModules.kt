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

package taiwan.no.one.sync.data

import android.content.Context
import androidx.datastore.core.DataStore as AndroidDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.sync.data.contracts.DataStore
import taiwan.no.one.sync.data.local.services.TimestampService
import taiwan.no.one.sync.data.local.services.v1.TimeService
import taiwan.no.one.sync.data.remote.services.SyncService
import taiwan.no.one.sync.data.remote.services.firebase.v1.FirebaseSyncService
import taiwan.no.one.sync.data.repositories.SyncRepository
import taiwan.no.one.sync.data.repositories.TimeRepository
import taiwan.no.one.sync.data.stores.LocalStore
import taiwan.no.one.sync.data.stores.RemoteStore
import taiwan.no.one.sync.domain.repositories.SyncRepo
import taiwan.no.one.sync.domain.repositories.TimeRepo

internal object SyncDataModules {
    private const val FEAT_NAME = "Sync"
    private const val TAG_LOCAL_DATA_STORE = "$FEAT_NAME local data store"
    private const val TAG_REMOTE_DATA_STORE = "$FEAT_NAME remote data store"
    private const val TAG_DATASTORE_TIMESTAMP = "the datastore of timestamp"
    private const val NAME_OF_DATASTORE_TIMESTAMP = "timestamp"
    private val Context.dataStore: AndroidDataStore<Preferences> by preferencesDataStore(NAME_OF_DATASTORE_TIMESTAMP)

    fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide(context))
        import(remoteProvide())

        bindSingleton<DataStore>(TAG_LOCAL_DATA_STORE) { LocalStore(instance()) }
        bindSingleton<DataStore>(TAG_REMOTE_DATA_STORE) { RemoteStore(instance()) }

        bindSingleton<SyncRepo> { SyncRepository(instance(TAG_REMOTE_DATA_STORE)) }
        bindSingleton<TimeRepo> { TimeRepository(instance(TAG_LOCAL_DATA_STORE)) }
    }

    private fun localProvide(context: Context) = DI.Module("${FEAT_NAME}LocalModule") {
        bindInstance(TAG_DATASTORE_TIMESTAMP) { context.dataStore }
        bindSingleton<TimestampService> { TimeService(instance(TAG_DATASTORE_TIMESTAMP)) }
    }

    private fun remoteProvide() = DI.Module("${FEAT_NAME}RemoteModule") {
        bindInstance { Firebase.firestore }
        bindSingleton<SyncService> { FirebaseSyncService(instance()) }
    }
}

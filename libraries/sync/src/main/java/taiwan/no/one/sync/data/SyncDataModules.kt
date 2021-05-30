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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.sync.data.contracts.DataStore
import taiwan.no.one.sync.data.remote.services.SyncService
import taiwan.no.one.sync.data.remote.services.firebase.v1.FirebaseSyncService
import taiwan.no.one.sync.data.repositories.SyncRepository
import taiwan.no.one.sync.data.stores.LocalStore
import taiwan.no.one.sync.data.stores.RemoteStore
import taiwan.no.one.sync.domain.repositories.SyncRepo

internal object SyncDataModules {
    private const val FEAT_NAME = "Sync"
    private const val TAG_LOCAL_DATA_STORE = "$FEAT_NAME local data store"
    private const val TAG_REMOTE_DATA_STORE = "$FEAT_NAME remote data store"

    fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide())

        bindSingleton<DataStore>(TAG_LOCAL_DATA_STORE) { LocalStore() }
        bindSingleton<DataStore>(TAG_REMOTE_DATA_STORE) { RemoteStore(instance()) }

        bindSingleton<SyncRepo> {
            SyncRepository(instance(TAG_LOCAL_DATA_STORE), instance(TAG_REMOTE_DATA_STORE))
        }
    }

    private fun localProvide() = DI.Module("${FEAT_NAME}LocalModule") {
    }

    private fun remoteProvide() = DI.Module("${FEAT_NAME}RemoteModule") {
        bindInstance { Firebase.firestore }
        bindSingleton<SyncService> { FirebaseSyncService(instance()) }
    }
}

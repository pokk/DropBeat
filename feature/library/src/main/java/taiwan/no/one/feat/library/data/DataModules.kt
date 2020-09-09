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

package taiwan.no.one.feat.library.data

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.library.FeatModules.Constant.FEAT_NAME
import taiwan.no.one.feat.library.data.contracts.DataStore
import taiwan.no.one.feat.library.data.local.configs.MusicLibraryDatabase
import taiwan.no.one.feat.library.data.local.services.database.v1.PlaylistDao
import taiwan.no.one.feat.library.data.local.services.database.v1.SongDao
import taiwan.no.one.feat.library.data.repositories.PlaylistRepository
import taiwan.no.one.feat.library.data.repositories.SongRepository
import taiwan.no.one.feat.library.data.stores.LocalStore
import taiwan.no.one.feat.library.data.stores.RemoteStore
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal object DataModules : ModuleProvider {
    private const val TAG_LOCAL_DATA_STORE = "$FEAT_NAME local data store"
    private const val TAG_REMOTE_DATA_STORE = "$FEAT_NAME remote data store"

    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<DataStore>(TAG_LOCAL_DATA_STORE) with singleton { LocalStore(instance(), instance()) }
        bind<DataStore>(TAG_REMOTE_DATA_STORE) with singleton { RemoteStore() }

        bind<PlaylistRepo>() with singleton {
            PlaylistRepository(instance(TAG_LOCAL_DATA_STORE), instance(TAG_REMOTE_DATA_STORE))
        }
        bind<SongRepo>() with singleton {
            SongRepository(instance(TAG_LOCAL_DATA_STORE), instance(TAG_REMOTE_DATA_STORE))
        }
    }

    private fun localProvide() = DI.Module("${FEAT_NAME}LocalModule") {
        bind<MusicLibraryDatabase>() with singleton { MusicLibraryDatabase.getDatabase(instance()) }

        bind<PlaylistDao>() with singleton { instance<MusicLibraryDatabase>().createPlaylistDao() }
        bind<SongDao>() with singleton { instance<MusicLibraryDatabase>().createSongDao() }
    }

    private fun remoteProvide(context: Context) = DI.Module("${FEAT_NAME}RemoteModule") {
    }
}

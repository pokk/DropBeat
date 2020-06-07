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
import taiwan.no.one.feat.library.FeatModules.FEAT_NAME
import taiwan.no.one.feat.library.data.local.configs.MusicLibraryDatabase
import taiwan.no.one.feat.library.data.local.services.database.v1.PlaylistDao
import taiwan.no.one.feat.library.data.local.services.database.v1.SongDao
import taiwan.no.one.feat.library.data.repositories.PlaylistRepository
import taiwan.no.one.feat.library.data.stores.LocalStore
import taiwan.no.one.feat.library.data.stores.RemoteStore
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo

internal object DataModules : ModuleProvider {
    override fun provide(context: Context) = DI.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<LocalStore>() with singleton { LocalStore(instance()) }
        bind<RemoteStore>() with singleton { RemoteStore() }

        bind<PlaylistRepo>() with singleton { PlaylistRepository(instance(), instance()) }
    }

    private fun localProvide() = DI.Module("${FEAT_NAME}LocalModule") {
        bind<MusicLibraryDatabase>() with singleton { MusicLibraryDatabase.getDatabase(instance()) }

        bind<PlaylistDao>() with singleton { instance<MusicLibraryDatabase>().createPlaylistDao() }
        bind<SongDao>() with singleton { instance<MusicLibraryDatabase>().createSongDao() }
    }

    private fun remoteProvide(context: Context) = DI.Module("${FEAT_NAME}RemoteModule") {
    }
}

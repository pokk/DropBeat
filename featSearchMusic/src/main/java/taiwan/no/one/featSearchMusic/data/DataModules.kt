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

package taiwan.no.one.featSearchMusic.data

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.featSearchMusic.FeatModules.FEAT_NAME
import taiwan.no.one.featSearchMusic.data.local.configs.BankDatabase
import taiwan.no.one.featSearchMusic.data.local.services.database.v1.DummyDao
import taiwan.no.one.featSearchMusic.data.local.services.database.v1.SearchHistoryDao
import taiwan.no.one.featSearchMusic.data.local.services.json.v1.DummyFile
import taiwan.no.one.featSearchMusic.data.repositories.SearchMusicRepository
import taiwan.no.one.featSearchMusic.data.stores.LocalStore
import taiwan.no.one.featSearchMusic.data.stores.RemoteStore
import taiwan.no.one.featSearchMusic.domain.repositories.SearchMusicRepo

internal object DataModules : ModuleProvider {
    override fun provide(context: Context) = Kodein.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide())

        bind<LocalStore>() with singleton { LocalStore(instance(), instance(), instance()) }
        bind<RemoteStore>() with singleton { RemoteStore(instance()) }

        bind<SearchMusicRepo>() with singleton { SearchMusicRepository(instance(), instance()) }
    }

    private fun localProvide() = Kodein.Module("LocalModule") {
        bind<BankDatabase>() with singleton { BankDatabase.getDatabase(instance()) }

        bind<DummyFile>() with singleton { DummyFile(instance()) }
        bind<DummyDao>() with singleton { instance<BankDatabase>().createDummyDao() }
        bind<SearchHistoryDao>() with singleton { instance<BankDatabase>().createSearchHistoryDao() }
    }

    private fun remoteProvide() = Kodein.Module("RemoteModule") {
    }
}

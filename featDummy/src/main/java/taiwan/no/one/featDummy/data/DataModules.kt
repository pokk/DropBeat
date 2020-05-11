/*
 * MIT License
 *
 * Copyright (c) 2019 SmashKs
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

package taiwan.no.one.featDummy.data

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import taiwan.no.one.featDummy.FeatModules.FEAT_NAME
import taiwan.no.one.featDummy.data.local.configs.DummyDatabase
import taiwan.no.one.featDummy.data.local.services.database.v1.DummyDao
import taiwan.no.one.featDummy.data.local.services.json.v1.DummyFile
import taiwan.no.one.featDummy.data.repository.DummyRepository
import taiwan.no.one.featDummy.data.stores.LocalStore
import taiwan.no.one.featDummy.data.stores.RemoteStore
import taiwan.no.one.featDummy.domain.repository.DummyRepo
import taiwan.no.one.jurassicpark.provider.ModuleProvider

object DataModules : ModuleProvider {
    override fun provide() = Kodein.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide())

        bind<LocalStore>() with singleton { LocalStore(instance(), instance()) }
        bind<RemoteStore>() with singleton { RemoteStore() }

        bind<DummyRepo>() with singleton { DummyRepository(instance(), instance()) }
    }

    private fun localProvide() = Kodein.Module("LocalModule") {
        bind<DummyDatabase>() with singleton { DummyDatabase.getDatabase(instance()) }

        bind<DummyFile>() with singleton { DummyFile(instance()) }
        bind<DummyDao>() with singleton { instance<DummyDatabase>().createDummyDao() }
    }

    private fun remoteProvide() = Kodein.Module("RemoteModule") {
    }
}

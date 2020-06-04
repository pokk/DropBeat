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

package taiwan.no.one.feat.login.data

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import taiwan.no.one.dropbeat.provider.ModuleProvider
import taiwan.no.one.feat.login.FeatModules.FEAT_NAME
import taiwan.no.one.feat.login.data.repositories.RankingRepository
import taiwan.no.one.feat.login.data.stores.LocalStore
import taiwan.no.one.feat.login.data.stores.RemoteStore
import taiwan.no.one.feat.login.domain.repositories.RankingRepo

internal object DataModules : ModuleProvider {
    override fun provide(context: Context) = Kodein.Module("${FEAT_NAME}DataModule") {
        import(localProvide())
        import(remoteProvide(context))

        bind<LocalStore>() with singleton { LocalStore() }
        bind<RemoteStore>() with singleton { RemoteStore() }

        bind<RankingRepo>() with singleton { RankingRepository(instance(), instance()) }
    }

    private fun localProvide() = Kodein.Module("${FEAT_NAME}LocalModule") {
//        bind<RankingDatabase>() with singleton { RankingDatabase.getDatabase(instance()) }

//        bind<RankingDao>() with singleton { instance<RankingDatabase>().createRankingDao() }
    }

    private fun remoteProvide(context: Context) = Kodein.Module("${FEAT_NAME}RemoteModule") {
    }
}

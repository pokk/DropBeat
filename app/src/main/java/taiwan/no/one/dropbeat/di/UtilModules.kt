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

package taiwan.no.one.dropbeat.di

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import taiwan.no.one.analytics.AnalyticsSender
import taiwan.no.one.widget.recyclerviews.listeners.LinearLoadMoreScrollListener
import java.lang.ref.WeakReference

object UtilModules {
    fun provide(context: Context) = DI.Module("Util Module") {
        bind<WorkManager>() with instance(WorkManager.getInstance(context))
        // OPTIMIZE(jieyi): 2018/10/16 We might use Gson for mapping data.
        bind<Gson>() with singleton {
            with(GsonBuilder()) {
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                setLenient()
                create()
            }
        }
        bind<MMKV>() with singleton { MMKV.defaultMMKV() }
    }

    fun provideAnalytics(context: Context) = DI.Module("Analytics Module") {
        bind<FirebaseAnalytics>() with singleton { FirebaseAnalytics.getInstance(context) }

        bind<AnalyticsSender>() with singleton { AnalyticsSender(instance()) }
    }

    fun provideUi() = DI.Module("Util UI Module") {
        // Linear Layout Manager.
        bind<LinearLayoutManager>() with factory { params: LayoutManagerParams ->
            LinearLayoutManager(params.context.get(), params.orientation, params.reverseLayout)
        }
        bind<GridLayoutManager>() with factory { params: LayoutManagerParams ->
            GridLayoutManager(params.context.get(), params.spanCount)
        }
        bind<StaggeredGridLayoutManager>() with factory { params: LayoutManagerParams ->
            StaggeredGridLayoutManager(params.spanCount, params.orientation)
        }
        bind<LinearLoadMoreScrollListener>() with provider { LinearLoadMoreScrollListener() }
    }

    fun provideAll(context: Context) = listOf(provide(context), provideAnalytics(context), provideUi())

    data class LayoutManagerParams(
        val context: WeakReference<Context>,
        val orientation: Int = RecyclerView.VERTICAL,
        val spanCount: Int = 1,
        val reverseLayout: Boolean = false,
    )
}

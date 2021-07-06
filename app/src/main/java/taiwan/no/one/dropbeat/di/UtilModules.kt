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

package taiwan.no.one.dropbeat.di

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.work.WorkManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tencent.mmkv.MMKV
import java.lang.ref.WeakReference
import java.util.Date
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindInstance
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import taiwan.no.one.analytics.AnalyticsSender
import taiwan.no.one.core.data.remote.provider.RetrofitProvider
import taiwan.no.one.dropbeat.data.remote.provider.MoshiRetrofitProvider
import taiwan.no.one.dropbeat.di.Constant.TAG_EDGE_FACTORY_BOUNCED
import taiwan.no.one.dropbeat.di.Constant.TAG_EDGE_FACTORY_NONE
import taiwan.no.one.dropbeat.di.Constant.TAG_NETWORK_MOSHI_RETROFIT
import taiwan.no.one.mediaplayer.lyric.DefaultLrcBuilder
import taiwan.no.one.mediaplayer.lyric.LrcBuilder
import taiwan.no.one.widget.recyclerviews.effectors.BounceEdgeEffectFactory
import taiwan.no.one.widget.recyclerviews.effectors.NoneEdgeEffectFactory
import taiwan.no.one.widget.recyclerviews.listeners.LinearLoadMoreScrollListener

object UtilModules {
    fun provideCommon(context: Context) = DI.Module("Util Module") {
        bindInstance { WorkManager.getInstance(context) }
        // OPTIMIZE(jieyi): 2018/10/16 We might use Gson for mapping data.
        bindInstance<Moshi> {
            Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
        }
        bindInstance { requireNotNull(MMKV.defaultMMKV()) }
        bindInstance<LrcBuilder> { DefaultLrcBuilder() }
    }

    private fun provideNetwork(context: Context) = DI.Module("Common Network Module") {
        bindSingleton<RetrofitProvider>(TAG_NETWORK_MOSHI_RETROFIT) { MoshiRetrofitProvider(instance()) }
    }

    private fun provideAnalytics(context: Context) = DI.Module("Analytics Module") {
        bindInstance { AnalyticsSender(Firebase.analytics) }
    }

    private fun provideUi() = DI.Module("Util UI Module") {
        // Linear Layout Manager.
        bindFactory { params: LayoutManagerParams ->
            LinearLayoutManager(params.context.get(), params.orientation, params.reverseLayout)
        }
        bindFactory { params: LayoutManagerParams ->
            GridLayoutManager(params.context.get(), params.spanCount)
        }
        bindFactory { params: LayoutManagerParams ->
            StaggeredGridLayoutManager(params.spanCount, params.orientation)
        }
        bindProvider { LinearLoadMoreScrollListener() }
        bindProvider<RecyclerView.EdgeEffectFactory>(TAG_EDGE_FACTORY_NONE) { NoneEdgeEffectFactory() }
        bindFactory<Boolean, RecyclerView.EdgeEffectFactory>(TAG_EDGE_FACTORY_BOUNCED) { isVertical ->
            BounceEdgeEffectFactory(isVertical)
        }
    }

    fun provideAll(context: Context) = listOf(
        provideCommon(context),
        provideNetwork(context),
        provideAnalytics(context),
        provideUi()
    )

    data class LayoutManagerParams(
        val context: WeakReference<Context>,
        val orientation: Int = RecyclerView.VERTICAL,
        val spanCount: Int = 1,
        val reverseLayout: Boolean = false,
    )
}

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

package taiwan.no.one.dropbeat.presentation.services.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import taiwan.no.one.dropbeat.di.FeatModuleHelper
import taiwan.no.one.dropbeat.di.UtilModules
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.KEY_EXCEPTION
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_KEY_RESULT_OF_SONGS
import taiwan.no.one.dropbeat.presentation.services.workers.WorkerConstant.PARAM_TAG_OF_NAME
import taiwan.no.one.dropbeat.provider.ExploreMethodsProvider
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.entity.SimpleTrackEntity

internal class GetSongsOfTagWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), DIAware {
    override val di by DI.lazy {
        import(UtilModules.provideCommon(context))
        import(FeatModuleHelper.provide())
    }
    private val moshi by instance<Moshi>()
    private val exploreProvider by instance<ExploreMethodsProvider>()
    private val libraryProvider by instance<LibraryMethodsProvider>()

    override suspend fun doWork(): Result {
        val tagName = inputData.getString(PARAM_TAG_OF_NAME)
        val data = Data.Builder()
        if (tagName.isNullOrBlank()) {
            return Result.failure(data.putString(KEY_EXCEPTION, "parameters aren't found").build())
        }
        return try {
            val entities = exploreProvider.getTopTracksOfTag(tagName).onEach {
                val isFavorite = libraryProvider.isFavoriteTrack(it.uri).getOrNull() ?: false
                it.isFavorite = isFavorite
            }
            val type = Types.newParameterizedType(List::class.java, SimpleTrackEntity::class.java)
            val json = moshi.adapter<List<SimpleTrackEntity>>(type).toJson(entities)
            Result.success(data.putString(PARAM_KEY_RESULT_OF_SONGS, json).build())
        }
        catch (e: Exception) {
            Result.failure(data.putString(KEY_EXCEPTION, e.message).build())
        }
    }
}

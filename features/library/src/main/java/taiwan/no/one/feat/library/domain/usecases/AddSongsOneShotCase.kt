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

package taiwan.no.one.feat.library.domain.usecases

import com.devrapid.kotlinknifer.loge
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.domain.repositories.SongRepo

internal class AddSongsOneShotCase(
    private val repository: SongRepo,
) : AddSongsCase(), DIAware {
    override val di by closestDI(DropBeatApp.appContext)

    override suspend fun acquireCase(parameter: Request?) = parameter.ensure {
        val list = songs ?: run {
            val moshi by instance<Moshi>()
            try {
                val type = Types.newParameterizedType(List::class.java, SongEntity::class.java)
                moshi.adapter<List<SongEntity>>(type).fromJson(songsStream.orEmpty()).orEmpty()
            }
            catch (e: Exception) {
                loge(e)
                return@ensure false
            }
        }
        repository.addMusics(list)
        true
    }

    internal data class Request(val songs: List<SongEntity>? = null, val songsStream: String? = null) : RequestValues
}

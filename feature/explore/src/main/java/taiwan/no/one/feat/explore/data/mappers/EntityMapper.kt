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

package taiwan.no.one.feat.explore.data.mappers

import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.ext.DEFAULT_STR
import taiwan.no.one.feat.explore.data.entities.remote.TrackInfoEntity.TrackEntity

internal object EntityMapper {
    fun exploreToSimpleTrackEntity(entity: TrackEntity) = entity.let {
        SimpleTrackEntity(
            it.name.orEmpty(),
            it.artist?.name.orEmpty(),
            it.url.orEmpty(),
            DEFAULT_STR,
            it.images?.first()?.text.orEmpty(),
            DEFAULT_STR,
            it.duration?.toInt() ?: 0,
            false,
            false,
        )
    }
}
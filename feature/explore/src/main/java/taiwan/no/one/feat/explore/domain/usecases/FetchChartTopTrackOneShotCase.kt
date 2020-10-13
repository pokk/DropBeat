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

package taiwan.no.one.feat.explore.domain.usecases

import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.feat.explore.domain.repositories.LastFmExtraRepo
import taiwan.no.one.feat.explore.domain.repositories.LastFmRepo

internal class FetchChartTopTrackOneShotCase(
    private val repository: LastFmRepo,
    private val extraRepo: LastFmExtraRepo,
) : FetchChartTopTrackCase() {
    override suspend fun acquireCase(parameter: FetchChartTopTrackReq?) = parameter.ensure {
        repository.fetchChartTopTrack(page, limit).apply {
            // Override the tracks
            tracks.mapIndexed { index, trackEntity ->
                takeIf { index < moreDetailRange }
                    ?.takeIf { trackEntity.images?.any { it.text == "cover" } ?: false }
                    ?.let { trackEntity.url }
                    ?.let { extraRepo.fetchTrackCover(it, trackEntity) } ?: trackEntity
            }.apply {
                // Save back again.
                repository.addChartTopTrack(page, limit, this)
            }
        }
    }

    internal data class Request(val page: Int, val limit: Int, val moreDetailRange: Int = 0) : RequestValues
}

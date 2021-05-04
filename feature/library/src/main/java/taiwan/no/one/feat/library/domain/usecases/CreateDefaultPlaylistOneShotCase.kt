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

package taiwan.no.one.feat.library.domain.usecases

import taiwan.no.one.core.data.extensions.parseObjectFromJson
import taiwan.no.one.core.domain.usecase.Usecase.RequestValues
import taiwan.no.one.dropbeat.AppResDrawable
import taiwan.no.one.dropbeat.DropBeatApp
import taiwan.no.one.dropbeat.core.helpers.ResourceHelper
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.domain.repositories.PlaylistRepo

internal class CreateDefaultPlaylistOneShotCase(
    private val repository: PlaylistRepo,
) : CreateDefaultPlaylistCase() {
    val defaultPlaylistUrl = listOf(
        AppResDrawable.bg_downloaded, AppResDrawable.bg_favorite, AppResDrawable.bg_uncategory
    ).map(ResourceHelper::getUriForDrawableResource)

    override suspend fun acquireCase(parameter: Request?): Boolean {
        val list = DropBeatApp.appContext
            .parseObjectFromJson<List<PlayListEntity>>("json/default_playlist.json")
            ?: throw Exception()
        val ids = list.map(PlayListEntity::id)
        val names = list.map(PlayListEntity::name)
        ids.zip(names)
            .zip(defaultPlaylistUrl)
            .map { (basicInfo, uri) ->
                val (id, name) = basicInfo
                PlayListEntity(id, name, coverUrl = uri.toString())
            }
            .forEach { repository.addPlaylist(it, true) }
        return true
    }

    internal class Request : RequestValues
}

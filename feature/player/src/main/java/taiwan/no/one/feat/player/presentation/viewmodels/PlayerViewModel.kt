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

package taiwan.no.one.feat.player.presentation.viewmodels

import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindViewModel
import taiwan.no.one.dropbeat.data.entities.SimplePlaylistEntity
import taiwan.no.one.dropbeat.di.FeatModuleHelper
import taiwan.no.one.ktx.livedata.toLiveData
import taiwan.no.one.mediaplayer.MusicInfo

internal class PlayerViewModel : BehindViewModel() {
    private val libraryProvider get() = FeatModuleHelper.methodsProvider()
    private val _playlists by lazy { ResultLiveData<List<SimplePlaylistEntity>>() }
    val playlists = _playlists.toLiveData()
    private val _isFavorite by lazy { ResultLiveData<Boolean>() }
    val isFavorite = _isFavorite.toLiveData()

    fun download(uri: String) = launchBehind { libraryProvider.downloadTrack(uri) }

    fun addFavorite(music: MusicInfo) = launchBehind {}

    fun removeFavorite() = launchBehind {}

    fun isFavorite(uri: String) = launchBehind { _isFavorite.postValue(libraryProvider.isFavoriteTrack(uri, 2)) }

    fun getPlaylists() = launchBehind { _playlists.postValue(libraryProvider.getPlaylists()) }

    fun createPlaylist(name: String) = launchBehind { libraryProvider.createPlaylist(name) }

    fun addSongToPlaylist(music: MusicInfo, playlistId: Int) =
        launchBehind { libraryProvider.addSongToPlaylist(music, playlistId) }
}

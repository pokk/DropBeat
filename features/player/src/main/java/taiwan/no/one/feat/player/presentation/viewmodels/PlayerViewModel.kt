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

package taiwan.no.one.feat.player.presentation.viewmodels

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import org.kodein.di.instance
import taiwan.no.one.core.presentation.viewmodel.ResultLiveData
import taiwan.no.one.dropbeat.core.viewmodel.BehindAndroidViewModel
import taiwan.no.one.dropbeat.provider.LibraryMethodsProvider
import taiwan.no.one.entity.SimplePlaylistEntity
import taiwan.no.one.feat.player.presentation.recyclerviews.states.LrcState
import taiwan.no.one.ktx.livedata.toLiveData
import taiwan.no.one.mediaplayer.MusicInfo
import taiwan.no.one.mediaplayer.lyric.LrcBuilder
import taiwan.no.one.mediaplayer.lyric.LrcRowEntity

internal class PlayerViewModel(
    application: Application,
    override val handle: SavedStateHandle,
) : BehindAndroidViewModel(application) {
    private val libraryProvider by instance<LibraryMethodsProvider>()
    private val lrcBuilder by instance<LrcBuilder>()
    private val _playlists by lazy { ResultLiveData<List<SimplePlaylistEntity>>() }
    val playlists get() = _playlists.toLiveData()
    private val _isFavorite by lazy { ResultLiveData<Boolean>() }
    val isFavorite get() = _isFavorite.toLiveData()
    private val _lrcRowEntities by lazy { ResultLiveData<List<LrcRowEntity>>() }
    val lrcRowEntities get() = _lrcRowEntities.toLiveData()
    // OPTIMIZE(jieyi): 7/15/21 the time accuracy is not perfect correct, should have in ms.
    var lrcMapper = IntArray(0)
        private set

    fun download(uri: String) = launchBehind { libraryProvider.downloadTrack(uri) }

    fun addFavorite(music: MusicInfo) = launchBehind {}

    fun removeFavorite() = launchBehind {}

    fun isFavorite(uri: String) = launchBehind {
        _isFavorite.postValue(libraryProvider.isFavoriteTrack(uri))
    }

    fun getPlaylists() = launchBehind { _playlists.postValue(libraryProvider.getPlaylists()) }

    fun createPlaylist(name: String) = launchBehind { libraryProvider.createPlaylist(name) }

    fun addSongToPlaylist(music: MusicInfo, playlistId: Int) = launchBehind {
        libraryProvider.addSongToPlaylist(music, playlistId)
    }

    fun getLyrics(lyricUrl: String) = launchBehind {
        _lrcRowEntities.postValue(libraryProvider.getLyric(1).map {
            lrcBuilder.getLrcRows(it)
                .toMutableList()
                // To be able to scroll the first and the last to the middle of recyclerview, we
                // Have to add two more extra entities.
                .apply {
                    // Add a dummy lyric entity to the top for an extra space.
                    add(0, LrcRowEntity(null, 0, null))
                    // Add a dummy lyric entity to the bottom for an extra space.
                    add(LrcRowEntity(null, 0, null))
                }
                .apply { createLrcTimeMapper(this) }
        })
    }

    @MainThread
    fun createLyricStates(lyricEntities: List<LrcRowEntity>, halfHeightOfRecyclerView: Int) =
        (0..lyricEntities.size).map {
            if (it == 0 || it == lyricEntities.size - 1) {
                LrcState.DummyState(halfHeightOfRecyclerView)
            }
            else if (it == 1) {
                LrcState.HighlightState(1)
            }
            else {
                LrcState.NoFocusedState(-1)
            }
        }

    private fun createLrcTimeMapper(lyricEntities: List<LrcRowEntity>) {
        // Minus 2 is for the dummy lrc entities.
        // Add an extra space for rounding the last number.
        lrcMapper = IntArray((lyricEntities[lyricEntities.size - 2].time / 1000).toInt() + 1)
        var index = 0
        lyricEntities.forEachIndexed { i, entity ->
            if (entity.time == 0L) return@forEachIndexed
            val bound = (entity.time / 1000).toInt()
            while (index < bound) {
                lrcMapper[index++] = i - 1
            }
        }
    }
}

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

package taiwan.no.one.feat.library.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.devrapid.kotlinknifer.logd
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.SongEntity
import taiwan.no.one.feat.library.databinding.FragmentMyPageBinding
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel
import taiwan.no.one.ktx.livedata.obs

internal class PlaylistFragment : BaseFragment<BaseActivity<*>, FragmentMyPageBinding>() {
    private val vm by viewModels<PlaylistViewModel>()

    override fun bindLiveData() {
        vm.resPlaylist.obs(this) {
            it.onSuccess {
                logw(it)
            }.onFailure {
                loge(it)
            }
        }
        vm.playlist.obs(this) {
            it.onSuccess {
                logd(it)
            }.onFailure {
                loge(it)
            }
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        val playlist = PlayListEntity(0, "playlist1", emptyList(), 3)
        val musics = listOf(
            SongEntity(1, "song1", "artist1", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(2, "song2", "artist2", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(3, "song3", "artist3", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(4, "song4", "artist4", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(5, "song5", "artist5", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(6, "song6", "artist6", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(7, "song7", "artist7", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(8, "song8", "artist8", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(9, "song9", "artist9", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
            SongEntity(10, "song10", "artist10", "xxxxxxxxxxxxxxx", "xxxxxxxxxxxxx", 100),
        )
//        vm.createPlaylist(playlist)
//        vm.createSongs(musics)
    }
}

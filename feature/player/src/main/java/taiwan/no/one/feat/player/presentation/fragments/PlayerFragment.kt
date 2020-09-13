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

package taiwan.no.one.feat.player.presentation.fragments

import android.os.Bundle
import android.view.Gravity
import com.devrapid.kotlinknifer.logw
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.player.databinding.FragmentPlayerBinding
import taiwan.no.one.mediaplayer.MusicInfo
import taiwan.no.one.mediaplayer.SimpleMusicPlayer

internal class PlayerFragment : BaseFragment<BaseActivity<*>, FragmentPlayerBinding>() {
    val player = SimpleMusicPlayer.getInstance()

    override fun componentListenersBinding() {
//        binding.btnPlay.setOnClickListener { player.play() }
//        binding.btnClear.setOnClickListener { player.clearPlaylist() }
//        binding.btnStop.setOnClickListener { player.stop() }
//        binding.btnNext.setOnClickListener { player.next() }
//        binding.btnPrevious.setOnClickListener { player.previous() }
//        binding.btnPause.setOnClickListener { player.pause() }
//        binding.btnShuffle.setOnClickListener { player.mode = Shuffle }
//        binding.btnRepeatAll.setOnClickListener { player.mode = RepeatAll }
//        binding.btnRepeatOne.setOnClickListener { player.mode = RepeatOne }
//        binding.btnCurrent.setOnClickListener {
//            Toast.makeText(requireActivity(), player.curPlayingInfo?.title.toString(), Toast.LENGTH_SHORT).show()
//        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        logw(Gravity.CENTER)

        val playlist = listOf(
            MusicInfo("title1",
                      "artist1",
                      "http://cdn.musicappserver.com/music/1d/2b52438d2f91cb61814dff8a1c73a8.mp3",
                      983),
            MusicInfo("title2",
                      "artist2",
                      "http://cdn.musicappserver.com/music/b1/4acbbb3567c3c35b33305a07dc693c.mp3",
                      224),
            MusicInfo("title3",
                      "artist3",
                      "http://cdn.musicappserver.com/music/af/c0dda7cfc27778575f9c4abcb4604e.mp3",
                      360),
            MusicInfo("title4",
                      "artist4",
                      "http://cdn.musicappserver.com/music/29/b311e13f3cff6d3b23eb151038c745.mp3",
                      368),
        )
        player.replacePlaylist(playlist)
    }
}

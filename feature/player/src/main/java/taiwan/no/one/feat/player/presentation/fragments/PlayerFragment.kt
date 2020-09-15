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

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.devrapid.kotlinknifer.getDrawable
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.google.android.material.slider.Slider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.core.helpers.StringUtil
import taiwan.no.one.feat.player.R.drawable
import taiwan.no.one.feat.player.databinding.FragmentPlayerBinding
import taiwan.no.one.feat.player.databinding.MergePlayerControllerBinding
import taiwan.no.one.mediaplayer.MusicInfo
import taiwan.no.one.mediaplayer.SimpleMusicPlayer
import taiwan.no.one.mediaplayer.exceptions.PlaybackException
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode
import taiwan.no.one.mediaplayer.interfaces.PlayerCallback

internal class PlayerFragment : BaseFragment<BaseActivity<*>, FragmentPlayerBinding>() {
    private var isTouchingSlider = false
    private val merge get() = MergePlayerControllerBinding.bind(binding.root)
    private val isPlaying get() = player.isPlaying
    private val playerCallback = object : PlayerCallback {
        override fun onTrackChanged(music: MusicInfo) {
            logw(music)
            merge.apply {
                logw(player.curDuration)
                mtvDuration.text = StringUtil.buildDurationToDigitalTime(music.duration.toLong())
                mtvCurrentTime.text = StringUtil.buildDurationToDigitalTime(0L)
            }
        }

        override fun onPlayState(isPlaying: Boolean) {
            // Change the icon.
            switchPlayIcon()
        }

        override fun onTrackCurrentPosition(second: Long) {
            merge.apply {
                if (!isTouchingSlider) {
                    mtvCurrentTime.text = StringUtil.buildDurationToDigitalTime(second)
                    setProgress(second / player.curDuration.toFloat())
                }
            }
        }

        override fun onErrorCallback(error: PlaybackException) {
            loge(error)
        }
    }
    private val sliderTouchListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
            isTouchingSlider = true
        }

        override fun onStopTrackingTouch(slider: Slider) {
            player.seekTo((slider.value * player.curDuration).toInt())
            isTouchingSlider = false
        }
    }
    private val seekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar) {
            isTouchingSlider = true
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            player.seekTo((seekBar.progress / 100f * player.curDuration).toInt())
            isTouchingSlider = false
        }

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (!isTouchingSlider) return
            merge.mtvCurrentTime.text =
                StringUtil.buildDurationToDigitalTime((progress * player.curDuration / 100f).toLong())
        }
    }
    val player = SimpleMusicPlayer.getInstance()
    val playlist = listOf(
        MusicInfo("title1",
                  "artist1",
                  "http://cdn.musicappserver.com/music/1d/2b52438d2f91cb61814dff8a1c73a8.mp3",
                  196),
        MusicInfo("title2",
                  "artist2",
                  "http://cdn.musicappserver.com/music/b1/4acbbb3567c3c35b33305a07dc693c.mp3",
                  335),
        MusicInfo("title3",
                  "artist3",
                  "http://cdn.musicappserver.com/music/af/c0dda7cfc27778575f9c4abcb4604e.mp3",
                  226),
        MusicInfo("title4",
                  "artist4",
                  "http://cdn.musicappserver.com/music/29/b311e13f3cff6d3b23eb151038c745.mp3",
                  183),
    )

    init {
        player.replacePlaylist(playlist)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.setPlayerEventCallback(null)
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        binding.apply {
            (player.curPlayingInfo ?: playlist.first()).also {
                mtvArtist.text = it.artist
                mtvTrack.text = it.title
            }
            merge.mtvDuration.text = StringUtil.buildDurationToDigitalTime(player.curDuration)
            merge.mtvCurrentTime.text = StringUtil.buildDurationToDigitalTime(player.curTrackSec)
            setProgress(player.curTrackSec / player.curDuration.toFloat())
        }
        switchPlayIcon()
    }

    override fun componentListenersBinding() {
        binding.apply {
            btnMiniPlay.setOnClickListener { handlePlayAction() }
            btnMiniNext.setOnClickListener { player.next() }
            btnMiniOption.setOnClickListener { player.mode = Mode.Shuffle }
            sivAlbum.setOnClickListener {
                logw("??????????????????????????????????????????")
            }
            sliderMiniProgress.clearOnSliderTouchListeners()
            sliderMiniProgress.addOnSliderTouchListener(sliderTouchListener)
        }
        merge.apply {
            btnFavorite.setOnClickListener { handleFavorite() }
            btnVideo.setOnClickListener {}
            btnPlay.setOnClickListener { handlePlayAction() }
            btnNext.setOnClickListener { player.next() }
            btnPrevious.setOnClickListener { player.previous() }
            btnShuffle.setOnClickListener { player.mode = Mode.Shuffle }
            btnRepeat.setOnClickListener { player.mode = Mode.RepeatAll }
            sliderMusic.setOnSeekBarChangeListener(seekBarChangeListener)
        }
        player.setPlayerEventCallback(playerCallback)
    }

    private fun switchPlayIcon() {
        listOf(merge.btnPlay, binding.btnMiniPlay).forEach { btn ->
            btn.icon = getDrawable(if (isPlaying) drawable.ic_pause else drawable.ic_play)
        }
    }

    private fun handlePlayAction() {
        // Switch the player action.
        if (isPlaying) player.pause() else player.play()
    }

    private fun handleFavorite() {
    }

    private fun setProgress(progress: Float) {
        merge.sliderMusic.progress = (progress * 100).toInt()
        binding.sliderMiniProgress.value = progress
    }
}

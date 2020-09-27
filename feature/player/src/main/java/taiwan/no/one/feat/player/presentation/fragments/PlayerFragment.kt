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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.loadAny
import com.devrapid.kotlinknifer.getDrawable
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.devrapid.kotlinknifer.ofAlpha
import com.google.android.material.slider.Slider
import org.kodein.di.provider
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseDialogFragment
import taiwan.no.one.dropbeat.core.helpers.StringUtil
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.R.drawable
import taiwan.no.one.feat.player.databinding.FragmentPlayerBinding
import taiwan.no.one.feat.player.databinding.MergePlayerControllerBinding
import taiwan.no.one.feat.player.presentation.popups.PlaylistPopupWindow
import taiwan.no.one.feat.player.presentation.recyclerviews.adapters.PlaylistAdapter
import taiwan.no.one.feat.player.presentation.viewmodels.PlayerViewModel
import taiwan.no.one.mediaplayer.MusicInfo
import taiwan.no.one.mediaplayer.SimpleMusicPlayer
import taiwan.no.one.mediaplayer.exceptions.PlaybackException
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode
import taiwan.no.one.mediaplayer.interfaces.PlayerCallback
import taiwan.no.one.widget.popupmenu.popupMenuWithIcon
import java.lang.ref.WeakReference

internal class PlayerFragment : BaseDialogFragment<BaseActivity<*>, FragmentPlayerBinding>() {
    private var isTouchingSlider = false
    private var isRunningAnim = false
    private val vm by viewModels<PlayerViewModel>()
    private val merge get() = MergePlayerControllerBinding.bind(binding.root)
    private val isPlaying get() = player.isPlaying
    private val linearLayoutManager: () -> LinearLayoutManager by provider {
        LayoutManagerParams(WeakReference(requireActivity()))
    }
    private val playerCallback = object : PlayerCallback {
        override fun onTrackChanged(music: MusicInfo) {
            logw(music)
            setMusicInfo(music)
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
        MusicInfo(
            "title1",
            "artist1",
            "http://cdn.musicappserver.com/music/1d/2b52438d2f91cb61814dff8a1c73a8.mp3",
            196,
            "https://cdn.musicappserver.com/image/88/ac2349d19d307f5c0f2228fa746cac.jpg",
            "",
        ),
        MusicInfo(
            "title2",
            "artist2",
            "http://cdn.musicappserver.com/music/b1/4acbbb3567c3c35b33305a07dc693c.mp3",
            335,
            "https://cdn.musicappserver.com/image/b1/3aa841c31193804b81b11be6c364b4.jpg",
            "",
        ),
        MusicInfo(
            "title3",
            "artist3",
            "http://cdn.musicappserver.com/music/af/c0dda7cfc27778575f9c4abcb4604e.mp3",
            226,
            "https://cdn.musicappserver.com/image/1d/2b52438d2f91cb61814dff8a1c73a8.jpg",
            "",
        ),
        MusicInfo(
            "title4",
            "artist4",
            "http://cdn.musicappserver.com/music/29/b311e13f3cff6d3b23eb151038c745.mp3",
            183,
            "https://cdn.musicappserver.com/image/4c/1b5f29b91a5a438b2c424102e8e5ab.jpg",
            "",
        ),
    )

    init {
        player.replacePlaylist(playlist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, taiwan.no.one.dropbeat.R.style.Dialog_FullScreen_Theme)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.setPlayerEventCallback(null)
    }

    override fun viewComponentBinding() {
        super.viewComponentBinding()
        addStatusBarHeightMarginTop(binding.btnClose)
        binding.apply {
            (player.curPlayingInfo ?: playlist.first()).also(this@PlayerFragment::setMusicInfo)
            merge.mtvCurrentTime.text = StringUtil.buildDurationToDigitalTime(player.curTrackSec)
            setProgress(player.curTrackSec / player.curDuration.toFloat())
        }
        switchPlayIcon()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun componentListenersBinding() {
        binding.apply {
            root.addTransitionListener(object : TransitionAdapter() {
                override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
                    isRunningAnim = true
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    isRunningAnim = false
                }
            })
            btnClose.setOnClickListener { collapsePlayer() }
            btnOption.setOnClickListener { popupMenu(binding.btnOption) }
            btnMiniPlay.setOnClickListener { handlePlayAction() }
            btnMiniNext.setOnClickListener { player.next() }
            btnMiniOption.setOnClickListener { player.mode = Mode.Shuffle }
            sliderMiniProgress.clearOnSliderTouchListeners()
            sliderMiniProgress.addOnSliderTouchListener(sliderTouchListener)
            sivAlbum.setOnClickListener { expandLyrics() }
            sivLyrics.setOnClickListener { collapseLyrics() }
        }
        merge.apply {
            btnFavorite.setOnClickListener { handleFavorite() }
            btnVideo.setOnClickListener {}
            btnPlay.setOnClickListener { handlePlayAction() }
            btnNext.setOnClickListener { player.next() }
            btnPrevious.setOnClickListener { player.previous() }
            btnShuffle.setOnClickListener { player.mode = Mode.Shuffle }
            btnRepeat.setOnClickListener { player.mode = Mode.RepeatAll }
            btnAddPlaylist.setOnClickListener { popupPlaylist() }
            sliderMusic.setOnSeekBarChangeListener(seekBarChangeListener)
        }
        player.setPlayerEventCallback(playerCallback)
    }

    override fun rendered(savedInstanceState: Bundle?) {
        requireDialog().window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // For not opaque(transparent) color.
            statusBarColor = 0.ofAlpha(0f)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        vm.getPlaylists()
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

    private fun popupPlaylist() = PlaylistPopupWindow(requireActivity()).builder {
        it.rvPlaylist.apply {
            adapter = PlaylistAdapter(vm.playlists.value?.getOrNull().orEmpty()).apply {
                setOnClickListener { playlist ->
                    player.curPlayingInfo?.let { music -> vm.addSongToPlaylist(music, playlist.id) }
                }
            }
            layoutManager = linearLayoutManager()
        }
    }.anchorOn(merge.btnAddPlaylist).popup()

    private fun popupMenu(anchor: View) = popupMenuWithIcon(requireActivity(), anchor, R.menu.menu_more).show()

    private fun setMusicInfo(music: MusicInfo) {
        binding.apply {
            sivAlbum.loadAny(music.thumbUri.takeIf { it.isNotBlank() } ?: "")
            mtvArtist.text = music.artist
            mtvTrack.text = music.title
        }
        merge.apply {
            mtvDuration.text = StringUtil.buildDurationToDigitalTime(music.duration.toLong())
            mtvCurrentTime.text = StringUtil.buildDurationToDigitalTime(0L)
        }
    }

    private fun setProgress(progress: Float) {
        merge.sliderMusic.progress = (progress * 100).toInt()
        binding.sliderMiniProgress.value = progress
    }

    private fun collapseLyrics() {
        if (isRunningAnim) return
        binding.root.apply {
            setTransition(R.id.transition_expand_lyric)
            transitionToStart()
        }
    }

    private fun expandLyrics() {
        if (isRunningAnim || binding.root.currentState == R.id.mini_player_end) return
        binding.root.apply {
            setTransition(R.id.transition_expand_lyric)
            transitionToEnd()
        }
    }

    private fun collapsePlayer() {
        if (isRunningAnim) return
        binding.root.apply {
            setTransition(currentState, R.id.mini_player_end)
            transitionToEnd()
        }
    }
}

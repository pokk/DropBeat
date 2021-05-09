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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.loadAny
import com.devrapid.kotlinknifer.displayMetrics
import com.devrapid.kotlinknifer.getDimen
import com.devrapid.kotlinknifer.getDrawable
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinknifer.logw
import com.google.android.material.slider.Slider
import java.lang.ref.WeakReference
import org.kodein.di.provider
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.dropbeat.core.utils.StringUtil
import taiwan.no.one.dropbeat.di.UtilModules.LayoutManagerParams
import taiwan.no.one.dropbeat.presentation.activities.MainActivity
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.databinding.FragmentPlayerBinding
import taiwan.no.one.feat.player.databinding.MergePlayerControllerBinding
import taiwan.no.one.feat.player.presentation.popups.PlaylistPopupWindow
import taiwan.no.one.feat.player.presentation.popups.SettingPopupWindow
import taiwan.no.one.feat.player.presentation.recyclerviews.adapters.PlaylistAdapter
import taiwan.no.one.feat.player.presentation.recyclerviews.decorators.PlaylistItemDecorator
import taiwan.no.one.feat.player.presentation.viewmodels.PlayerViewModel
import taiwan.no.one.mediaplayer.MusicInfo
import taiwan.no.one.mediaplayer.SimpleMusicPlayer
import taiwan.no.one.mediaplayer.exceptions.PlaybackException
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode
import taiwan.no.one.mediaplayer.interfaces.PlayerCallback
import taiwan.no.one.widget.WidgetResDimen

internal class PlayerFragment : BaseFragment<MainActivity, FragmentPlayerBinding>() {
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
            "http://cdn.nilsonstorage.com/music/1b/8f3e854275abf92aadffe4548d4ba3.mp3",
            196,
            "http://cdn.nilsonstorage.com/image/1b/8f3e854275abf92aadffe4548d4ba3.jpg",
            "",
        ),
        MusicInfo(
            "title2",
            "artist2",
            "http://cdn.nilsonstorage.com/music/d9/1e448054cfcc9eab9c18fe256efc38.mp3",
            335,
            "http://cdn.nilsonstorage.com/image/d9/1e448054cfcc9eab9c18fe256efc38.jpg",
            "",
        ),
    )

    init {
        player.replacePlaylist(playlist)
    }

    //region Fragment Lifecycle
    override fun onDestroy() {
        super.onDestroy()
        player.setPlayerEventCallback(null)
    }
    //endregion

    override fun viewComponentBinding() {
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
                val displaySize = requireContext().displayMetrics()
                val statusHeight = getStatusBarHeight()

                override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
                    isRunningAnim = true
                    parent.updatePlayerFragmentHeight(displaySize.heightPixels + statusHeight)
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    isRunningAnim = false
                    when (currentId) {
                        R.id.mini_player_start ->
                            parent.updatePlayerFragmentHeight(displaySize.heightPixels + statusHeight)
                        R.id.mini_player_end ->
                            parent.updatePlayerFragmentHeight(
                                getDimen(WidgetResDimen.md_eight_unit).toInt() * 2 +
                                    getDimen(WidgetResDimen.one_dp).toInt()
                            )
                        else -> Unit
                    }
                }
            })
            btnClose.setOnClickListener { collapsePlayer() }
            btnOption.setOnClickListener { popupMenu(binding.btnOption) }
            btnMiniPlay.setOnClickListener { handlePlayAction() }
            btnMiniNext.setOnClickListener { player.next() }
            btnMiniOption.setOnClickListener { player.mode = Mode.Shuffle }
            sliderMiniProgress.clearOnSliderTouchListeners()
            sliderMiniProgress.addOnSliderTouchListener(sliderTouchListener)
            sivAlbum.setOnClickListener {
                if (root.currentState == R.id.mini_player_end) {
                    expandPlayer()
                }
                else {
                    expandLyrics()
                }
            }
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
        vm.getPlaylists()
    }

    private fun switchPlayIcon() {
        listOf(merge.btnPlay, binding.btnMiniPlay).forEach { btn ->
            btn.icon = getDrawable(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
        }
    }

    private fun handlePlayAction() {
        // Switch the player action.
        if (isPlaying) player.pause() else player.play()
    }

    private fun handleFavorite() = Unit

    private fun popupMenu(anchor: View) = SettingPopupWindow(requireActivity()).builder {
        // Set the first group
        var groupId = R.id.g_my_list
        PopupMenu(requireContext(), anchor).apply {
            menuInflater.inflate(R.menu.menu_more, menu)
        }.menu.forEach {
            // Set the menu items
            if (groupId != it.groupId) {
                groupId = it.groupId
                val divider =
                    LayoutInflater.from(root.context).inflate(R.layout.item_setting_divider, root, false)
                root.addView(divider)
            }
            val textview = LayoutInflater.from(root.context).inflate(R.layout.item_setting, root, false)
            (textview as? TextView)?.apply {
                text = it.title
                tag = it.itemId
                setCompoundDrawablesWithIntrinsicBounds(it.icon, null, null, null)
                root.addView(this)
            }
        }
        // Set the click event
        root.setOnClickListener { layout ->
            (layout as ViewGroup).children.forEach {
                when (it.tag) {
                    R.id.item_more -> Unit
                    R.id.item_share -> Unit
                    R.id.item_download -> Unit
                    R.id.item_add_favorite -> Unit
                    R.id.item_cast -> Unit
                    R.id.item_add_playlist -> Unit
                }
            }
        }
    }.anchorOn(anchor).popup()

    private fun popupPlaylist() = PlaylistPopupWindow(requireActivity()).builder {
        btnCreatePlaylist.setOnClickListener {
            findNavController().navigate(PlayerFragmentDirections.actionPlayerToPlaylistCreate())
        }
        rvPlaylist.apply {
            adapter = PlaylistAdapter().apply {
                submitList(vm.playlists.value?.getOrNull().orEmpty())
                setOnClickListener { playlist ->
                    player.curPlayingInfo?.let { music -> vm.addSongToPlaylist(music, playlist.id) }
                }
            }
            layoutManager = linearLayoutManager()
            addItemDecoration(PlaylistItemDecorator(getDimen(WidgetResDimen.md_two_unit).toInt(), 0))
        }
    }.anchorOn(merge.btnAddPlaylist).popup()

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
        parent.apply {
            showBottomNavigationBar()
            isMinimalPlayer = true
        }
    }

    private fun expandPlayer() {
        if (isRunningAnim) return
        binding.root.apply {
            setTransition(currentState, R.id.mini_player_start)
            transitionToEnd()
        }
        parent.apply {
            hideBottomNavigationBar()
            isMinimalPlayer = false
        }
    }
}

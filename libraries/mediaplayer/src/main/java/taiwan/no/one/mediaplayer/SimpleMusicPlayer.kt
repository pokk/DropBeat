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

package taiwan.no.one.mediaplayer

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.ShuffleOrder.DefaultShuffleOrder
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode.Default
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode.Shuffle
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State.Standby
import taiwan.no.one.mediaplayer.interfaces.PlayerCallback
import taiwan.no.one.mediaplayer.states.MusicState
import taiwan.no.one.mediaplayer.states.MusicStateStandby
import taiwan.no.one.mediaplayer.utils.MediaUtil

class SimpleMusicPlayer(private val context: Context) : MusicPlayer {
    companion object {
        private const val NAME = "LocalExoPlayer"

        @Volatile private var INSTANCE: SimpleMusicPlayer? = null

        fun initialize(context: Context) {
            val tempInstance = INSTANCE

            if (tempInstance != null) return
            synchronized(this) {
                INSTANCE = SimpleMusicPlayer(context).apply { exoPlayer }
            }
        }

        fun getInstance() = INSTANCE ?: throw Exception("Please call initialize(context) first...")
    }

    private lateinit var timerJob: Job
    private lateinit var timer: ReceiveChannel<Unit>
    private lateinit var _playerState: MusicState
    private var playerState: MusicState
        get() = _playerState
        set(value) {
            callback?.onStatusChanged(value.state)
            _playerState = value
        }
    private var callback: PlayerCallback? = null
    private val exoPlayer by lazy {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaSource(queue, true)
            prepare()
            addListener(MusicEventListener())
            playerState = MusicStateStandby(this)
        }
    }
    private val playlist by lazy { mutableListOf<MusicInfo>() }
    private val queue by lazy { ConcatenatingMediaSource() }
    private val curPlayingIndex get() = exoPlayer.currentWindowIndex
    override val isPlaying get() = exoPlayer.isPlaying
    override val curPlayingInfo get() = playlist.find { exoPlayer.currentMediaItem?.mediaId == it.uri }
    override val curTrackSec get() = exoPlayer.currentPosition / MediaUtil.SECOND_UNIT
    override val curDuration get() = exoPlayer.duration / MediaUtil.SECOND_UNIT
    override var mode: Mode by Delegates.observable(Default) { _, oldMode, newMode ->
        if (oldMode == Shuffle && newMode != Shuffle) {
            // reset the timeline of the player
            exoPlayer.shuffleModeEnabled = false
        }
        if (newMode == Shuffle) {
            exoPlayer.shuffleModeEnabled = true
            queue.setShuffleOrder(DefaultShuffleOrder(queue.size, Random.nextLong()))
        }
        exoPlayer.repeatMode = mode.value
    }

    override fun getState() = playerState.state

    override fun play(): Boolean {
        playerState = playerState.play()
        return true
    }

    override fun stop() {
        playerState = playerState.pause()
        seekTo(0)
    }

    override fun pause() {
        playerState = playerState.pause()
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun next() {
        playerState = playerState.next()
    }

    override fun previous() {
        playerState = playerState.previous()
    }

    override fun seekTo(sec: Int): Boolean {
        if (playerState.state == Standby) {
            return false
        }
        exoPlayer.seekTo(sec.times(MediaUtil.SECOND_UNIT))
        return true
    }

    override fun clearPlaylist() {
        queue.clear()
        playlist.clear()
    }

    override fun appendPlaylist(list: List<MusicInfo>): Boolean {
        list.forEach {
            queue.addMediaSource(buildMediaSource(it.uri))
        }
        playlist.addAll(list)
        return true
    }

    override fun replacePlaylist(list: List<MusicInfo>) {
        queue.clear()
        playlist.clear()
        appendPlaylist(list)
    }

    override fun setPlayerEventCallback(callback: PlayerCallback?) {
        this.callback = callback
    }

    private fun buildMediaSource(url: String): MediaSource {
        val uri = Uri.parse(url)
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, NAME),
            DefaultBandwidthMeter.Builder(context).build()
        )
        // This is the MediaSource representing the media to be played.
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            // FIXME(jieyi): 2022/05/09 This is the new API.
            // .setExtractorsFactory(DefaultExtractorsFactory())
            // .setTag(url) // Keep the uri to tag.
            .createMediaSource(MediaItem.fromUri(uri))
    }

    internal inner class MusicEventListener : Player.Listener {
        override fun onLoadingChanged(isLoading: Boolean) = Unit

        override fun onRepeatModeChanged(repeatMode: Int) = Unit

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) = Unit

        override fun onTimelineChanged(timeline: Timeline, reason: Int) = Unit

        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            // Only trackGroups is not empty, the track will really play the next/previous track.
            if (!trackGroups.isEmpty) {
                curPlayingInfo?.let {
                    callback?.onTrackChanged(it)
                }
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            // TODO(jieyiwu): 6/13/20 The real state change should be here!
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                // Send the callback function each second when the player is playing.
                val (playingS, playingMs) = getTimeOfTrack()
                // Just in the case, for the first second, should send individual because the timer
                // always starts from 1s.
                if (playingS == 0L) {
                    callback?.onTrackCurrentPosition(0)
                }
                // Raise a global coroutine for receiving the tick.
                timerJob = CoroutineScope(Dispatchers.Main).launch {
                    timer = ticker(MediaUtil.SECOND_UNIT, MediaUtil.SECOND_UNIT - playingMs)
                    timer.consumeAsFlow().collect {
                        val (s, ms) = getTimeOfTrack()
                        // The ms time is not always correct so we do need to round the ms.
                        callback?.onTrackCurrentPosition(if (ms > 800) s + 1 else s)
                    }
                }
            }
            else {
                // If it's not playing, job and timer should be cancelled.
                if (::timerJob.isInitialized && ::timer.isInitialized) {
                    timerJob.cancel()
                    timer.cancel()
                }
            }
            callback?.onPlayState(isPlaying)
        }

        private fun getTimeOfTrack() =
            exoPlayer.currentPosition / MediaUtil.SECOND_UNIT to exoPlayer.currentPosition % MediaUtil.SECOND_UNIT
    }
}

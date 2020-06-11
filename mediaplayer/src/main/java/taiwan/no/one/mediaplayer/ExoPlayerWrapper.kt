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

package taiwan.no.one.mediaplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import taiwan.no.one.mediaplayer.MusicPlayerState.Pause
import taiwan.no.one.mediaplayer.MusicPlayerState.Play
import taiwan.no.one.mediaplayer.MusicPlayerState.Standby
import taiwan.no.one.mediaplayer.interfaces.InnerQueue
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer

//class ExoPlayerWrapper(private val context: Context) : MusicPlayer {
//    companion object {
//        private const val TAG = "ExoPlayerWrapper"
//        private const val NAME = "LocalExoPlayer"
//    }
//
//    override var isPlaying = false
//    override val curPlayingUri get() = (exoPlayer.currentTag as? String).orEmpty()
//    override var playingMode: InnerQueue.Mode = InnerQueue.Mode.Default
//    private val exoPlayer by lazy {
//        Log.i(TAG, "init ExoPlayer")
//        ExoPlayer.Builder(context, DefaultTrackSelector()).apply {
//            addListener(LocalPlayerEventListener(this@ExoPlayerWrapper))
//        }
//    }
//    private lateinit var timer: PausableTimer
//    private var playerState: MusicPlayerState = Standby
//        set(value) {
//            if (value == field) return
//            field = value
////            when (value) {
////                Standby -> timer.stop()
////                Play -> timer.resume()
////                Pause -> timer.pause()
////            }
//            // Call the callback function.
//            listener?.onPlayerStateChanged(value)
//        }
//    private val playlist by lazy { ConcatenatingMediaSource() }
//    private val duplicatedList by lazy { mutableListOf<String>() }
//    private val isTheSame: Boolean
//        get() {
//            if (playlist.size != duplicatedList.size)
//                return false
//            duplicatedList.forEachIndexed { index, uri ->
//                if ((playlist.getMediaSource(index).tag as String) != uri)
//                    return false
//            }
//            return true
//        }
//    private var listener: PlayerEventListener? = null
//    private var individualPlay = false
//    private var isPreparedList = false
//
//    init {
//        exoPlayer
//    }
//
//    /**
//     * Start playing a music.
//     * This function will play the music which is specified with an URI.
//     * If the [uri] is blank string, the function is also about play the music, but when
//     * the music is playing, executing this function will pause the music.
//     * If the music is pausing, executing this function will resume the music.
//     *
//     * @param uri a individual track or video uri.
//     * @return True if playing is success, otherwise, as like [Pause], playing the same uri then return False.
//     */
//    override fun play(uri: String): Boolean {
//        // Play the media from the build-in [exoPlayer]'s playlist.
//        if (uri.isBlank()) {
//            playerState = if (isPlaying) Pause else Play
//            exoPlayer.playWhenReady = !isPlaying
//        }
//        // Play a single individual uri.
//        else {
//            if (curPlayingUri == uri) return false
//            // Prepare the player with the source.
//            exoPlayer.apply {
//                prepare(buildMediaSource(uri))
//                isPreparedList = false
//                playWhenReady = true
//            }
//            individualPlay = true
//            playerState = Play
//        }
//
//        return playerState == Play
//    }
//
//    /**
//     * Start playing a music according to the playlist index.
//     *
//     * @param index
//     * @return
//     */
//    override fun play(index: Int): Boolean {
//        // If play the difference track, it should be reset.
//        if (duplicatedList[index] != curPlayingUri) {
//            if (!isTheSame) {
//                playlist.clear()
//                playlist.addMediaSources(duplicatedList.map(::buildMediaSource))
//            }
//            // According to index to play the music from the playlist.
//            exoPlayer.apply {
//                resetPlayerState()
//                // If it's not prepared yet then prepare the playlist.
//                if (!isPreparedList) {
//                    exoPlayer.prepare(playlist)
//                    isPreparedList = true
//                }
//                seekTo(index, C.TIME_UNSET)
//            }
//        }
//        return play()
//    }
//
//    /**
//     * Stop playing the music.
//     */
//    override fun stop() {
//        if (playerState is Standby) return
//        exoPlayer.playWhenReady = false
//        exoPlayer.stop()
//        resetPlayerState()
//    }
//
//    /**
//     * Pause a music. If no music is played, nothing to do.
//     */
//    override fun pause() {
//        if (playerState is Pause) return
//        exoPlayer.playWhenReady = false
//        playerState = Pause
//    }
//
//    /**
//     * Resume the playing of the music.
//     */
//    override fun resume() {
//        if (playerState is Play) return
//        exoPlayer.playWhenReady = true
//        playerState = Play
//    }
//
//    /**
//     * Play the next track from the playlist.
//     */
//    override fun next() {
//        exoPlayer.apply {
//            if (hasNext())
//                next()
//        }
//    }
//
//    /**
//     * Play the previous track from the playlist.
//     */
//    override fun previous() {
//        exoPlayer.apply {
//            if (hasPrevious())
//                previous()
//        }
//    }
//
//    /**
//     * Clear all tracks from the playlist.
//     */
//    override fun clearPlaylist() {
//        duplicatedList.clear()
//    }
//
//    /**
//     * Add a new track [list] into the playlist.
//     *
//     * @param list
//     * @return
//     */
//    override fun addToPlaylist(list: List<String>): Boolean {
//        if (individualPlay) {
//            exoPlayer.prepare(playlist)
//            isPreparedList = true
//            individualPlay = false
//        }
//        // Set backup list temporally because it might not be played.
//        duplicatedList.addAll(list)
//        return true
//    }
//
//    /**
//     * Clear original list playlist has then add a new track [list] into the playlist.
//     *
//     * @param list
//     */
//    override fun replacePlaylist(list: List<String>) {
//        clearPlaylist()
//        addToPlaylist(list)
//    }
//
//    /**
//     * Set the repeat mode: normal play, repeat one music, repeat the whole playlist
//     */
//    override fun setPlayMode(mode: InnerQueue.Mode) {
//        playingMode = mode
//        when (mode) {
//            InnerQueue.Mode.Default -> {
//                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
//                exoPlayer.shuffleModeEnabled = false
//            }
//            InnerQueue.Mode.RepeatOne -> {
//                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//                exoPlayer.shuffleModeEnabled = false
//            }
//            InnerQueue.Mode.RepeatAll -> {
//                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
//                exoPlayer.shuffleModeEnabled = false
//            }
//            InnerQueue.Mode.Shuffle -> exoPlayer.shuffleModeEnabled = true
//        }
//    }
//
//    /**
//     * seek the play time when the music is playing
//     */
//    override fun seekTo(sec: Int) {
//        exoPlayer.seekTo(sec.times(1000).toLong())
//    }
//
//    /**
//     * The function is used to get the current state of the music player.
//     *
//     * @return [Standby]: the music player is waiting for the music.
//     *  [Play]: the music player is playing.
//     *  [Pause]: the music player is pausing.
//     */
//    override fun getPlayerState() = playerState
//
//    /**
//     * The function is used to write the media file to local storage if the music player get the complete file.
//     *
//     * @return false is that writing file unsuccessful, otherwise, is that writing file successful.
//     */
//    override fun writeToFile(url: String, filePath: String?) = try {
//        DownloadHandler(url, filePath, listener).start()
//        true
//    }
//    catch (e: Exception) {
//        if (BuildConfig.DEBUG)
//            e.printStackTrace()
//        false
//    }
//
//    /**
//     * The function is used to set up an event listener which monitor the activity of music player.
//     */
//    override fun setEventListener(listener: PlayerEventListener?) {
//        this.listener = listener
//    }
//
//    private fun buildMediaSource(url: String): MediaSource {
//        val uri = Uri.parse(url)
//        // Produces DataSource instances through which media data is loaded.
//        val dataSourceFactory =
//            DefaultDataSourceFactory(context,
//                                     Util.getUserAgent(context, NAME),
//                                     DefaultBandwidthMeter())
//        // This is the MediaSource representing the media to be played.
//        return ExtractorMediaSource.Factory(dataSourceFactory)
//            .setExtractorsFactory(DefaultExtractorsFactory())
//            .setTag(url) // Keep the uri to tag.
//            .createMediaSource(uri)
//    }
//
//    private fun resetPlayerState() {
//        isPlaying = false
//        playerState = Standby
//    }
//
//    private open class LocalPlayerEventListener(
//        private val wrapper: ExoPlayerWrapper
//    ) : Player.EventListener {
//        private var currentIndex = -1
//        private val exoPlayer by lazy { wrapper.exoPlayer }
//
//        override fun onPositionDiscontinuity(reason: Int) {
//            val newIndex = exoPlayer.currentWindowIndex
//            if (newIndex != currentIndex) {
//                currentIndex = newIndex
//                // The index has changed; update the UI to show info for source at newIndex.
//                wrapper.listener?.onChangeTrack(currentIndex, newIndex)
//            }
//        }
//
//        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//            wrapper.isPlaying = playWhenReady
//        }
//
//        override fun onLoadingChanged(isLoading: Boolean) {
//            wrapper.listener?.onBufferPercentage(exoPlayer.bufferedPercentage)
//        }
//
//        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
//            val millis = 1000
//            val threshold =
//                1_000_000 // Avoiding the bigger timeline comes, cause the running time is incorrect.
//
//            if (exoPlayer.duration in 1..threshold) {
//                wrapper.listener?.onDurationChanged(exoPlayer.duration.div(millis).toInt())
//                wrapper.timer =
//                    PausableTimer(exoPlayer.duration.minus(exoPlayer.currentPosition),
//                                  millis.toLong())
//                wrapper.timer.onTick = { millisUntilFinished ->
//                    val time = (exoPlayer.duration - millisUntilFinished).div(millis).toInt()
//                    wrapper.listener?.onCurrentTime(time)
//                }
//                wrapper.timer.onFinish = {
//                    wrapper.listener?.onCurrentTime(0)
//                }
//                wrapper.timer.start()
//            }
//        }
//    }
//}

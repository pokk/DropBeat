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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import taiwan.no.one.mediaplayer.interfaces.InnerQueue
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.Mode.Default
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State.Pause
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State.Play
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State.Standby
import taiwan.no.one.mediaplayer.utils.MusicQueue

class SimpleMusicPlayer(private val context: Context) : MusicPlayer {
    companion object {
        private const val TAG = "ExoPlayerWrapper"
        private const val NAME = "LocalExoPlayer"

        @Volatile
        private var INSTANCE: MusicPlayer? = null

        fun initialize(context: Context) {
            val tempInstance = INSTANCE

            if (tempInstance != null) return
            synchronized(this) { INSTANCE = SimpleMusicPlayer(context) }
        }

        fun getInstance() = INSTANCE ?: throw Exception("Please call initialize(context) first...")
    }

    private val exoPlayer by lazy {
        Log.i(TAG, "init ExoPlayer")
        ExoPlayer.Builder(context).build()
    }
    private val playlist by lazy { mutableListOf<MusicInfo>() }
    private val queue: InnerQueue<MusicInfo> by lazy { MusicQueue() }
    private var state: State = Standby

    init {
        exoPlayer
    }

    override val isPlaying get() = state == Play
    override val curPlayingUri: MusicInfo get() = TODO()
    override var mode: Mode = Default
    override fun getPlayerState() = state

    override fun play(): Boolean {
        TODO("Not yet implemented")
    }

    override fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
        state = Standby
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
        state = Pause
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun next() {
        val music = queue.goNext() ?: return
        exoPlayer.prepare(buildMediaSource(music.uri))
    }

    override fun previous() {
        val music = queue.goPrevious() ?: return
        exoPlayer.prepare(buildMediaSource(music.uri))
    }

    override fun seekTo(sec: Int) = exoPlayer.seekTo(sec.times(1000).toLong())

    override fun clearPlaylist() {
        playlist.clear()
        queue.clear()
    }

    override fun appendPlaylist(list: List<MusicInfo>): Boolean {
        val isSuccess = playlist.addAll(list) && queue.enqueue(list)
        // TODO(jieyiwu): 6/12/20 should rescue the list again.
        return isSuccess
    }

    override fun replacePlaylist(list: List<MusicInfo>) {
        playlist.apply {
            clear()
            addAll(list)
        }
        queue.apply {
            clear()
            enqueue(list)
        }
    }

    private fun buildMediaSource(url: String): MediaSource {
        val uri = Uri.parse(url)
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(context,
                                                         Util.getUserAgent(context, NAME),
                                                         DefaultBandwidthMeter.Builder(context).build())
        // This is the MediaSource representing the media to be played.
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(DefaultExtractorsFactory())
            .setTag(url) // Keep the uri to tag.
            .createMediaSource(uri)
    }
}

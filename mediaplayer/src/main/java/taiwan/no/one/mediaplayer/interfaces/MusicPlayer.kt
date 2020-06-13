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

package taiwan.no.one.mediaplayer.interfaces

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import taiwan.no.one.mediaplayer.MusicInfo

interface MusicPlayer {
    enum class Mode(val value: Int) {
        Default(Player.REPEAT_MODE_OFF), // Sequence play
        RepeatOne(Player.REPEAT_MODE_ONE),
        RepeatAll(Player.REPEAT_MODE_ALL),
        Shuffle(Player.REPEAT_MODE_ALL);
    }

    sealed class State {
        object Standby : State()
        object Play : State()
        object Pause : State()
    }

    /** The current is paying state or not. */
    val isPlaying: Boolean

    /** Current playing track's information. */
    val curPlayingInfo: MusicInfo?

    /** Current track position of second */
    val curTrackSec: Long

    /** Current playing mode for the playlist. */
    var mode: Mode

    /**
     * The function is used to get the current state of the music player.
     *
     * @return [Standby]: the music player is waiting for the music.
     *  [Play]: the music player is playing.
     *  [Pause]: the music player is pausing.
     */
    fun getState(): State

    //region Player Action
    /**
     * Start playing a music.
     * The function is also about play the music, but when the music is playing, executing this
     * function will pause the music.
     * If the music is pausing, executing this function will resume the music.
     * If playing is failed, the function returns false.
     */
    fun play(): Boolean

    /**
     * Stop playing the music.
     */
    fun stop()

    /**
     * Pause a music. If no music is played, nothing to do.
     */
    fun pause()

    /**
     * Resume the playing of the music.
     */
    fun resume()

    /**
     * Play the next track from the playlist.
     */
    fun next()

    /**
     * Play the previous track from the playlist.
     */
    fun previous()
    //endregion

    /**
     * seek the play time when the music is playing
     */
    fun seekTo(sec: Int): Boolean

    /**
     * Clear all tracks from the playlist.
     */
    fun clearPlaylist()

    /**
     * Add a new track [list] into the playlist.
     *
     * @param list
     * @return
     */
    fun appendPlaylist(list: List<MusicInfo>): Boolean

    /**
     * Clear original list playlist has then add a new track [list] into the playlist.
     *
     * @param list
     */
    fun replacePlaylist(list: List<MusicInfo>)

    /**
     * The function is used to set up an event listener which monitor the activity of music player.
     */
    fun setPlayerEventCallback(callback: PlayerCallback?)
}

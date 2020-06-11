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

import taiwan.no.one.mediaplayer.MusicPlayerState

interface MusicPlayer {
    sealed class Mode {
        object Default : Mode() // Sequence play
        object RepeatOne : Mode()
        object RepeatAll : Mode()
        object Shuffle : Mode()
    }

    /** The current is paying state or not. */
    val isPlaying: Boolean

    /** Current playing track's uri. */
    val curPlayingUri: String

    /** Current playing mode for the playlist. */
    val playingMode: Mode

    /**
     * Start playing a music.
     * This function will play the music which is specified with an URI.
     * If the [uri] is blank string, the function is also about play the music, but when
     * the music is playing, executing this function will pause the music.
     * If the music is pausing, executing this function will resume the music.
     * If playing is failed, the function returns false.
     *
     * @param uri the uri of a track.
     */
    fun play(uri: String = ""): Boolean

    /**
     * Start playing a music according to the playlist index.
     *
     * @param index
     * @return
     */
    fun play(index: Int): Boolean

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
    fun addToPlaylist(list: List<String>): Boolean

    /**
     * Clear original list playlist has then add a new track [list] into the playlist.
     *
     * @param list
     */
    fun replacePlaylist(list: List<String>)

    /**
     * Set the repeat mode: normal play, repeat one music, repeat the whole playlist
     */
    fun setPlayMode(mode: Mode)

    /**
     * seek the play time when the music is playing
     */
    fun seekTo(sec: Int)

    /**
     * The function is used to get the current state of the music player.
     *
     * @return [taiwan.no.one.mediaplayer.MusicPlayerState.Standby]: the music player is waiting for the music.
     *  [taiwan.no.one.mediaplayer.MusicPlayerState.Play]: the music player is playing.
     *  [taiwan.no.one.mediaplayer.MusicPlayerState.Pause]: the music player is pausing.
     */
    fun getPlayerState(): MusicPlayerState

    /**
     * The function is used to set up an event listener which monitor the activity of music player.
     */
//    fun setEventListener(listener: PlayerEventListener?)

    /**
     * The function is used to write the media file to local storage if the music player get the complete file.
     *
     * @return false is that writing file unsuccessful, otherwise, is that writing file successful.
     */
    fun writeToFile(url: String, filePath: String? = null): Boolean
}

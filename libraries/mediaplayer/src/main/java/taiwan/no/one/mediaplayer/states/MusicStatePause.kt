/*
 * MIT License
 *
 * Copyright (c) 2022 Jieyi
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

package taiwan.no.one.mediaplayer.states

import androidx.media3.exoplayer.ExoPlayer
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State.Pause

internal class MusicStatePause(player: ExoPlayer) : MusicState(player) {
    override val state = Pause

    override fun play(): MusicState {
        player.playWhenReady = true
        return MusicStatePlay(player)
    }

    override fun pause() = this

    override fun next(): MusicState {
        if (!player.hasNextMediaItem()) return this
        player.seekToNextMediaItem()
        return play()
    }

    override fun previous(): MusicState {
        if (!player.hasPreviousMediaItem()) return this
        player.seekToPreviousMediaItem()
        return play()
    }

    override fun resetPlaylist() = MusicStatePause(player)
}

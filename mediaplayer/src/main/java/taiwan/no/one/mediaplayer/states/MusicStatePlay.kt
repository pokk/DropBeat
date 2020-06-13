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

package taiwan.no.one.mediaplayer.states

import com.google.android.exoplayer2.ExoPlayer
import taiwan.no.one.mediaplayer.interfaces.MusicPlayer.State.Play

internal class MusicStatePlay(player: ExoPlayer) : MusicState(player) {
    override val state = Play

    override fun play(): MusicState {
        player.playWhenReady = false
        return MusicStatePause(player)
    }

    override fun pause(): MusicState {
        player.playWhenReady = false
        return MusicStatePause(player)
    }

    override fun next(): MusicState {
        player.next()
        return this
    }

    override fun previous(): MusicState {
        player.previous()
        return this
    }

    override fun resetPlaylist(): MusicState {
        return this
    }
}

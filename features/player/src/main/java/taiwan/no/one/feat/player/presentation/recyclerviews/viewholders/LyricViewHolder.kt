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

package taiwan.no.one.feat.player.presentation.recyclerviews.viewholders

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.devrapid.kotlinknifer.getColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import taiwan.no.one.ext.DEFAULT_INT
import taiwan.no.one.feat.player.R
import taiwan.no.one.feat.player.databinding.ItemLyricBinding
import taiwan.no.one.feat.player.presentation.recyclerviews.adapters.LyricAdapter
import taiwan.no.one.feat.player.presentation.recyclerviews.states.LrcState
import taiwan.no.one.mediaplayer.lyric.LrcRowEntity
import taiwan.no.one.widget.recyclerviews.ViewHolderBinding

internal class LyricViewHolder(
    private val binding: ItemLyricBinding,
) : ViewHolderBinding<Pair<LrcState, LrcRowEntity>, LyricAdapter>(binding.root) {
    private var lrcRowEntity: LrcRowEntity? = null
    private var job: Job? = null

    override fun initView(entity: Pair<LrcState, LrcRowEntity>, adapter: LyricAdapter) {
        val (state, lrc) = entity
        lrcRowEntity = lrc
        binding.mtvLyric.apply {
            text = lrc.content.orEmpty()
            setTextColor(context.getColor(state.color))
            updateLayoutParams {
                height = if (state is LrcState.DummyState) state.rowHeight else ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    fun observeStateChange(adapter: LyricAdapter) {
        val root = binding.root
        job = root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            adapter.stateFlow?.collect {
                if (it.color != DEFAULT_INT) {
                    root.setTextColor(
                        root.getColor(
                            if (absoluteAdapterPosition == it.highlightPos) it.color else R.color.silver_chalice
                        )
                    )
                }
            }
        }
    }

    fun unsubscribeStateChange() {
        job?.cancel()
    }
}

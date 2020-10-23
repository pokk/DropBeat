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

package taiwan.no.one.feat.library.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import taiwan.no.one.dropbeat.AppResLayout
import taiwan.no.one.dropbeat.data.entities.SimpleTrackEntity
import taiwan.no.one.dropbeat.databinding.ItemTypeOfMusicBinding
import taiwan.no.one.feat.library.presentation.recyclerviews.viewholders.TrackViewHolder
import taiwan.no.one.widget.recyclerviews.AutoUpdatable
import kotlin.properties.Delegates

internal class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>(), AutoUpdatable {
    var data: List<SimpleTrackEntity> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(oldValue, newValue) { o, n -> o.uri == n.uri }
    }
    var clickListener: ((SimpleTrackEntity) -> Unit)? = null
        private set
    var optionListener: (() -> Unit)? = null
        private set
    var favoriteListener: ((SimpleTrackEntity) -> Unit)? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(AppResLayout.item_type_of_music, parent, false)
            .let { TrackViewHolder(ItemTypeOfMusicBinding.bind(it)) }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.initView(data[position], this)

    override fun getItemCount() = data.size

    fun setOnClickListener(listener: (SimpleTrackEntity) -> Unit) {
        clickListener = listener
    }

    fun setOptionClickListener(listener: () -> Unit) {
        optionListener = listener
    }

    fun setFavoriteClickListener(listener: (SimpleTrackEntity) -> Unit) {
        favoriteListener = listener
    }
}

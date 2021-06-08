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

package taiwan.no.one.feat.library.presentation.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.devrapid.kotlinknifer.getDimen
import kotlin.properties.Delegates
import taiwan.no.one.dropbeat.AppResLayout
import taiwan.no.one.dropbeat.databinding.ItemTypeOfMusicBinding
import taiwan.no.one.entity.SimpleTrackEntity
import taiwan.no.one.feat.library.presentation.recyclerviews.viewholders.TrackViewHolder
import taiwan.no.one.widget.WidgetResDimen
import taiwan.no.one.widget.recyclerviews.AutoUpdatable

internal class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>(), AutoUpdatable {
    var data: List<SimpleTrackEntity> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(oldValue, newValue) { o, n -> o.uri == n.uri && o.thumbUri == n.thumbUri }
    }
    var clickListener: ((SimpleTrackEntity) -> Unit)? = null
        private set
    var optionListener: ((View, SimpleTrackEntity) -> Unit)? = null
        private set
    var favoriteListener: ((SimpleTrackEntity) -> Unit)? = null
        private set
    var requestPictureCallback: ((SimpleTrackEntity) -> Unit)? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(AppResLayout.item_type_of_music, parent, false)
            .let { TrackViewHolder(ItemTypeOfMusicBinding.bind(it)) }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.itemView.updatePadding(top = holder.itemView.context.applicationContext.getDimen(
            if (position == 0) {
                WidgetResDimen.md_zero_unit
            }
            else {
                WidgetResDimen.md_two_half_unit
            }
        ).toInt())
        holder.initView(data[position], this)
    }

    override fun getItemCount() = data.size

    fun removeItem(entity: SimpleTrackEntity) {
        data = data.toMutableList().apply { remove(entity) }
    }

    fun setOnClickListener(listener: (SimpleTrackEntity) -> Unit) {
        clickListener = listener
    }

    fun setOptionClickListener(listener: (View, SimpleTrackEntity) -> Unit) {
        optionListener = listener
    }

    fun setFavoriteClickListener(listener: (SimpleTrackEntity) -> Unit) {
        favoriteListener = listener
    }

    fun setRequestPicCallback(callback: (SimpleTrackEntity) -> Unit) {
        requestPictureCallback = callback
    }
}

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

package taiwan.no.one.feat.library.presentation.fragments

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devrapid.kotlinknifer.loge
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.library.data.entities.local.LibraryEntity.PlayListEntity
import taiwan.no.one.feat.library.databinding.FragmentCreateBinding
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel

internal class CreateFragment : BaseFragment<BaseActivity<*>, FragmentCreateBinding>() {
    private val vm by viewModels<PlaylistViewModel>()

    override fun bindLiveData() {
        vm.result.observe(this) {
            it.onSuccess {
                findNavController().navigateUp()
            }.onFailure(::loge)
        }
    }

    override fun viewComponentBinding() {
        binding.tietPlaylistName.addTextChangedListener {
            binding.btnCreate.text = if (it.isNullOrBlank()) "Skip" else "Create"
        }
    }

    override fun componentListenersBinding() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnCreate.setOnClickListener {
            if (binding.tietPlaylistName.text.isNullOrBlank()) {
                findNavController().navigateUp()
            }
            else {
                vm.createPlaylist(PlayListEntity(name = binding.tietPlaylistName.text.toString()))
            }
        }
    }
}
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

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devrapid.kotlinknifer.loge
import com.google.android.material.transition.MaterialSharedAxis
import taiwan.no.one.core.presentation.activity.BaseActivity
import taiwan.no.one.core.presentation.fragment.BaseFragment
import taiwan.no.one.feat.library.databinding.FragmentRenameBinding
import taiwan.no.one.feat.library.presentation.viewmodels.PlaylistViewModel

internal class RenameFragment : BaseFragment<BaseActivity<*>, FragmentRenameBinding>() {
    private val vm by viewModels<PlaylistViewModel>()
    private val navArgs by navArgs<RenameFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun bindLiveData() {
        vm.playlist.observe(this) { res ->
            res.onSuccess {
                binding.tietPlaylistName.setText(it.name)
            }.onFailure { loge(it) }
        }
        vm.result.observe(this) {
            it.onSuccess { findNavController().navigateUp() }.onFailure { loge(it) }
        }
    }

    override fun componentListenersBinding() {
        binding.tietPlaylistName.addTextChangedListener {
            binding.btnUpdate.isEnabled = !it.isNullOrBlank()
        }
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnUpdate.setOnClickListener {
            vm.updatePlaylist(navArgs.playlistId, binding.tietPlaylistName.text.toString())
        }
    }

    override fun rendered(savedInstanceState: Bundle?) {
        addStatusBarHeightMarginTop(binding.btnBack)
        vm.getPlaylist(navArgs.playlistId)
    }
}

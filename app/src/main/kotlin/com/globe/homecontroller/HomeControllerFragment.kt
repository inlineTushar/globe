package com.globe.homecontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.globe.GenericErrorFragment
import com.globe.LoaderFragment
import com.globe.NetworkFragment
import com.globe.databinding.FragmentHomeControllerBinding
import com.globe.homecontroller.HomeControllerViewModel.ViewState
import com.globe.platform.extension.replaceIfNoPrevious
import com.globe.platform.extension.viewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeControllerFragment : Fragment() {

    private var binding: FragmentHomeControllerBinding by viewLifecycle()
    private val homeControllerViewModel: HomeControllerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeControllerBinding.inflate(inflater, container, false)
        .also {
            binding = it
            initObserver()
            homeControllerViewModel.onCreate()
        }
        .root

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeControllerViewModel.viewState.collect { viewState ->
                    when (viewState) {
                        ViewState.Loading -> {
                            replaceIfNoPrevious(binding.container.id) { LoaderFragment.newInstance() }
                        }
                        ViewState.Data -> {
                            replaceIfNoPrevious(binding.container.id) { HomeDataControllerFragment.newInstance() }
                        }
                        ViewState.GenericError -> {
                            replaceIfNoPrevious(binding.container.id) { GenericErrorFragment.newInstance() }
                        }
                        ViewState.NetworkError -> {
                            replaceIfNoPrevious(binding.container.id) { NetworkFragment.newInstance() }
                        }
                    }
                }
            }
        }
    }
}

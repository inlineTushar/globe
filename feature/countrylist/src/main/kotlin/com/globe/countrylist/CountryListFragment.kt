package com.globe.countrylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.globe.countrylist.databinding.FragmentCountryListBinding
import com.globe.platform.extension.viewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountryListFragment : Fragment() {

    private var binding: FragmentCountryListBinding by viewLifecycle()
    private val countryListViewModel: CountryListViewModel by viewModel()
    private val countryListAdapter = CountryListAdapter {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentCountryListBinding.inflate(inflater, container, false)
            .also {
                binding = it
                initRecyclerview()
                initObserver()
                countryListViewModel.onCreate()
            }
            .root

    private fun initRecyclerview() {
        binding.countryList.adapter = countryListAdapter
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                countryListViewModel.viewState.collect {
                    countryListAdapter.submitList(it.countries)
                }
            }
        }
    }
}

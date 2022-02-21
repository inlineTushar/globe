package com.globe.countrydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.globe.GenericErrorFragment
import com.globe.platform.extension.fragmentArgs
import com.globe.platform.extension.replaceIfNoPrevious
import com.globe.platform.extension.viewLifecycle
import com.globe.platform.extension.withArguments
import com.globe.countrydetail.CountryDetailControllerViewModel.ViewState
import com.globe.countrydetail.databinding.FragmentCountryControllerDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CountryDetailControllerFragment : Fragment() {

    companion object {
        private const val ARGUMENT_COUNTRY_ID = "argument:country_id"
        fun newInstance(countryId: String) =
            CountryDetailControllerFragment().withArguments(ARGUMENT_COUNTRY_ID to countryId)
    }

    private var binding: FragmentCountryControllerDetailBinding by viewLifecycle()

    private val countryDetailControllerViewModel: CountryDetailControllerViewModel by viewModel {
        val countryId: String by fragmentArgs(ARGUMENT_COUNTRY_ID)
        parametersOf(countryId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCountryControllerDetailBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            initObserver()
            countryDetailControllerViewModel.onCreate()
        }
        .root

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                countryDetailControllerViewModel.viewState.collect { viewState ->
                    when (viewState) {
                        is ViewState.Data -> {
                            replaceIfNoPrevious(binding.detailContainer.id) {
                                CountryDetailFragment.newInstance(viewState.countryModel)
                            }
                        }
                        ViewState.Empty -> {
                            replaceIfNoPrevious(binding.detailContainer.id) { GenericErrorFragment.newInstance() }
                        }
                    }
                }
            }
        }
    }
}

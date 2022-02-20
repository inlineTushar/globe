package com.tushar.countrydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.globe.data.model.CountryModel
import com.globe.platform.extension.fragmentArgs
import com.globe.platform.extension.viewLifecycle
import com.globe.platform.extension.withArguments
import com.tushar.countrydetail.databinding.FragmentCountryDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CountryDetailFragment : Fragment() {

    companion object {
        private const val ARGUMENT_COUNTRY = "argument:country"
        fun newInstance(countryModel: CountryModel) =
            CountryDetailFragment().withArguments(ARGUMENT_COUNTRY to countryModel)
    }

    private var binding: FragmentCountryDetailBinding by viewLifecycle()
    private val countryDetailViewModel: CountryDetailViewModel by viewModel {
        val countryModel: CountryModel by fragmentArgs(ARGUMENT_COUNTRY)
        parametersOf(countryModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCountryDetailBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            initObserver()
            countryDetailViewModel.onCreate()
        }
        .root

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                countryDetailViewModel.viewState.collect {
                    binding.renderCountryDetail(it.countryModel)
                }
            }
        }
    }

    private fun FragmentCountryDetailBinding.renderCountryDetail(countryModel: CountryModel) {
        countryModel.flag?.let {
            countryFlag.isVisible = true
            countryFlag.text = it
        }

        countryName.text = countryModel.name
        countryOfficialName.text = countryModel.officialName

        countryModel
            .currencies
            .takeIf { it.isNotEmpty() }
            ?.apply {
                currencies.isVisible = true
                currencies.text = getString(
                    R.string.title_currency,
                    joinToString(", ") { "${it.name} (${it.code} - ${it.symbol})" })
            }

        countryCode.text = getString(R.string.title_currency_code, countryModel.countryCode)

        countryModel
            .capitals
            .takeIf { it.isNotEmpty() }
            ?.apply {
                capital.isVisible = true
                capital.text = getString(R.string.title_capital, joinToString(", ") { it })
            }

        population.text = getString(
            R.string.title_population,
            String.format("%.2fM", (countryModel.population / 1000000.0))
        )
    }
}

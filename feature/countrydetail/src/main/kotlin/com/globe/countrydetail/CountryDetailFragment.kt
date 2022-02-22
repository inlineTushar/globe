package com.globe.countrydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.globe.countrydetail.databinding.FragmentCountryDetailBinding
import com.globe.data.model.CountryModel
import com.globe.data.model.CurrencyModel
import com.globe.load
import com.globe.platform.extension.fragmentArgs
import com.globe.platform.extension.viewLifecycle
import com.globe.platform.extension.withArguments
import kotlinx.coroutines.flow.collectLatest
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
                countryDetailViewModel.viewState.collectLatest {
                    binding.renderCountryDetail(it.countryModel)
                }
            }
        }
    }

    private fun FragmentCountryDetailBinding.renderCountryDetail(countryModel: CountryModel) {
        renderFlag(countryModel)
        renderName(countryModel.name)
        renderOfficialName(countryModel.officialName)
        renderCurrencies(countryModel.currencies)
        renderCountryCode(countryModel.countryCode)
        renderCapitals(countryModel.capitals)
        renderPopulation(countryModel.population)
    }

    private fun FragmentCountryDetailBinding.renderFlag(countryModel: CountryModel) {
        countryModel.flag?.url?.let {
            imageFlagView.isVisible = true
            imageFlagView.load(
                context = imageFlagView.context,
                it,
                onFailure = {
                    imageFlagView.isVisible = false
                    renderUnicodeFlag(countryModel)
                }
            )
        }
    }

    private fun FragmentCountryDetailBinding.renderUnicodeFlag(countryModel: CountryModel) {
        countryModel.flag?.unicode?.let {
            unicodeFlagView.isVisible = true
            unicodeFlagView.text = it
        }
    }

    private fun renderName(name: String) {
        binding.countryNameView.text = name
    }

    private fun renderOfficialName(officialName: String) {
        binding.countryOfficialNameView.text = officialName
    }

    private fun FragmentCountryDetailBinding.renderCurrencies(currencyList: List<CurrencyModel>) {
        currencyList
            .takeIf { it.isNotEmpty() }
            ?.apply {
                currenciesView.isVisible = true
                currenciesView.text = getString(
                    com.globe.ui.R.string.title_currency,
                    joinToString(", ") { "${it.name} (${it.code} - ${it.symbol})" })
            }
    }

    private fun renderCountryCode(code: String) {
        binding.countryCodeView.text = getString(com.globe.ui.R.string.title_currency_code, code)
    }

    private fun FragmentCountryDetailBinding.renderCapitals(capitals: List<String>) {
        capitals
            .takeIf { it.isNotEmpty() }
            ?.apply {
                capitalView.isVisible = true
                capitalView.text =
                    getString(com.globe.ui.R.string.title_capital, joinToString(", ") { it })
            }
    }

    private fun renderPopulation(population: Long) {
        binding.populationView.text = getString(
            com.globe.ui.R.string.title_population,
            String.format("%.2fM", (population / 1000000.0))
        )
    }
}

package com.globe.search

import com.globe.data.repository.CountryRepository
import com.globe.platform.BaseViewModel

class SearchViewModel(
    private val countryRepository: CountryRepository
) : BaseViewModel() {

    fun searchCounties(keyword: String) {
        coroutineWrapper { countryRepository.fetchCountries(keyword) }
    }
}

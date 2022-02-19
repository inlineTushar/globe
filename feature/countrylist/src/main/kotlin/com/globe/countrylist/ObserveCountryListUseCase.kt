package com.globe.countrylist

import com.globe.data.repository.CountryModel
import com.globe.data.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCountryListUseCase(
    private val countryRepository: CountryRepository
) {
    fun observe(): Flow<List<CountryModel>> =
        countryRepository
            .observeAllCountriesInfo()
            .map { counties -> counties.sortedBy { it.name } }
}

package com.globe.countrylist

import com.globe.data.model.CountryModel
import com.globe.data.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class ObserveCountryListUseCase(
    private val countryRepository: CountryRepository
) {
    fun observe(): Flow<List<CountryModel>> =
        countryRepository
            .observeAllCountriesInfo()
}

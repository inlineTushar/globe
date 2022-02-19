package com.globe.data.api

import com.globe.data.model.CountryApiModel

class CountryApi(private val countryService: CountryService) {

    suspend fun getAllCountries(): List<CountryApiModel> =
        countryService.getAllCountries()
}

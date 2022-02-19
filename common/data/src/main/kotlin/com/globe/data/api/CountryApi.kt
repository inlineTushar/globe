package com.globe.data.api

import com.globe.data.model.CountryApiModel

class CountryApi(private val countryService: CountryService) {

    suspend fun getCountryInfo(code: String): List<CountryApiModel> =
        countryService.getCountryInfo(code)
}

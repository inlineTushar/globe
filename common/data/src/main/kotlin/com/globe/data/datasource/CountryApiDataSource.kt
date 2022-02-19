package com.globe.data.datasource

import com.globe.data.api.CountryApi
import com.globe.data.model.CountryApiModel

class CountryApiDataSource(private val countryApi: CountryApi) {

    suspend fun getAllCountries(): List<CountryApiModel> =
        countryApi.getAllCountries()
}

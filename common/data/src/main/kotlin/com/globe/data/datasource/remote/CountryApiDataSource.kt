package com.globe.data.datasource.remote

import com.globe.data.datasource.remote.api.CountryApi
import com.globe.data.model.CountryApiModel

class CountryApiDataSource(private val countryApi: CountryApi) {

    suspend fun getAllCountries(): List<CountryApiModel> =
        countryApi.getAllCountries()
}

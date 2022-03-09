package com.globe.data.datasource.remote.api

import com.globe.data.model.CountryApiModel

interface CountryService {
    suspend fun getAllCountries(): List<CountryApiModel>
}

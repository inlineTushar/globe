package com.globe.data.datasource.remote.api

import com.globe.data.model.CountryApiModel
import retrofit2.http.GET

interface CountryApiService : CountryService {

    @GET("all")
    override suspend fun getAllCountries(): List<CountryApiModel>
}

package com.globe.data.datasource.remote.api

import com.globe.data.model.CountryApiModel
import retrofit2.http.GET

interface CountryService {

    @GET("all")
    suspend fun getAllCountries(): List<CountryApiModel>
}

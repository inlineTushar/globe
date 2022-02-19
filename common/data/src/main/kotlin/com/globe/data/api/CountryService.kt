package com.globe.data.api

import com.globe.data.model.CountryApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {

    @GET("{code}")
    suspend fun getCountryInfo(@Path("code") code: String): List<CountryApiModel>
}

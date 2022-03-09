package com.globe.data.datasource.remote.api

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.globe.data.model.CountryApiModel
import retrofit2.http.GET

interface CountryMockApiService : CountryService {

    @Mock
    @MockResponse(body = "mock/all_countries_response.json")
    @GET("all")
    override suspend fun getAllCountries(): List<CountryApiModel>
}

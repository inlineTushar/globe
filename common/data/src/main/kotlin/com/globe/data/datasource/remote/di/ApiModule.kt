package com.globe.data.datasource.remote.di

import com.globe.data.datasource.remote.api.CountryApi
import com.globe.data.datasource.remote.api.CountryService
import org.koin.dsl.module
import retrofit2.Retrofit

private const val COUNTRY_API = "https://restcountries.com/v3.1/"

val apiModule = module {
    single {
        CountryApi(get())
    }

    single<CountryService> {
        val builder: Retrofit.Builder = get()
        builder
            .baseUrl(COUNTRY_API)
            .build()
            .create(CountryService::class.java)
    }
}

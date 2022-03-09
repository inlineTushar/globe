package com.globe.data.datasource.remote.di

import com.globe.data.datasource.remote.api.CountryApiService
import com.globe.data.extension.NetworkCheck
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single<CountryApiService> {
        val builder: Retrofit.Builder = get()
        builder
            .baseUrl(COUNTRY_API)
            .build()
            .create(CountryApiService::class.java)
    }

    single {
        NetworkCheck(get())
    }
}

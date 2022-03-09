package com.globe.data.datasource.remote.di

import com.globe.data.BuildConfig
import com.globe.data.datasource.remote.api.CountryApi
import com.globe.data.datasource.remote.api.CountryApiService
import com.globe.data.datasource.remote.api.CountryMockApiService
import org.koin.dsl.module

val countryApiModule = module {
    single {
        CountryApi(
            if (BuildConfig.FLAVOR == "local") {
                get<CountryMockApiService>()
            } else {
                get<CountryApiService>()
            }
        )
    }
}

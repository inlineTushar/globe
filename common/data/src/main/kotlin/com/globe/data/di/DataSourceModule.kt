package com.globe.data.di

import com.globe.data.datasource.CountryApiDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single {
        CountryApiDataSource(get())
    }
}

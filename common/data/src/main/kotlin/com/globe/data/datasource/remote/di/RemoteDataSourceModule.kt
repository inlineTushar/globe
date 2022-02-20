package com.globe.data.datasource.remote.di

import com.globe.data.datasource.remote.CountryApiDataSource
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single { CountryApiDataSource(get()) }
}

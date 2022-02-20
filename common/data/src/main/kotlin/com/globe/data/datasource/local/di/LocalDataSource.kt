package com.globe.data.datasource.local.di

import com.globe.data.datasource.local.CountryDbDataSource
import com.globe.data.datasource.local.CountryDbDataSourceImpl
import com.globe.data.datasource.local.instrumentation.RealmKeyProvider
import com.globe.data.datasource.local.instrumentation.RealmManager
import org.koin.dsl.module

val localDataSourceModule = module {
    single { RealmKeyProvider(get()) }
    single { RealmManager(get(), get()) }
    single<CountryDbDataSource> { CountryDbDataSourceImpl(get()) }
}
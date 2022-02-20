package com.globe.data.repository.di

import com.globe.data.repository.CountryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CountryRepository(get(), get()) }
}

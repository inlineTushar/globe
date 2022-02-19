package com.globe.countrylist.di

import com.globe.countrylist.CountryListViewModel
import com.globe.countrylist.ObserveCountryListUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryListModule = module {
    viewModel {
        CountryListViewModel(get())
    }
    single {
        ObserveCountryListUseCase(get())
    }
}

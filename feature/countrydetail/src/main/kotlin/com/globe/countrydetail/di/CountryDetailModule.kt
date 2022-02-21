package com.globe.countrydetail.di

import com.globe.data.model.CountryModel
import com.globe.countrydetail.CountryDetailControllerViewModel
import com.globe.countrydetail.CountryDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryDetailModule = module {
    viewModel { (countryId: String) ->
        CountryDetailControllerViewModel(countryId, get())
    }

    viewModel { (countryModel: CountryModel) ->
        CountryDetailViewModel(countryModel)
    }
}

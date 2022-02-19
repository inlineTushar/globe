package com.globe.di

import com.globe.homecontroller.HomeControllerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        HomeControllerViewModel(get())
    }
}

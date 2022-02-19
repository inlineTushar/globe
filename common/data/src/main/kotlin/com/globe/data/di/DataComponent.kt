package com.globe.data.di

import com.globe.api.di.apiConfigModule

val dataComponent = listOf(
    apiConfigModule,
    apiModule,
    dataSourceModule,
    repositoryModule
)

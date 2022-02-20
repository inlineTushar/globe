package com.globe.data.di

import com.globe.data.datasource.local.di.localDataSourceModule
import com.globe.data.datasource.remote.di.apiConfigModule
import com.globe.data.datasource.remote.di.apiModule
import com.globe.data.datasource.remote.di.remoteDataSourceModule
import com.globe.data.repository.di.repositoryModule

val dataComponent = listOf(
    apiConfigModule,
    apiModule,
    remoteDataSourceModule,
    localDataSourceModule,
    repositoryModule
)

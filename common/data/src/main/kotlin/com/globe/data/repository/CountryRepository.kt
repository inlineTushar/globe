package com.globe.data.repository

import com.globe.data.datasource.CountryApiDataSource
import com.globe.data.datasource.CountryApiDataSource.CountryInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter

class CountryRepository(private val dataSource: CountryApiDataSource) {

    private val countryInfo: MutableStateFlow<CountryInfo?> = MutableStateFlow(null)

    suspend fun fetchCountryInfo(countryCode: String) {
        dataSource.getCountryInfo(countryCode).also {
            countryInfo.value = it
        }
    }

    fun observeCountryInfo(countryCode: String): Flow<CountryInfo?> =
        countryInfo.filter { it?.countryCode == countryCode }
}

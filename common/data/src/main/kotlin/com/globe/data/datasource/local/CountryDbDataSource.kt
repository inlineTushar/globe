package com.globe.data.datasource.local

import com.globe.data.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountryDbDataSource {
    suspend fun insert(countryList: List<CountryModel>)
    suspend fun getAllCountries(): List<CountryModel>
    fun observeCountyList(): Flow<List<CountryModel>>
}

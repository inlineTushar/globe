package com.globe.data.datasource.local

import com.globe.data.model.CountryModel

interface CountryDbDataSource {
    suspend fun insert(countryList: List<CountryModel>)
    suspend fun getAllCountries(): List<CountryModel>
    suspend fun getCountriesBySearch(keyword: String): List<CountryModel>
}

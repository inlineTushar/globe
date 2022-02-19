package com.globe.data.datasource

import com.globe.data.api.CountryApi
import com.globe.data.model.CountryApiModel

class CountryApiDataSource(private val countryApi: CountryApi) {

    suspend fun getCountryInfo(code: String): CountryInfo? =
        countryApi.getCountryInfo(code).toCountryInfo()

    data class CountryInfo(
        val name: String,
        val officialName: String,
        val countryCode: String,
        val population: Long,
        val flag: String
    )

    private fun List<CountryApiModel>.toCountryInfo(): CountryInfo? =
        takeIf { it.isNotEmpty() }
            ?.first()
            ?.let {
                CountryInfo(
                    it.countryNameApiModel.name,
                    it.countryNameApiModel.officialName,
                    it.countryCode,
                    it.population,
                    it.flag
                )
            }
}

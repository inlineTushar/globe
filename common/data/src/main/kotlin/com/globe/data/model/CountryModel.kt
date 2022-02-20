package com.globe.data.model

data class CountryModel(
    val id: String,
    val name: String,
    val officialName: String,
    val countryCode: String,
    val population: Long,
    val flag: String?,
    val capitals: List<String>,
    val currencies: List<CurrencyModel>
)

data class CurrencyModel(val id: String, val code: String, val name: String, val symbol: String)
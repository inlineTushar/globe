package com.globe.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryApiModel(
    @Json(name = "name")
    val countryNameApiModel: CountryNameApiModel,
    val cca2: String,
    val population: Long,
    val flag: String?,
    @Json(name = "capital")
    val capitals: List<String>?,
    val currencies: List<CurrencyApiModel>?
)

@JsonClass(generateAdapter = true)
data class CountryNameApiModel(
    @Json(name = "common")
    val name: String,

    @Json(name = "official")
    val officialName: String
)

data class CurrencyApiModel(val code: String, val name: String, val symbol: String)

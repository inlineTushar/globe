package com.globe.data.model

import com.globe.api.instrumentation.Currency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryApiModel(
    @Json(name = "name")
    val countryNameApiModel: CountryNameApiModel,
    @Json(name = "cca2")
    val countryCode: String,
    val population: Long,
    val flag: String?,
    @Json(name = "capital")
    val capitals: List<String>?,
    val currencies: List<Currency>?
)

@JsonClass(generateAdapter = true)
data class CountryNameApiModel(
    @Json(name = "common")
    val name: String,

    @Json(name = "official")
    val officialName: String
)

package com.globe.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryModel(
    val id: String,
    val name: String,
    val officialName: String,
    val countryCode: String,
    val population: Long,
    val flag: FlagModel?,
    val capitals: List<String>,
    val currencies: List<CurrencyModel>
) : Parcelable

@Parcelize
data class CurrencyModel(
    val id: String,
    val code: String,
    val name: String,
    val symbol: String
) : Parcelable

@Parcelize
data class FlagModel(
    val id: String,
    val unicode: String?,
    val url: String?
) : Parcelable
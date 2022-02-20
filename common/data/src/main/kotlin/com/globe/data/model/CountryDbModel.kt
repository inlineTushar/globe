package com.globe.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CountryDbModel(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var officialName: String = "",
    var countryCode: String = "",
    var population: Long = Long.MIN_VALUE,
    var flag: String? = null,
    var capitals: RealmList<String>? = null,
    var currencies: RealmList<CurrencyDbModel>? = null
) : RealmObject()

open class CurrencyDbModel(
    @PrimaryKey
    var id: String = "",
    var code: String = "",
    var name: String = "",
    var symbol: String = ""
) : RealmObject()

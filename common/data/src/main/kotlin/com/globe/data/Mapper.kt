package com.globe.data

import com.globe.data.model.*
import io.realm.RealmList

internal fun List<CountryApiModel>.toCountryInfoList(): List<CountryModel> =
    map {
        CountryModel(
            id = it.cca2.hashCode().toString(),
            name = it.countryNameApiModel.name,
            officialName = it.countryNameApiModel.officialName,
            countryCode = it.cca2,
            population = it.population,
            flag = it.flag,
            capitals = it.capitals ?: emptyList(),
            currencies = it.currencyApiModels
                ?.map { apiModel -> apiModel.toCurrencyModel() }
                ?: emptyList()
        )
    }

internal fun CurrencyApiModel.toCurrencyModel() =
    CurrencyModel(
        id = code.plus(symbol).hashCode().toString(),
        code = code,
        name = name,
        symbol = symbol
    )

internal fun CountryDbModel.toCountryModel() =
    CountryModel(
        id = id,
        name = name,
        officialName = officialName,
        countryCode = countryCode,
        population = population,
        flag = flag,
        capitals = capitals?.map { it } ?: emptyList(),
        currencies = currencies?.map { CurrencyModel(it.id, it.code, it.name, it.symbol) }
            ?: emptyList()
    )

internal fun CountryModel.toCountryDbModel() =
    CountryDbModel(
        id = id,
        name = name,
        officialName = officialName,
        countryCode = countryCode,
        population = population,
        flag = flag,
        capitals = capitals.toRealmList()
    )

internal inline fun <reified T> List<T>.toRealmList(): RealmList<T> {
    val realmList = RealmList<T>()
    realmList.addAll(this)
    return realmList
}

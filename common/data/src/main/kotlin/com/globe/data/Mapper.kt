package com.globe.data

import com.globe.data.model.*
import io.realm.RealmList

internal fun List<CountryApiModel>.toCountryInfoList(): List<CountryModel> =
    map {
        val countryId = it.cca2.hashCode().toString()
        CountryModel(
            id = countryId,
            name = it.countryNameApiModel.name,
            officialName = it.countryNameApiModel.officialName,
            countryCode = it.cca2,
            population = it.population,
            flag = it.toFlagModel(countryId),
            capitals = it.capitals ?: emptyList(),
            currencies = it.currencies
                ?.map { apiModel -> apiModel.toCurrencyModel() }
                ?: emptyList()
        )
    }

private fun CountryApiModel.toFlagModel(flagId: String): FlagModel? =
    if (!flag.isNullOrEmpty() || !flags?.pngUrl.isNullOrEmpty()) {
        FlagModel(id = flagId, unicode = flag, url = flags?.pngUrl)
    } else null

private fun CurrencyApiModel.toCurrencyModel() =
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
        flag = toFlagModel(),
        capitals = capitals?.map { it } ?: emptyList(),
        currencies = currencies?.map { it.toCurrencyModel() } ?: emptyList()
    )

private fun CurrencyDbModel.toCurrencyModel() = CurrencyModel(id, code, name, symbol)

private fun CurrencyModel.toCurrencyDbModel() = CurrencyDbModel(id, code, name, symbol)

private fun CountryDbModel.toFlagModel(): FlagModel? =
    flag?.id?.let { FlagModel(it, flag?.unicode, flag?.url) }

internal fun CountryModel.toCountryDbModel() =
    CountryDbModel(
        id = id,
        name = name,
        officialName = officialName,
        countryCode = countryCode,
        population = population,
        flag = toFlagDbModel(),
        capitals = capitals.toRealmList(),
        currencies = currencies.toRealmList { it.toCurrencyDbModel() }
    )

private fun CountryModel.toFlagDbModel() = flag?.id?.let { FlagDbModel(it, flag.unicode, flag.url) }

private inline fun <reified T> List<T>.toRealmList(): RealmList<T> {
    val realmList = RealmList<T>()
    realmList.addAll(this)
    return realmList
}

private inline fun <reified T, E> List<T>.toRealmList(mapper: (T) -> E): RealmList<E> {
    val realmList = RealmList<E>()
    realmList.addAll(this.map(mapper))
    return realmList
}

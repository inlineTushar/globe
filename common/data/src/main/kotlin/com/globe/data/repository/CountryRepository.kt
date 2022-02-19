package com.globe.data.repository

import arrow.core.Either
import com.globe.data.datasource.CountryApiDataSource
import com.globe.data.model.CountryApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class CountryRepository(private val dataSource: CountryApiDataSource) {

    private val countryModel: MutableStateFlow<List<CountryModel>> = MutableStateFlow(emptyList())

    suspend fun fetchAllCountries(): Either<Throwable, Unit> =
        try {
            dataSource.getAllCountries().toCountryInfoList().also { countryModel.value = it }
            Either.Right(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Either.Left(e)
        }

    fun observeAllCountriesInfo(): Flow<List<CountryModel>> = countryModel
}

data class CountryModel(
    val name: String,
    val officialName: String,
    val countryCode: String,
    val population: Long,
    val flag: String?,
    val capital: String?,
)

private fun List<CountryApiModel>.toCountryInfoList(): List<CountryModel> =
    map {
        CountryModel(
            it.countryNameApiModel.name,
            it.countryNameApiModel.officialName,
            it.countryCode,
            it.population,
            it.flag,
            it.capitals?.takeIf { capitals -> capitals.isNotEmpty() }?.first()
        )
    }

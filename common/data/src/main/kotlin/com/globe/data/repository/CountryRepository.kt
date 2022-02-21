package com.globe.data.repository

import arrow.core.Either
import com.globe.data.datasource.local.CountryDbDataSource
import com.globe.data.datasource.remote.CountryApiDataSource
import com.globe.data.exception.NetworkException
import com.globe.data.extension.NetworkCheck
import com.globe.data.model.CountryModel
import com.globe.data.toCountryInfoList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber

class CountryRepository(
    private val remoteDataSource: CountryApiDataSource,
    private val localDataSource: CountryDbDataSource,
    private val networkCheck: NetworkCheck
) {

    private val countries: MutableSharedFlow<List<CountryModel>> = MutableSharedFlow(replay = 1)

    suspend fun fetchAllCountries(keyword: String = ""): Either<Throwable, Unit> {
        with(getLocalSource(keyword)) {
            if (isNotEmpty()) {
                countries.emit(this)
                return Either.Right(Unit)
            } else {
                if (!networkCheck.isConnected) {
                    return Either.Left(NetworkException)
                }
                try {
                    with(
                        remoteDataSource
                            .getAllCountries()
                            .toCountryInfoList()
                    ) {
                        localDataSource.insert(countryList = this)
                        countries.emit(getLocalSource(keyword))
                        return Either.Right(Unit)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    return Either.Left(e)
                }
            }
        }
    }

    private suspend fun getLocalSource(keyword: String) =
        if (keyword.isEmpty()) localDataSource.getAllCountries()
        else localDataSource.getCountriesBySearch(keyword)


    fun observeAllCountriesInfo(): Flow<List<CountryModel>> = countries

    fun getCountryDetail(countryId: String): CountryModel? =
        countries.replayCache.firstOrNull()?.firstOrNull { it.id == countryId }
}

package com.globe.data.repository

import arrow.core.Either
import com.globe.data.datasource.local.CountryDbDataSource
import com.globe.data.datasource.remote.CountryApiDataSource
import com.globe.data.model.CountryModel
import com.globe.data.toCountryInfoList
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class CountryRepository(
    private val remoteDataSource: CountryApiDataSource,
    private val localDataSource: CountryDbDataSource
) {

    suspend fun fetchAllCountries(): Either<Throwable, Unit> {
        if (localDataSource.getAllCountries().isNotEmpty()) {
            return Either.Right(Unit)
        } else {
            try {
                remoteDataSource
                    .getAllCountries()
                    .toCountryInfoList()
                    .also {
                        localDataSource.insert(it)
                        return Either.Right(Unit)
                    }
            } catch (e: Exception) {
                Timber.e(e)
                return Either.Left(e)
            }
        }
    }


    fun observeAllCountriesInfo(): Flow<List<CountryModel>> = localDataSource.observeCountyList()
}

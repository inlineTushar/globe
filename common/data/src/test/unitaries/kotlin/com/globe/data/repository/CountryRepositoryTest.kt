package com.globe.data.repository

import app.cash.turbine.test
import arrow.core.Either
import com.globe.data.datasource.local.CountryDbDataSource
import com.globe.data.datasource.remote.CountryApiDataSource
import com.globe.data.exception.NetworkException
import com.globe.data.extension.NetworkCheck
import com.globe.data.model.CountryModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class CountryRepositoryTest {

    private val mockRemoteDataSource: CountryApiDataSource = mockk()
    private val mockLocalDataSource: CountryDbDataSource = mockk()
    private val mockNetworkCheck: NetworkCheck = mockk()
    private val mockCountryId = "anyId"
    private val mockCountryModelList = listOf(mockk<CountryModel>() {
        every { id } returns mockCountryId
    })
    private lateinit var repository: CountryRepository

    @BeforeEach
    fun setUp() {
        repository = CountryRepository(mockRemoteDataSource, mockLocalDataSource, mockNetworkCheck)
        coEvery { mockLocalDataSource.insert(any()) } coAnswers {}
    }


    @Test
    fun `Should emit country list when network is available`() = runTest {
        every { mockNetworkCheck.isConnected } returns true
        coEvery { mockLocalDataSource.getAllCountries() } returns emptyList()
        coEvery { mockRemoteDataSource.getAllCountries() } returns emptyList()

        assertThat(repository.fetchCountries()).isEqualTo(Either.Right(Unit))
    }

    @Test
    fun `Should return exception when network is not available and local storage is empty`() =
        runTest {
            every { mockNetworkCheck.isConnected } returns false
            coEvery { mockLocalDataSource.getAllCountries() } returns emptyList()

            assertThat(repository.fetchCountries()).isEqualTo(Either.Left(NetworkException))
        }

    @Test
    fun `Should return exception when network is available and error happens in API call`() =
        runTest {
            val exception = Exception()
            every { mockNetworkCheck.isConnected } returns true
            coEvery { mockLocalDataSource.getAllCountries() } returns emptyList()
            coEvery { mockRemoteDataSource.getAllCountries() } throws exception

            assertThat(repository.fetchCountries()).isEqualTo(Either.Left(exception))
        }

    @Test
    fun `Should return country list from local source`() =
        runTest {
            coEvery { mockLocalDataSource.getAllCountries() } returns mockCountryModelList
            assertThat(repository.fetchCountries()).isEqualTo(Either.Right(Unit))
        }

    @Test
    fun `Should observe country list as expected`() =
        runTest {
            coEvery { mockLocalDataSource.getAllCountries() } returns mockCountryModelList
            repository.observeCountries().test {
                repository.fetchCountries()
                assertEquals(mockCountryModelList, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `Should get expected country by id`() =
        runTest {
            coEvery { mockLocalDataSource.getAllCountries() } returns mockCountryModelList
            repository.fetchCountries()
            assertEquals(repository.getCountry(mockCountryId), mockCountryModelList[0])
        }

    @Test
    fun `Should return country list by keyword search`() =
        runTest {
            coEvery { mockLocalDataSource.getCountriesBySearch(any()) } returns mockCountryModelList
            assertThat(repository.fetchCountries("any country")).isEqualTo(Either.Right(Unit))
        }
}

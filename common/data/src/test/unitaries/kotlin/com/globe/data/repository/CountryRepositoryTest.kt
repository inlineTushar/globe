package com.globe.data.repository

import app.cash.turbine.test
import arrow.core.Either
import com.globe.data.datasource.local.CountryDbDataSource
import com.globe.data.datasource.remote.CountryApiDataSource
import com.globe.data.exception.NetworkException
import com.globe.data.extension.NetworkCheck
import com.globe.data.model.CountryModel
import com.globe.unittestingtools.MainCoroutineScopeExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MainCoroutineScopeExtension::class)
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
    fun `Should emit country list when network is available`() = runBlockingTest {
        every { mockNetworkCheck.isConnected } returns true
        coEvery { mockLocalDataSource.getAllCountries() } returns emptyList()
        coEvery { mockRemoteDataSource.getAllCountries() } returns emptyList()

        assertThat(repository.fetchAllCountries()).isEqualTo(Either.Right(Unit))
    }

    @Test
    fun `Should return exception when network is not available and local storage is empty`() =
        runBlockingTest {
            every { mockNetworkCheck.isConnected } returns false
            coEvery { mockLocalDataSource.getAllCountries() } returns emptyList()

            assertThat(repository.fetchAllCountries()).isEqualTo(Either.Left(NetworkException))
        }

    @Test
    fun `Should return exception when network is available and error happens in API call`() =
        runBlockingTest {
            val exception = Exception()
            every { mockNetworkCheck.isConnected } returns true
            coEvery { mockLocalDataSource.getAllCountries() } returns emptyList()
            coEvery { mockRemoteDataSource.getAllCountries() } throws exception

            assertThat(repository.fetchAllCountries()).isEqualTo(Either.Left(exception))
        }

    @Test
    fun `Should return country list from local source`() =
        runBlockingTest {
            coEvery { mockLocalDataSource.getAllCountries() } returns mockCountryModelList
            assertThat(repository.fetchAllCountries()).isEqualTo(Either.Right(Unit))
        }

    @Test
    fun `Should observe country list as expected`() =
        runBlockingTest {
            coEvery { mockLocalDataSource.getAllCountries() } returns mockCountryModelList
            repository.observeCountriesInfo().test {
                repository.fetchAllCountries()
                assertEquals(mockCountryModelList, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `Should get expected country by id`() =
        runBlockingTest {
            coEvery { mockLocalDataSource.getAllCountries() } returns mockCountryModelList
            repository.fetchAllCountries()
            assertEquals(repository.getCountryDetail(mockCountryId), mockCountryModelList[0])
        }

    @Test
    fun `Should return country list by keyword search`() =
        runBlockingTest {
            coEvery { mockLocalDataSource.getCountriesBySearch(any()) } returns mockCountryModelList
            assertThat(repository.fetchAllCountries("any country")).isEqualTo(Either.Right(Unit))
        }
}

package com.globe

import app.cash.turbine.test
import arrow.core.Either
import com.globe.data.exception.NetworkException
import com.globe.data.extension.NetworkCheck
import com.globe.data.extension.NetworkStatus
import com.globe.data.model.CountryModel
import com.globe.data.repository.CountryRepository
import com.globe.homecontroller.HomeControllerViewModel
import com.globe.homecontroller.HomeControllerViewModel.ViewState
import com.globe.unittestingtools.MainCoroutineScopeExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.annotation.Repeatable
import kotlin.test.assertEquals

@ExtendWith(MainCoroutineScopeExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeControllerViewModelTest {
    private var mockCountryList: List<CountryModel> = listOf(mockk())
    private var mockCountryRepository: CountryRepository = mockk()
    private var mockNetworkCheck: NetworkCheck = mockk()
    private var viewModel: HomeControllerViewModel =
        HomeControllerViewModel(mockCountryRepository, mockNetworkCheck)

    @Test
    fun `Should emit Loading state`() = runBlockingTest {
        coEvery { mockCountryRepository.fetchAllCountries() } returns Either.Right(Unit)
        coEvery { mockCountryRepository.observeCountriesInfo() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit Data state`() = runBlockingTest {
        coEvery { mockCountryRepository.fetchAllCountries() } returns Either.Right(Unit)
        coEvery { mockCountryRepository.observeCountriesInfo() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.Data, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit NetworkError state`() = runBlockingTest {
        coEvery { mockCountryRepository.fetchAllCountries() } returns Either.Left(NetworkException)
        coEvery { mockCountryRepository.observeCountriesInfo() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.NetworkError, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit GenericError state`() = runBlockingTest {
        coEvery { mockCountryRepository.fetchAllCountries() } returns Either.Left(RuntimeException())
        coEvery { mockCountryRepository.observeCountriesInfo() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.GenericError, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit state as expected`() = runBlockingTest {
        coEvery { mockCountryRepository.fetchAllCountries() } coAnswers { Either.Left(NetworkException) } coAndThen { Either.Right(Unit) }
        coEvery { mockCountryRepository.observeCountriesInfo() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } coAnswers { flowOf(NetworkStatus.Unavailable) } coAndThen { flowOf(NetworkStatus.Available) }
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.NetworkError, awaitItem())
            assertEquals(ViewState.Data, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}

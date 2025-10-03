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
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeControllerViewModelTest {
    private var mockCountryList: List<CountryModel> = listOf(mockk())
    private var mockCountryRepository: CountryRepository = mockk()
    private var mockNetworkCheck: NetworkCheck = mockk()
    private lateinit var viewModel: HomeControllerViewModel

    @BeforeEach
    fun setUp() {
        viewModel = HomeControllerViewModel(mockCountryRepository, mockNetworkCheck)
    }

    @Test
    fun `Should emit Loading state`(): Unit = runTest {
        coEvery { mockCountryRepository.fetchCountries() } returns Either.Right(Unit)
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit Data state`(): Unit = runTest {
        coEvery { mockCountryRepository.fetchCountries() } returns Either.Right(Unit)
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.Data, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit NetworkError state`(): Unit = runTest {
        coEvery { mockCountryRepository.fetchCountries() } returns Either.Left(NetworkException)
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.NetworkError, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit GenericError state`(): Unit = runTest {
        coEvery { mockCountryRepository.fetchCountries() } returns Either.Left(RuntimeException())
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flowOf(NetworkStatus.Available)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.GenericError, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit state as expected`(): Unit = runTest {
        coEvery { mockCountryRepository.fetchCountries() } returnsMany listOf(
            Either.Left(NetworkException),
            Either.Right(Unit)
        )
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        coEvery { mockNetworkCheck.changes() } returns flow {
            emit(NetworkStatus.Unavailable)
            emit(NetworkStatus.Available)
        }
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState.Loading, awaitItem())
            assertEquals(ViewState.NetworkError, awaitItem())
            assertEquals(ViewState.Data, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}

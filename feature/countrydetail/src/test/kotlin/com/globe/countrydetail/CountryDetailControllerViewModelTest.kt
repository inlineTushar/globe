package com.globe.countrydetail

import app.cash.turbine.test
import com.globe.countrydetail.CountryDetailControllerViewModel.ViewState
import com.globe.data.model.CountryModel
import com.globe.data.repository.CountryRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CountryDetailControllerViewModelTest {
    private val mockCountryRepository: CountryRepository = mockk()
    private val viewModel: CountryDetailControllerViewModel =
        CountryDetailControllerViewModel("any_country_id", mockCountryRepository)

    @Test
    fun `Should emit Data state`() = runBlockingTest {
        val mockCountryModel: CountryModel = mockk()
        every { mockCountryRepository.getCountry(any()) } returns mockCountryModel
        viewModel.onCreate()
        viewModel.viewState.test {
            assertEquals(ViewState.Data(mockCountryModel), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should emit Empty state`() = runBlockingTest {
        every { mockCountryRepository.getCountry(any()) } returns null
        viewModel.onCreate()
        viewModel.viewState.test {
            assertEquals(ViewState.Empty, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}

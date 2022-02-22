package com.globe.countrylist

import app.cash.turbine.test
import com.globe.countrylist.CountryListViewModel.NavState
import com.globe.countrylist.CountryListViewModel.ViewState
import com.globe.data.model.CountryModel
import com.globe.data.repository.CountryRepository
import com.globe.unittestingtools.MainCoroutineScopeExtension
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MainCoroutineScopeExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CountryListViewModelTest {
    private val mockCountryRepository: CountryRepository = mockk()
    private val mockCountryId = "anyId"
    private val mockCountryList: List<CountryModel> =
        listOf(mockk { every { id } returns mockCountryId })
    private val viewModel: CountryListViewModel = CountryListViewModel(mockCountryRepository)

    @Test
    fun `Should emit ViewState`() = runBlockingTest {
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        viewModel.viewState.test {
            viewModel.onCreate()
            assertEquals(ViewState(mockCountryList), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Should not emit ViewState`() = runBlockingTest {
        coEvery { mockCountryRepository.observeCountries() } returns flow { throw Exception() }
        viewModel.onCreate()
        viewModel.viewState.test { expectNoEvents() }
    }

    @Test
    fun `Should emit NavState when country is tapped`() = runBlockingTest {
        coEvery { mockCountryRepository.observeCountries() } returns flowOf(mockCountryList)
        viewModel.navState.test {
            viewModel.onCountryTapped(mockCountryList[0])
            assertEquals(NavState(mockCountryId), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}

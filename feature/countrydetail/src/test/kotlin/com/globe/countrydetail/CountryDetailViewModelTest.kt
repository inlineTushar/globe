package com.globe.countrydetail

import app.cash.turbine.test
import com.globe.countrydetail.CountryDetailViewModel.ViewState
import com.globe.data.model.CountryModel
import com.globe.unittestingtools.MainCoroutineScopeExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MainCoroutineScopeExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CountryDetailViewModelTest {
    private val mockCountryModel: CountryModel = mockk()
    private val viewModel: CountryDetailViewModel = CountryDetailViewModel(mockCountryModel)

    @Test
    fun `Should emit ViewState`() = runBlockingTest {
        viewModel.onCreate()
        viewModel.viewState.test {
            assertEquals(ViewState(mockCountryModel), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}

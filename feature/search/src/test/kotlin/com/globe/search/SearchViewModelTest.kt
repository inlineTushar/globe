package com.globe.search

import com.globe.data.repository.CountryRepository
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class SearchViewModelTest {
    private val mockCountryRepository: CountryRepository = mockk()
    private val viewModel: SearchViewModel = SearchViewModel(mockCountryRepository)

    @Test
    fun `Should trigger fetch in repository`() {
        val keyword = "Spa"
        viewModel.searchCounties(keyword)
        coVerify { mockCountryRepository.fetchCountries(keyword) }
    }
}

package com.tushar.countrydetail

import com.globe.data.model.CountryModel
import com.globe.data.repository.CountryRepository
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountryDetailControllerViewModel(
    private val countryId: String,
    private val countryRepository: CountryRepository
) : BaseViewModel() {

    sealed class ViewState {
        data class Data(val countryModel: CountryModel) : ViewState()
        object Empty : ViewState()
    }

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Empty)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    fun onCreate() {
        countryRepository.getCountryDetail(countryId)?.let {
            coroutineWrapper { _viewState.emit(ViewState.Data(it)) }
        } ?: coroutineWrapper { _viewState.emit(ViewState.Empty) }
    }
}

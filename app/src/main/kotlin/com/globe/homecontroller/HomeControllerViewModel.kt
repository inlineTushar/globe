package com.globe.homecontroller

import com.globe.data.repository.CountryRepository
import com.globe.extension.onFailure
import com.globe.extension.onSuccess
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeControllerViewModel(
    private val countryRepository: CountryRepository
) : BaseViewModel() {

    sealed class ViewState {
        object Loading : ViewState()
        object Data : ViewState()
        object Error : ViewState()
    }

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    fun onCreate() {
        fetchCounties()
    }

    private fun fetchCounties() {
        coroutineWrapper {
            countryRepository.fetchAllCountries()
                .onSuccess { coroutineWrapper { _viewState.emit(ViewState.Data) } }
                .onFailure { coroutineWrapper { _viewState.emit(ViewState.Error) } }
        }
    }
}

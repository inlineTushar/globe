package com.globe.homecontroller

import com.globe.countrylist.ObserveCountryListUseCase
import com.globe.data.repository.CountryRepository
import com.globe.extension.onFailure
import com.globe.extension.onSuccess
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class HomeControllerViewModel(
    private val countryRepository: CountryRepository,
    private val observeCountryUseCase: ObserveCountryListUseCase
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
        observeCountries()
    }

    private fun fetchCounties() {
        coroutineWrapper {
            countryRepository.fetchAllCountries()
                .onSuccess { coroutineWrapper { _viewState.emit(ViewState.Data) } }
                .onFailure { coroutineWrapper { _viewState.emit(ViewState.Error) } }
        }
    }

    private fun observeCountries() {
        observeCountryUseCase.observe()
            .onEach {
                coroutineWrapper {
                    if (_viewState.value == ViewState.Error && it.isNotEmpty()) {
                        _viewState.emit(ViewState.Data)
                    }
                }
            }
            .catch { Timber.e(it) }
            .launchIn(this)
    }
}

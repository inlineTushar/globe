package com.globe.homecontroller

import com.globe.countrylist.ObserveCountryListUseCase
import com.globe.data.exception.NetworkException
import com.globe.data.extension.NetworkCheck
import com.globe.data.extension.NetworkStatus
import com.globe.data.repository.CountryRepository
import com.globe.extension.onFailure
import com.globe.extension.onSuccess
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class HomeControllerViewModel(
    private val countryRepository: CountryRepository,
    private val observeCountryUseCase: ObserveCountryListUseCase,
    private val networkCheck: NetworkCheck
) : BaseViewModel() {

    sealed class ViewState {
        object Loading : ViewState()
        object Data : ViewState()
        object GenericError : ViewState()
        object NetworkError : ViewState()
    }

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    fun onCreate() {
        fetchCounties()
        observeCountries()
        observeNetwork()
    }

    private fun fetchCounties() {
        coroutineWrapper {
            _viewState.emit(ViewState.Loading)
            countryRepository.fetchAllCountries()
                .onSuccess { coroutineWrapper { _viewState.emit(ViewState.Data) } }
                .onFailure {
                    coroutineWrapper {
                        _viewState.emit(
                            if (it is NetworkException)
                                ViewState.NetworkError
                            else
                                ViewState.GenericError
                        )
                    }
                }
        }
    }

    private fun observeCountries() {
        observeCountryUseCase.observe()
            .onEach {
                coroutineWrapper {
                    if (_viewState.value == ViewState.GenericError ||
                        _viewState.value == ViewState.NetworkError &&
                        it.isNotEmpty()
                    ) {
                        _viewState.emit(ViewState.Data)
                    }
                }
            }
            .catch { Timber.e(it) }
            .launchIn(this)
    }

    private fun observeNetwork() {
        networkCheck
            .changes()
            .onEach { networkStatus ->
                if (shouldRetryFetch(_viewState.value, networkStatus)) {
                    fetchCounties()
                }
            }
            .launchIn(this)
    }

    private fun shouldRetryFetch(state: ViewState, status: NetworkStatus) =
        state == ViewState.NetworkError && status is NetworkStatus.Available
}

package com.globe.homecontroller

import com.globe.data.exception.NetworkException
import com.globe.data.extension.NetworkCheck
import com.globe.data.extension.NetworkStatus
import com.globe.data.repository.CountryRepository
import com.globe.platform.BaseViewModel
import com.globe.platform.extension.onFailure
import com.globe.platform.extension.onSuccess
import kotlinx.coroutines.flow.*
import timber.log.Timber

class HomeControllerViewModel(
    private val countryRepository: CountryRepository,
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
            countryRepository.fetchCountries()
                .onSuccess { coroutineWrapper { _viewState.emit(ViewState.Data) } }
                .onFailure { coroutineWrapper { _viewState.emit(it.toState()) } }
        }
    }

    private fun observeCountries() {
        countryRepository.observeCountries()
            .onEach {
                coroutineWrapper {
                    if (isErrorState(_viewState.value) && it.isNotEmpty()) {
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

    private fun Throwable.toState() =
        if (this is NetworkException) ViewState.NetworkError else ViewState.GenericError

    private fun isErrorState(state: ViewState) =
        state == ViewState.GenericError || state == ViewState.NetworkError

    private fun shouldRetryFetch(state: ViewState, status: NetworkStatus) =
        state == ViewState.NetworkError && status is NetworkStatus.Available
}

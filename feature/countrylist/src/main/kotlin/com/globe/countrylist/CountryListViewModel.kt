package com.globe.countrylist

import com.globe.data.repository.CountryModel
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import timber.log.Timber

class CountryListViewModel(
    private val countryListUseCase: ObserveCountryListUseCase
) : BaseViewModel() {

    data class ViewState(val countries: List<CountryModel>)

    private val _viewState: MutableSharedFlow<ViewState> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val viewState: SharedFlow<ViewState> = _viewState.asSharedFlow()

    fun onCreate() {
        observeCountryInfo()
    }

    private fun observeCountryInfo() {
        countryListUseCase.observe()
            .onEach { coroutineWrapper { _viewState.emit(ViewState(it)) } }
            .catch { Timber.e(it) }
            .launchIn(this)
    }

    fun onCountryTapped(country: CountryModel) {
        //TODO:: set action here
    }
}

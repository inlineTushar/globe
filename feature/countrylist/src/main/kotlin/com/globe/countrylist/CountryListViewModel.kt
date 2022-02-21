package com.globe.countrylist

import com.globe.data.model.CountryModel
import com.globe.data.repository.CountryRepository
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import timber.log.Timber

class CountryListViewModel(
    private val countryRepository: CountryRepository
) : BaseViewModel() {

    data class ViewState(val countries: List<CountryModel>)

    data class NavState(val countryId: String)

    private val _viewState: MutableSharedFlow<ViewState> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val viewState: SharedFlow<ViewState> = _viewState.asSharedFlow()

    private val _navState: MutableSharedFlow<NavState> = MutableSharedFlow()
    val navState: SharedFlow<NavState> = _navState.asSharedFlow()

    fun onCreate() {
        observeCountryInfo()
    }

    private fun observeCountryInfo() {
        countryRepository.observeCountriesInfo()
            .onEach { coroutineWrapper { _viewState.emit(ViewState(it)) } }
            .catch { Timber.e(it) }
            .launchIn(this)
    }

    fun onCountryTapped(country: CountryModel) {
        coroutineWrapper { _navState.emit(NavState(country.id)) }
    }
}

package com.globe.countrydetail

import com.globe.data.model.CountryModel
import com.globe.platform.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CountryDetailViewModel(
    private val countryModel: CountryModel,
) : BaseViewModel() {

    data class ViewState(val countryModel: CountryModel)

    private val _viewState: MutableSharedFlow<ViewState> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val viewState: SharedFlow<ViewState> = _viewState.asSharedFlow()

    fun onCreate() {
        coroutineWrapper { _viewState.emit(ViewState(countryModel)) }
    }
}

package com.globe.homecontroller

import com.globe.platform.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class HomeControllerViewModel() : BaseViewModel() {

    sealed class ViewState {
        object Loading : ViewState()
        object Data : ViewState()
        object Error : ViewState()
    }

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    fun onCreate() {
        fetch()
    }

    private fun fetch() {

    }
}

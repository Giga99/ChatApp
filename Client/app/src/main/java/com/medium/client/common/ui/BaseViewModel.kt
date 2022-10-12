package com.medium.client.common.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<ViewState, Event, SideEffect>(
    viewState: ViewState
) : ViewModel() {

    private val _uiState = MutableStateFlow(viewState)
    val uiState = _uiState.asStateFlow()

    protected val _sideEffects = Channel<SideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    protected fun setState(reducer: ViewState.() -> ViewState) {
        _uiState.update(reducer)
    }

    protected fun getState(): ViewState {
        return _uiState.value
    }

    abstract fun onEvent(event: Event)
}

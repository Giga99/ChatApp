/*
 * Created by Igor Stevanovic on 9/16/22, 9:26 PM
 * igorstevanovic99@gmail.com
 * Last modified 9/16/22, 9:26 PM
 * Copyright (c) 2022.
 * All rights reserved.
 */

package com.tech4health.androidclient.common.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<ViewState, Event, SideEffect>(
    viewState: ViewState
) : ViewModel() {

    private val _viewState = MutableStateFlow(viewState)
    val viewState = _viewState.asStateFlow()

    protected val _sideEffects = Channel<SideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    protected fun setState(reducer: ViewState.() -> ViewState) {
        _viewState.update(reducer)
    }

    protected fun getState(): ViewState {
        return _viewState.value
    }

    abstract fun onEvent(event: Event)
}

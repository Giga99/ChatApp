package com.medium.client.presentation

import androidx.lifecycle.viewModelScope
import com.medium.client.common.core.Result
import com.medium.client.common.ui.BaseViewModel
import com.medium.client.common.wrappers.session_manager.ChatAppSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatAppSessionManager: ChatAppSessionManager
) : BaseViewModel<MainViewState, Unit, Unit>(MainViewState()) {

    init {
        viewModelScope.launch {
            setState { copy(sessionStatus = Result.Loading()) }
            chatAppSessionManager.observeSessionStatus().collect {
                setState { copy(sessionStatus = Result.Success(it)) }
            }
        }
    }

    override fun onEvent(event: Unit) = Unit
}

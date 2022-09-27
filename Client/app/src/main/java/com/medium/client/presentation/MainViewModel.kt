package com.medium.client.presentation

import androidx.lifecycle.viewModelScope
import com.medium.client.common.core.Result
import com.medium.client.common.ui.BaseViewModel
import com.medium.client.common.wrappers.connectivity.NetworkConnectivityManager
import com.medium.client.common.wrappers.session_manager.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val networkConnectivityManager: NetworkConnectivityManager
) : BaseViewModel<MainViewState, Unit, Unit>(MainViewState()) {

    init {
        viewModelScope.launch {
            setState { copy(sessionStatus = Result.Loading()) }
            sessionManager.observeSessionStatus().collect { sessionStatus ->
                setState { copy(sessionStatus = Result.Success(sessionStatus)) }
            }
        }

        viewModelScope.launch {
            networkConnectivityManager.observeNetworkStatus().collect { networkStatus ->
                setState { copy(networkStatus = networkStatus) }
            }
        }
    }

    override fun onEvent(event: Unit) = Unit
}

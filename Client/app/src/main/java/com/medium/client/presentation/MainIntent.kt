package com.medium.client.presentation

import com.medium.client.common.core.Result
import com.medium.client.common.wrappers.connectivity.NetworkStatus
import com.medium.client.common.wrappers.session_manager.SessionStatus

data class MainViewState(
    val sessionStatus: Result<SessionStatus> = Result.Loading(),
    val networkStatus: NetworkStatus = NetworkStatus.CONNECTED
)

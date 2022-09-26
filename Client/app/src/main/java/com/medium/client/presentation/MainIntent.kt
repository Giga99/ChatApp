package com.medium.client.presentation

import com.medium.client.common.core.Result
import com.medium.client.common.wrappers.session_manager.ChatAppSessionManager.SessionStatus

data class MainViewState(
    val sessionStatus: Result<SessionStatus> = Result.Loading()
)

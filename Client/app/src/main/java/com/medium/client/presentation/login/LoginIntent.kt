package com.medium.client.presentation.login

import com.medium.client.common.core.Result
import com.medium.client.presentation.register.RegisterEvent

sealed class LoginEvent {
    data class UsernameInputChanged(val username: String) : LoginEvent()
    data class PasswordInputChanged(val password: String) : LoginEvent()
    object DoneButtonClicked : LoginEvent()
    object LoginButtonClicked : LoginEvent()
    object SignUpTextClicked : LoginEvent()
    object ClearUsernameInputButtonClicked : LoginEvent()
    object TogglePasswordHidden : LoginEvent()
}

sealed class LoginSideEffect {
    object NavigateToHomeScreen : LoginSideEffect()
    object NavigateToRegisterScreen : LoginSideEffect()
}

data class LoginViewState(
    val username: String = "",
    val password: String = "",
    val passwordHidden: Boolean = true,
    val loginResult: Result<Unit> = Result.Success(Unit),
)

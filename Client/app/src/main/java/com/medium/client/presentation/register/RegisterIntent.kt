package com.medium.client.presentation.register

import com.medium.client.common.core.Result

sealed class RegisterEvent {
    data class UsernameInputChanged(val username: String) : RegisterEvent()
    data class EmailInputChanged(val email: String) : RegisterEvent()
    data class PasswordInputChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordInputChanged(val confirmPassword: String) : RegisterEvent()
    object DoneButtonClicked : RegisterEvent()
    object RegisterButtonClicked : RegisterEvent()
    object BackButtonClicked : RegisterEvent()
    object ClearUsernameInputButtonClicked : RegisterEvent()
    object ClearEmailInputButtonClicked : RegisterEvent()
    object TogglePasswordHidden : RegisterEvent()
    object ToggleConfirmPasswordHidden : RegisterEvent()
}

sealed class RegisterSideEffect {
    object NavigateToHomeScreen : RegisterSideEffect()
    object NavigateBack : RegisterSideEffect()
}

data class RegisterViewState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordHidden: Boolean = true,
    val confirmPasswordHidden: Boolean = true,
    val registerResult: Result<Unit> = Result.Success(Unit),
)

package com.medium.client.presentation.register

import androidx.lifecycle.viewModelScope
import com.medium.client.common.core.Result
import com.medium.client.common.ui.BaseViewModel
import com.medium.client.domain.models.requests.RegisterRequest
import com.medium.client.domain.usecases.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUser: RegisterUser
) : BaseViewModel<RegisterViewState, RegisterEvent, RegisterSideEffect>(RegisterViewState()) {

    override fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameInputChanged -> setState { copy(username = event.username) }
            is RegisterEvent.EmailInputChanged -> setState { copy(email = event.email) }
            is RegisterEvent.PasswordInputChanged -> setState { copy(password = event.password) }
            is RegisterEvent.ConfirmPasswordInputChanged -> setState { copy(confirmPassword = event.confirmPassword) }
            is RegisterEvent.RegisterButtonClicked, RegisterEvent.DoneButtonClicked -> {
                setState { copy(registerResult = Result.Loading()) }
                viewModelScope.launch {
                    val result = registerUser(
                        RegisterRequest(
                            username = getState().username,
                            email = getState().email,
                            password = getState().password,
                            confirmPassword = getState().confirmPassword
                        )
                    )
                    setState { copy(registerResult = result) }
                    if (result is Result.Success) {
                        _sideEffects.trySend(RegisterSideEffect.NavigateToHomeScreen)
                    }
                }
            }
            is RegisterEvent.BackButtonClicked -> _sideEffects.trySend(RegisterSideEffect.NavigateBack)
            is RegisterEvent.ClearUsernameInputButtonClicked -> setState { copy(username = "") }
            is RegisterEvent.ClearEmailInputButtonClicked -> setState { copy(email = "") }
            is RegisterEvent.TogglePasswordHidden -> setState { copy(passwordHidden = !passwordHidden) }
            is RegisterEvent.ToggleConfirmPasswordHidden -> setState { copy(confirmPasswordHidden = !confirmPasswordHidden) }
        }
    }
}

package com.medium.client.presentation.login

import androidx.lifecycle.viewModelScope
import com.medium.client.common.core.Result
import com.medium.client.common.ui.BaseViewModel
import com.medium.client.domain.models.requests.LoginRequest
import com.medium.client.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<LoginViewState, LoginEvent, LoginSideEffect>(LoginViewState()) {

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameInputChanged -> setState { copy(username = event.username) }
            is LoginEvent.PasswordInputChanged -> setState { copy(password = event.password) }
            is LoginEvent.LoginButtonClicked -> {
                setState { copy(loginResult = Result.Loading()) }
                viewModelScope.launch {
                    val result = authRepository.login(
                        LoginRequest(
                            username = getState().username,
                            password = getState().password
                        )
                    )
                    setState { copy(loginResult = result) }
                    if (result is Result.Success) {
                        _sideEffects.trySend(LoginSideEffect.NavigateToHomeScreen)
                    }
                }
            }
            is LoginEvent.RegisterTextClicked -> Unit
            is LoginEvent.ClearUsernameInputButtonClicked -> setState { copy(username = "") }
            is LoginEvent.TogglePasswordHidden -> setState { copy(passwordHidden = !getState().passwordHidden) }
        }
    }
}

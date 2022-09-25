package com.medium.client.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.medium.client.R
import com.medium.client.common.core.Result
import com.medium.client.presentation.destinations.HomeScreenDestination
import com.medium.client.presentation.ui.ChatAppInputField
import com.medium.client.presentation.ui.ChatAppPrimaryButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val viewState = loginViewModel.viewState.collectAsState().value
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(loginViewModel.sideEffects) {
        loginViewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.NavigateToHomeScreen -> navigator.navigate(HomeScreenDestination)
                is LoginSideEffect.NavigateToRegisterScreen -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.size_16))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_64)))
        Text(
            text = stringResource(R.string.welcome_to_chat_app),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_64)))
        ChatAppInputField(
            value = viewState.username,
            onValueChange = { loginViewModel.onEvent(LoginEvent.UsernameInputChanged(it)) },
            placeholderText = stringResource(R.string.username),
            trailingIcon = painterResource(R.drawable.ic_clear_button),
            onTrailingIconClick = { loginViewModel.onEvent(LoginEvent.ClearUsernameInputButtonClicked) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_16)))
        ChatAppInputField(
            value = viewState.password,
            onValueChange = { loginViewModel.onEvent(LoginEvent.PasswordInputChanged(it)) },
            placeholderText = stringResource(R.string.password),
            trailingIcon = if (viewState.passwordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            onTrailingIconClick = { loginViewModel.onEvent(LoginEvent.TogglePasswordHidden) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    loginViewModel.onEvent(LoginEvent.LoginButtonClicked)
                }
            ),
            visualTransformation = if (viewState.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        )
        if (viewState.loginResult is Result.Error) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_32)))
            Text(
                text = viewState.loginResult.message ?: "",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_64)))
        ChatAppPrimaryButton(
            text = stringResource(R.string.login),
            onClick = {
                keyboardController?.hide()
                loginViewModel.onEvent(LoginEvent.LoginButtonClicked)
            }
        )
    }
}

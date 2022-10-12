package com.medium.client.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
@RootNavGraph
@Destination
@Composable
fun RegisterScreen(
    navigator: DestinationsNavigator,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val viewState = registerViewModel.uiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(registerViewModel.sideEffects) {
        registerViewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is RegisterSideEffect.NavigateToHomeScreen ->
                    navigator.navigate(HomeScreenDestination)
                is RegisterSideEffect.NavigateBack -> navigator.navigateUp()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.size_16))
    ) {
        IconButton(
            onClick = { registerViewModel.onEvent(RegisterEvent.BackButtonClicked) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_32)))
        Text(
            text = stringResource(R.string.register_your_account),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_16)))
        Text(
            text = stringResource(R.string.fill_details),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_64)))
        ChatAppInputField(
            value = viewState.username,
            onValueChange = { registerViewModel.onEvent(RegisterEvent.UsernameInputChanged(it)) },
            placeholderText = stringResource(R.string.username),
            trailingIcon = painterResource(R.drawable.ic_clear_button),
            onTrailingIconClick = { registerViewModel.onEvent(RegisterEvent.ClearUsernameInputButtonClicked) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_16)))
        ChatAppInputField(
            value = viewState.email,
            onValueChange = { registerViewModel.onEvent(RegisterEvent.EmailInputChanged(it)) },
            placeholderText = stringResource(R.string.email),
            trailingIcon = painterResource(R.drawable.ic_clear_button),
            onTrailingIconClick = { registerViewModel.onEvent(RegisterEvent.ClearEmailInputButtonClicked) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_16)))
        ChatAppInputField(
            value = viewState.password,
            onValueChange = { registerViewModel.onEvent(RegisterEvent.PasswordInputChanged(it)) },
            placeholderText = stringResource(R.string.password),
            trailingIcon = if (viewState.passwordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            onTrailingIconClick = { registerViewModel.onEvent(RegisterEvent.TogglePasswordHidden) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
            visualTransformation = if (viewState.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_16)))
        ChatAppInputField(
            value = viewState.confirmPassword,
            onValueChange = { registerViewModel.onEvent(RegisterEvent.ConfirmPasswordInputChanged(it)) },
            placeholderText = stringResource(R.string.confirm_password),
            trailingIcon = if (viewState.confirmPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            onTrailingIconClick = { registerViewModel.onEvent(RegisterEvent.ToggleConfirmPasswordHidden) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    registerViewModel.onEvent(RegisterEvent.RegisterButtonClicked)
                }
            ),
            visualTransformation = if (viewState.confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        )
        if (viewState.registerResult is Result.Error) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_32)))
            Text(
                text = viewState.registerResult.message ?: "",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.error
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_64)))
        ChatAppPrimaryButton(
            text = stringResource(R.string.register),
            onClick = {
                keyboardController?.hide()
                registerViewModel.onEvent(RegisterEvent.RegisterButtonClicked)
            }
        )
    }
}

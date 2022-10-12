package com.medium.client.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.medium.client.R
import com.medium.client.common.core.Result
import com.medium.client.common.wrappers.connectivity.NetworkStatus
import com.medium.client.common.wrappers.session_manager.SessionStatus
import com.medium.client.presentation.destinations.HomeScreenDestination
import com.medium.client.presentation.destinations.LoginScreenDestination
import com.medium.client.presentation.ui.theme.ChatAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun ChatApp(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val viewState = mainViewModel.uiState.collectAsState().value

    if (viewState.sessionStatus is Result.Loading) {

    } else if (viewState.sessionStatus.data == SessionStatus.LoggedIn) {
        navController.navigate(HomeScreenDestination)
    } else if (viewState.sessionStatus.data == SessionStatus.LoggedOut) {
        navController.navigate(LoginScreenDestination)
    }

    ChatAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            when (viewState.networkStatus) {
                NetworkStatus.LOSING -> {
                    systemUiController.setStatusBarColor(MaterialTheme.colors.onError)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.onError)
                            .padding(dimensionResource(R.dimen.size_16))
                    ) {
                        Text(
                            text = stringResource(R.string.connecting),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
                NetworkStatus.NOT_CONNECTED -> {
                    systemUiController.setStatusBarColor(MaterialTheme.colors.error)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.error)
                            .padding(dimensionResource(R.dimen.size_16)),
                    ) {
                        Text(
                            text = stringResource(R.string.connection_not_available),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
                else -> {
                    systemUiController.setStatusBarColor(MaterialTheme.colors.primary)
                }
            }
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController
            )
        }
    }
}

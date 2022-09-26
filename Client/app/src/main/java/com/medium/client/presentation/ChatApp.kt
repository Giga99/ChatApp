package com.medium.client.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.medium.client.common.core.Result
import com.medium.client.common.wrappers.session_manager.ChatAppSessionManager.SessionStatus
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
    val viewState = mainViewModel.viewState.collectAsState().value

    if (viewState.sessionStatus is Result.Loading) {

    } else if (viewState.sessionStatus.data == SessionStatus.LoggedIn) {
        navController.navigate(HomeScreenDestination)
    } else if (viewState.sessionStatus.data == SessionStatus.LoggedOut) {
        navController.navigate(LoginScreenDestination)
    }

    ChatAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController
            )
        }
    }
}

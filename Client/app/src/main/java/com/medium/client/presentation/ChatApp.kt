package com.medium.client.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.medium.client.presentation.login.NavGraphs
import com.medium.client.presentation.ui.theme.ChatAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun ChatApp() {
    ChatAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}

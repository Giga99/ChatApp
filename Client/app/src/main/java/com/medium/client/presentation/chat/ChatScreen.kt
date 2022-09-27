package com.medium.client.presentation.chat

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination
@Composable
fun ChatScreen(
    chatId: String,
    participant: String,
    navigator: DestinationsNavigator,
) {
    val chatViewModel: ChatViewModel = hiltViewModel()

}

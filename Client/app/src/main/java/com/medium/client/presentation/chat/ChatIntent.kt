package com.medium.client.presentation.chat

import com.medium.client.common.core.Result
import com.medium.client.domain.models.ui.MessageModel

sealed class ChatEvent {
    object BackButtonClicked : ChatEvent()
    data class OnMessageInputChange(val message: String) : ChatEvent()
    object SendMessage : ChatEvent()
}

sealed class ChatSideEffect {
    object NavigateBack : ChatSideEffect()
}

data class ChatViewState(
    val currentUser: String = "",
    val participant: String = "",
    val nextMessage: String = "",
    val messages: Result<List<MessageModel>> = Result.Loading()
)

data class ChatScreenNavArgs(
    val chatId: String,
    val participant: String
)

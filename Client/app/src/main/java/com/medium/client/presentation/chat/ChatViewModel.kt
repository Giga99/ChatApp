package com.medium.client.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.medium.client.common.ui.BaseViewModel
import com.medium.client.domain.models.requests.GetAllMessagesRequest
import com.medium.client.domain.repositories.ChatsRepository
import com.medium.client.domain.repositories.DataStoreRepository
import com.medium.client.presentation.destinations.ChatScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val chatsRepository: ChatsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ChatViewState, ChatEvent, ChatSideEffect>(ChatViewState()) {

    init {
        val navArgs = ChatScreenDestination.argsFrom(savedStateHandle)
        setState { copy(participant = navArgs.participant) }

        viewModelScope.launch {
            dataStoreRepository.observeUsername().collect {
                it?.let { currentUser ->
                    setState { copy(currentUser = currentUser) }
                }
            }
        }

        viewModelScope.launch {
            val messages = chatsRepository.getAllMessages(GetAllMessagesRequest(navArgs.chatId))
            setState { copy(messages = messages) }
        }
    }

    override fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.BackButtonClicked -> _sideEffects.trySend(ChatSideEffect.NavigateBack)
            is ChatEvent.OnMessageInputChange -> setState { copy(nextMessage = event.message) }
            is ChatEvent.SendMessage -> {}
        }
    }
}

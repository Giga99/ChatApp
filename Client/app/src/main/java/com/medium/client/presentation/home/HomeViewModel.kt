package com.medium.client.presentation.home

import androidx.lifecycle.viewModelScope
import com.medium.client.common.ui.BaseViewModel
import com.medium.client.domain.repositories.ChatsRepository
import com.medium.client.domain.repositories.UsersRepository
import com.medium.client.presentation.home.HomeViewState.HomeScreenTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val chatsRepository: ChatsRepository
) : BaseViewModel<HomeViewState, HomeEvent, HomeSideEffect>(HomeViewState()) {

    init {
        viewModelScope.launch {
            val chats = chatsRepository.getAllChats()
            println(chats)
            setState {
                copy(chats = enumValues<HomeScreenTab>().associateWith { chats })
            }
        }
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchButtonClicked -> {}
            is HomeEvent.OnTabClicked -> {
                setState { copy(selectedTab = event.tab) }
            }
            is HomeEvent.PageChanged -> {
                setState { copy(selectedTab = event.tab) }
            }
            is HomeEvent.OnChatRowClicked ->
                _sideEffects.trySend(HomeSideEffect.NavigateToChat(event.chat.id))
        }
    }
}

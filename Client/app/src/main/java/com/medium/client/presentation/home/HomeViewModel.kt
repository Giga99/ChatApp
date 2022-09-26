package com.medium.client.presentation.home

import com.medium.client.common.ui.BaseViewModel
import com.medium.client.domain.repositories.ChatsRepository
import com.medium.client.domain.repositories.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val chatsRepository: ChatsRepository
) : BaseViewModel<HomeViewState, HomeEvent, HomeSideEffect>(HomeViewState()) {

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchButtonClicked -> {}
        }
    }
}

package com.medium.client.presentation.home

import com.medium.client.common.ui.BaseViewModel
import com.medium.client.domain.repositories.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel<HomeViewState, HomeEvent, HomeSideEffect>(HomeViewState()) {

    override fun onEvent(event: HomeEvent) {
        TODO("Not yet implemented")
    }
}

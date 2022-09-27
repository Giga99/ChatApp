package com.medium.client.presentation.home

import com.medium.client.common.core.Result
import com.medium.client.domain.models.ui.ChatModel
import com.medium.client.presentation.home.HomeViewState.HomeScreenTab

sealed class HomeEvent {
    object SearchButtonClicked : HomeEvent()
    data class OnTabClicked(val tab: HomeScreenTab) : HomeEvent()
    data class PageChanged(val tab: HomeScreenTab) : HomeEvent()
    data class OnChatRowClicked(val chat: ChatModel) : HomeEvent()
}

sealed class HomeSideEffect {
    data class NavigateToChat(val chatId: String, val participant: String) : HomeSideEffect()
}

data class HomeViewState(
    val currentUser: String = "",
    val selectedTab: HomeScreenTab = HomeScreenTab.All,
    val chats: Map<HomeScreenTab, Result<List<ChatModel>>> = enumValues<HomeScreenTab>().associateWith { Result.Loading() }
) {

    enum class HomeScreenTab {
        All, Unread
    }
}

package com.medium.client.presentation.home

sealed class HomeEvent {

}

sealed class HomeSideEffect {

}

data class HomeViewState(
    val selectedTab: HomeScreenTab = HomeScreenTab.All
) {

    enum class HomeScreenTab {
        All
    }
}

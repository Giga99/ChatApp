package com.medium.client.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.medium.client.R
import com.medium.client.common.core.Result
import com.medium.client.common.ui.toPrettierDateFormat
import com.medium.client.domain.models.ui.ChatModel
import com.medium.client.presentation.destinations.ChatScreenDestination
import com.medium.client.presentation.home.HomeViewState.HomeScreenTab
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@RootNavGraph
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val viewState = homeViewModel.uiState.collectAsState().value
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val tabs = viewState.chats.keys.toList()
    val pages = viewState.chats.values.toList()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                homeViewModel.onEvent(HomeEvent.PageChanged(tabs[page]))
            }
    }

    LaunchedEffect(homeViewModel.sideEffects) {
        homeViewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is HomeSideEffect.NavigateToChat ->
                    navigator.navigate(
                        ChatScreenDestination(
                            chatId = sideEffect.chatId,
                            participant = sideEffect.participant
                        )
                    )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeScreenTopBar(
                tabs = tabs,
                pagerState = pagerState,
                onTabClick = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page = index)
                    }
                    homeViewModel.onEvent(HomeEvent.OnTabClicked(tabs[index]))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                count = tabs.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimensionResource(R.dimen.size_16))
            ) { currentPageIndex ->
                when (val currentPage = pages[currentPageIndex]) {
                    is Result.Success -> {
                        ChatsSuccess(
                            currentUser = viewState.currentUser,
                            chats = currentPage.data ?: emptyList(),
                            onRowClick = { homeViewModel.onEvent(HomeEvent.OnChatRowClicked(it)) }
                        )
                    }
                    is Result.Error -> {
                        ChatsError(errorMessage = currentPage.message ?: "")
                    }
                    is Result.Loading -> {
                        ChatsLoading()
                    }
                }
            }
        }
    }
}

@Composable
fun ChatsSuccess(
    currentUser: String,
    chats: List<ChatModel>,
    onRowClick: (ChatModel) -> Unit
) {
    if (chats.isEmpty()) {
        Text(
            text = stringResource(R.string.no_chats),
            style = MaterialTheme.typography.h6
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = chats,
                key = { it.id }
            ) {
                ChatRow(
                    currentUser = currentUser,
                    chatModel = it,
                    onRowClick = onRowClick
                )
            }
        }
    }
}

@Composable
fun ChatRow(
    currentUser: String,
    chatModel: ChatModel,
    onRowClick: (ChatModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRowClick(chatModel) }
            .padding(dimensionResource(R.dimen.size_16))
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (currentUser == chatModel.user1) chatModel.user2 else chatModel.user1,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = chatModel.lastMessage.timestamp.toPrettierDateFormat(),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_8)))
            Text(
                text = chatModel.lastMessage.text,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@Composable
fun ChatsError(
    errorMessage: String
) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.error
    )
}

@Composable
fun ColumnScope.ChatsLoading() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.size_16))
            .align(Alignment.CenterHorizontally)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenTopBar(
    tabs: List<HomeScreenTab>,
    pagerState: PagerState,
    onTabClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colors.primary)
            .padding(dimensionResource(R.dimen.size_16)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.chats),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_16)))
            ChatTabs(
                tabs = tabs,
                pagerState = pagerState,
                onTabClick = onTabClick
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ChatTabs(
    tabs: List<HomeScreenTab>,
    pagerState: PagerState,
    onTabClick: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .wrapContentWidth()
                    .padding(horizontal = dimensionResource(R.dimen.size_64))
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, item ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = { onTabClick(index) },
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                text = {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            )
        }
    }
}

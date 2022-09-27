package com.medium.client.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.medium.client.R
import com.medium.client.common.core.Result
import com.medium.client.common.ui.toTime
import com.medium.client.domain.models.ui.MessageModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@RootNavGraph
@Destination(navArgsDelegate = ChatScreenNavArgs::class)
@Composable
fun ChatScreen(
    navigator: DestinationsNavigator
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val viewState = chatViewModel.viewState.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(chatViewModel.sideEffects) {
        chatViewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is ChatSideEffect.NavigateBack -> navigator.navigateUp()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChatScreenTopBar(
                participant = viewState.participant,
                onBackButtonClick = { chatViewModel.onEvent(ChatEvent.BackButtonClicked) }
            )
        },
        bottomBar = {
            ChatScreenBottomBar(
                message = viewState.nextMessage,
                keyboardController = keyboardController,
                onMessageInputChange = { chatViewModel.onEvent(ChatEvent.OnMessageInputChange(it)) },
                onSendMessage = { chatViewModel.onEvent(ChatEvent.SendMessage) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewState.messages) {
                is Result.Success -> {
                    MessagesSuccess(
                        messages = viewState.messages.data ?: emptyList(),
                        currentUser = viewState.currentUser
                    )
                }
                is Result.Error -> {
                    MessagesError(
                        errorMessage = viewState.messages.message ?: ""
                    )
                }
                is Result.Loading -> {
                    MessagesLoading()
                }
            }
        }
    }
}

@Composable
fun MessagesSuccess(
    messages: List<MessageModel>,
    currentUser: String
) {
    if (messages.isEmpty()) {
        Text(
            text = stringResource(R.string.no_messages),
            style = MaterialTheme.typography.h6
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = messages,
                key = { it.id }
            ) {
                MessageRow(
                    message = it,
                    currentUser = currentUser
                )
            }
        }
    }
}

@Composable
fun MessageRow(
    message: MessageModel,
    currentUser: String
) {
    val senderIsCurrentUser = message.sender == currentUser
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.size_16)),
        horizontalArrangement = if (senderIsCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.body1,
                color = if (senderIsCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(if (senderIsCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.secondaryVariant)
                    .padding(dimensionResource(R.dimen.size_8))
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_4)))
            Text(
                text = message.timestamp.toTime(),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@Composable
fun MessagesError(
    errorMessage: String
) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.error
    )
}

@Composable
fun ColumnScope.MessagesLoading() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.size_16))
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
fun ChatScreenTopBar(
    participant: String,
    onBackButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(dimensionResource(R.dimen.size_16)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackButtonClick) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "",
                tint = MaterialTheme.colors.onPrimary
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_8)))
        Text(
            text = participant,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreenBottomBar(
    message: String,
    keyboardController: SoftwareKeyboardController?,
    onMessageInputChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = dimensionResource(R.dimen.size_16),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = onMessageInputChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.size_16)),
                placeholder = {
                    Text(
                        text = stringResource(R.string.type_message),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                },
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.secondaryVariant,
                    backgroundColor = MaterialTheme.colors.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onSendMessage()
                    }
                ),
                trailingIcon = {
                    IconButton(
                        onClick = onSendMessage
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = "",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            )
        }
    }
}

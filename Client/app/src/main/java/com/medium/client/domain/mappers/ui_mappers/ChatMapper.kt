package com.medium.client.domain.mappers.ui_mappers

import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.domain.models.ui.ChatModel

fun ChatResponse.toModel(): ChatModel =
    ChatModel(
        id = id,
        user1 = user1,
        user2 = user2,
        lastMessage = lastMessage.toModel()
    )

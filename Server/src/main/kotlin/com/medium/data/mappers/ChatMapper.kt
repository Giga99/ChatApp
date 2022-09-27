package com.medium.data.mappers

import com.medium.data.chat.Chat
import com.medium.data.responses.ChatDto
import com.medium.data.responses.MessageDto

fun Chat.toDto(lastMessage: MessageDto): ChatDto =
    ChatDto(
        id = id.toString(),
        user1 = user1,
        user2 = user2,
        lastMessage = lastMessage
    )

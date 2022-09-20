package com.medium.data.mappers

import com.medium.chat.ChatSocket
import com.medium.data.responses.ChatDto
import com.medium.data.responses.MessageDto

fun ChatSocket.toDto(lastMessage: MessageDto): ChatDto =
    ChatDto(
        id = id,
        user1 = user1,
        user2 = user2,
        lastMessage = lastMessage
    )

package com.medium.data.mappers

import com.medium.data.message.Message
import com.medium.data.responses.MessageDto

fun Message.toDto(): MessageDto =
    MessageDto(
        id = id.toString(),
        text = text,
        sender = sender,
        receiver = receiver,
        timestamp = timestamp
    )

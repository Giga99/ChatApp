package com.medium.data.mappers

import com.medium.data.chat.Message
import com.medium.data.responses.MessageDto

fun Message.toDto(): MessageDto =
    MessageDto(
        id = id.toString(),
        text = text,
        from = from,
        to = to,
        timestamp = timestamp
    )

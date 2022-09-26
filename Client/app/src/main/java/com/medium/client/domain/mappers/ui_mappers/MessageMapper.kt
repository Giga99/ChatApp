package com.medium.client.domain.mappers.ui_mappers

import com.medium.client.data.remote.responses.MessageResponse
import com.medium.client.domain.models.ui.MessageModel

fun MessageResponse.toModel(): MessageModel =
    MessageModel(
        id = id,
        text = text,
        sender = sender,
        receiver = receiver,
        timestamp = timestamp
    )

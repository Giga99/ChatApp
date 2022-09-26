package com.medium.client.domain.mappers.requests_mappers

import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.domain.models.requests.GetAllMessagesRequest

fun GetAllMessagesRequest.toBody(): GetAllMessagesBody =
    GetAllMessagesBody(participant = participant)

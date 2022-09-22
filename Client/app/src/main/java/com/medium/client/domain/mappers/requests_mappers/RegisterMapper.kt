package com.medium.client.domain.mappers.requests_mappers

import com.medium.client.data.remote.requests.RegisterBody
import com.medium.client.domain.models.requests.RegisterRequest

fun RegisterRequest.toBody(): RegisterBody =
    RegisterBody(
        username = username,
        email = email,
        password = password
    )

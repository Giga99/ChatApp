package com.medium.client.domain.mappers.requests_mappers

import com.medium.client.data.remote.requests.LoginBody
import com.medium.client.domain.models.requests.LoginRequest

fun LoginRequest.toBody(): LoginBody =
    LoginBody(
        username = username,
        password = password
    )

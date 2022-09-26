package com.medium.client.domain.mappers.ui_mappers

import com.medium.client.data.remote.responses.UserResponse
import com.medium.client.domain.models.ui.UserModel

fun UserResponse.toModel(): UserModel =
    UserModel(
        id = id,
        username = username,
        email = email
    )

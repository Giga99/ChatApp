package com.medium.data.mappers

import com.medium.data.responses.UserResponse
import com.medium.data.user.User

fun User.toResponse(): UserResponse =
    UserResponse(
        id = id.toString(),
        username = username
    )

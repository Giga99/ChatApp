package com.medium.data.mappers

import com.medium.data.responses.UserDto
import com.medium.data.user.User

fun User.toDto(): UserDto =
    UserDto(
        id = id.toString(),
        username = username
    )

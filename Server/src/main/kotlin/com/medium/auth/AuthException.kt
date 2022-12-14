package com.medium.auth

import io.ktor.http.*

sealed class AuthException(override val message: String, val status: HttpStatusCode) : Exception(message) {
    object UsernamePasswordBlankException : AuthException("Username and password can't be blank", HttpStatusCode.BadRequest)
    object PasswordTooShortException : AuthException("Password is too short", HttpStatusCode.BadRequest)
    object DatabaseInsertException : AuthException("Database insert failed", HttpStatusCode.ExpectationFailed)

    object IncorrectUsernamePasswordException : AuthException("Incorrect username or password", HttpStatusCode.BadRequest)

    object ExpiredRefreshTokenException : AuthException("Expired refresh token", HttpStatusCode.Unauthorized)
    object UserNotFoundException : AuthException("User with given username not found", HttpStatusCode.NotFound)
}

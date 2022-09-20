package com.medium.auth

import com.medium.data.requests.LoginRequest
import com.medium.data.requests.RefreshTokenRequest
import com.medium.data.requests.RegisterRequest
import com.medium.data.responses.AuthDto
import com.medium.data.user.User
import com.medium.data.user.UserDataSource
import com.medium.security.hashing.HashingService
import com.medium.security.hashing.SaltedHash
import com.medium.security.manager.TokensManager

class AuthController(
    private val userDataSource: UserDataSource,
    private val tokensManager: TokensManager,
    private val hashingService: HashingService
) {

    suspend fun registerUser(request: RegisterRequest): AuthDto {
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        if (areFieldsBlank) {
            throw AuthException.UsernamePasswordBlankException
        }

        val isPasswordTooShort = request.password.length < 8
        if (isPasswordTooShort) {
            throw AuthException.PasswordTooShortException
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            throw AuthException.DatabaseInsertException
        }

        val accessToken = tokensManager.generateAccessToken(user = user)
        val refreshToken = tokensManager.generateRefreshToken(user = user)
        return AuthDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    suspend fun loginUser(request: LoginRequest): AuthDto {
        val user =
            userDataSource.getUserByUsername(request.username) ?: throw AuthException.IncorrectUsernamePasswordException

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            throw AuthException.IncorrectUsernamePasswordException
        }

        val accessToken = tokensManager.generateAccessToken(user = user)
        val refreshToken = tokensManager.generateRefreshToken(user = user)
        return AuthDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    suspend fun refreshToken(request: RefreshTokenRequest): AuthDto {
        val isExpired = tokensManager.isTokenExpired(token = request.refreshToken)
        if (isExpired) {
            throw AuthException.ExpiredRefreshTokenException
        }

        val username = tokensManager.getUsernameFromToken(token = request.refreshToken)
        val user = userDataSource.getUserByUsername(username) ?: throw AuthException.UserNotFoundException

        val accessToken = tokensManager.generateAccessToken(user = user)
        val refreshToken = tokensManager.generateRefreshToken(user = user)
        return AuthDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

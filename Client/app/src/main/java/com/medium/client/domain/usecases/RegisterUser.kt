package com.medium.client.domain.usecases

import androidx.core.util.PatternsCompat
import com.medium.client.common.core.Constants.PASSWORD_REGEX
import com.medium.client.common.core.Result
import com.medium.client.domain.models.requests.RegisterRequest
import com.medium.client.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(registerRequest: RegisterRequest): Result<Unit> =
        withContext(Dispatchers.IO) {
            with(registerRequest) {
                if (username.isBlank())
                    return@withContext Result.Error("Username is empty")

                if (email.isBlank())
                    return@withContext Result.Error("Email is empty")

                if (password.isBlank())
                    return@withContext Result.Error("Password is empty")

                if (confirmPassword.isBlank())
                    return@withContext Result.Error("Confirm password is empty")

                if (!PatternsCompat.EMAIL_ADDRESS.matcher(registerRequest.email).matches())
                    return@withContext Result.Error("Email is not in right format")

                if (!PASSWORD_REGEX.matches(registerRequest.password))
                    return@withContext Result.Error("Password must include minimum of eight characters, at least one uppercase letter, one lowercase letter, one number, and one special character")

                if (password != confirmPassword)
                    return@withContext Result.Error("Password and confirm password are not the same")
            }

            authRepository.register(registerRequest)
        }
}

package com.medium.client.data.remote.interceptors

import com.medium.client.common.wrappers.session_manager.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptorImpl @Inject constructor(
    private val sessionManager: Provider<SessionManager>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var accessToken = sessionManager.get().getAccessToken()

        val response = chain.proceed(newRequestWithAccessToken(accessToken, request))

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            synchronized(this) {
                val newAccessToken = sessionManager.get().getAccessToken()
                if (newAccessToken != accessToken) {
                    return chain.proceed(newRequestWithAccessToken(newAccessToken, request))
                } else {
                    accessToken = refreshToken()
                    if (accessToken.isNullOrBlank()) {
                        sessionManager.get().logout()
                        return response
                    }
                    return chain.proceed(newRequestWithAccessToken(accessToken, request))
                }
            }
        }

        return response
    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .apply { if (accessToken != null) header("Authorization", "Bearer $accessToken") }
            .build()

    private fun refreshToken(): String? {
        val refreshToken: String? = sessionManager.get().getRefreshToken()
        refreshToken?.let {
            return sessionManager.get().refreshToken(it)
        } ?: return null
    }
}

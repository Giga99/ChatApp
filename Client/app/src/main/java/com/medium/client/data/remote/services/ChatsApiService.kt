package com.medium.client.data.remote.services

import com.medium.client.common.core.ApiResponse
import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.data.remote.responses.MessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatsApiService {

    @GET("messaging/chats")
    suspend fun getUserChats(): ApiResponse<List<ChatResponse>>

    @POST("messaging/messages")
    suspend fun getAllMessages(
        @Body body: GetAllMessagesBody
    ): ApiResponse<List<MessageResponse>>
}

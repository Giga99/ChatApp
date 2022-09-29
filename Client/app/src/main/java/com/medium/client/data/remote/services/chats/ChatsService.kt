package com.medium.client.data.remote.services.chats

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.data.remote.responses.MessageResponse

interface ChatsService {

    suspend fun getUserChats(): BasicApiResponse<List<ChatResponse>>

    suspend fun getAllMessages(body: GetAllMessagesBody): BasicApiResponse<List<MessageResponse>>

    sealed class Endpoints(val url: String) {
        object UserChats : Endpoints("messaging/chats")
        object AllMessages : Endpoints("messaging/messages")
    }
}

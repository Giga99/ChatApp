package com.medium.client.data.remote.services.chats

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.data.remote.responses.MessageResponse
import kotlinx.coroutines.flow.Flow

interface ChatsService {

    suspend fun getUserChats(): BasicApiResponse<List<ChatResponse>>

    suspend fun getAllMessages(body: GetAllMessagesBody): BasicApiResponse<List<MessageResponse>>

    suspend fun initSocket(participant: String)

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<MessageResponse>

    suspend fun closeSession()

    sealed class Endpoints(val path: String) {
        object UserChats : Endpoints("messaging/chats")
        object AllMessages : Endpoints("messaging/messages")
        object ChatSocket : Endpoints("ws/chat-socket")
    }
}

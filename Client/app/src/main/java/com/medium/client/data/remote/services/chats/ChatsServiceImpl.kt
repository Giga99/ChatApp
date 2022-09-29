package com.medium.client.data.remote.services.chats

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.data.remote.responses.MessageResponse
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ChatsServiceImpl @Inject constructor(
    private val client: HttpClient
) : ChatsService {

    override suspend fun getUserChats(): BasicApiResponse<List<ChatResponse>> =
        client.get(path = ChatsService.Endpoints.UserChats.url)

    override suspend fun getAllMessages(body: GetAllMessagesBody): BasicApiResponse<List<MessageResponse>> =
        client.post(path = ChatsService.Endpoints.AllMessages.url) {
            this.body = body
        }
}

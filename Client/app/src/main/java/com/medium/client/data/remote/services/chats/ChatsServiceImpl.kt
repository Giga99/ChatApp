package com.medium.client.data.remote.services.chats

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.data.remote.responses.MessageResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatsServiceImpl @Inject constructor(
    private val client: HttpClient
) : ChatsService {

    private var socket: WebSocketSession? = null

    override suspend fun getUserChats(): BasicApiResponse<List<ChatResponse>> =
        client.get(path = ChatsService.Endpoints.UserChats.url)

    override suspend fun getAllMessages(body: GetAllMessagesBody): BasicApiResponse<List<MessageResponse>> =
        client.post(path = ChatsService.Endpoints.AllMessages.url) {
            this.body = body
        }

    override suspend fun initSocket(participant: String) {
        socket = client.webSocketSession {
            url(path = ChatsService.Endpoints.ChatSocket.url) {
                parameters.append("participant", participant)
            }
        }
        println(socket)
    }

    override suspend fun sendMessage(message: String) {
        socket?.send(Frame.Text(message))
    }

    override fun observeMessages(): Flow<MessageResponse> =
        socket?.incoming
            ?.receiveAsFlow()
            ?.filter { it is Frame.Text }
            ?.map {
                val json = (it as? Frame.Text)?.readText() ?: ""
                Json.decodeFromString(json)
            } ?: flow { }

    override suspend fun closeSession() {
        socket?.close()
    }
}

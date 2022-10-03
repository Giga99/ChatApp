package com.medium.client.data.remote.services.chats

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.GetAllMessagesBody
import com.medium.client.data.remote.responses.ChatResponse
import com.medium.client.data.remote.responses.MessageResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatsServiceImpl @Inject constructor(
    private val client: HttpClient
) : ChatsService {

    private var socket: WebSocketSession? = null

    override suspend fun getUserChats(): BasicApiResponse<List<ChatResponse>> =
        client.get(ChatsService.Endpoints.UserChats.path).body()

    override suspend fun getAllMessages(body: GetAllMessagesBody): BasicApiResponse<List<MessageResponse>> =
        client.post(ChatsService.Endpoints.AllMessages.path) {
            setBody(body)
        }.body()

    override suspend fun initSocket(participant: String) {
        client.webSocket("${ChatsService.Endpoints.ChatSocket.path}?participant=$participant") {
            socket = this
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

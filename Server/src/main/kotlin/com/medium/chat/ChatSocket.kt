package com.medium.chat

import com.medium.data.responses.MessageDto
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class ChatSocket(
    val id: String,
    val user1: String,
    val user2: String,
    var user1Socket: WebSocketSession?,
    var user2Socket: WebSocketSession? = null,
    var user1Connected: Boolean,
    var user2Connected: Boolean = false
) {
    fun connectUser1(socket: WebSocketSession) {
        user1Socket = socket
        user1Connected = true
    }

    fun connectUser2(socket: WebSocketSession) {
        user2Socket = socket
        user2Connected = true
    }

    suspend fun disconnectUser1() {
        user1Socket?.close()
        user1Connected = false
    }

    suspend fun disconnectUser2() {
        user2Socket?.close()
        user2Connected = true
    }

    fun isChatForUser(user: String): Boolean = this.user1 == user || this.user2 == user

    fun isCorrectChat(user1: String, user2: String): Boolean =
        this.user1 == user1 && this.user2 == user2 || this.user1 == user2 && this.user2 == user1

    suspend fun emitMessage(message: MessageDto) {
        val parsedMessage = Json.encodeToString(message)
        user1Socket?.send(Frame.Text(parsedMessage))
        user2Socket?.send(Frame.Text(parsedMessage))
    }
}

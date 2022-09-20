package com.medium.routes

import com.medium.chat.ChatController
import com.medium.data.requests.ChatRequest
import com.medium.data.requests.GetAllMessagesRequest
import com.medium.utils.toBasicResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.ktor.ext.inject

fun Route.chatRoutes() {
    val chatController by inject<ChatController>()

    route("messaging/") {
        authenticate {
            getAllChats(chatController = chatController)
            getAllMessages(chatController = chatController)
            chatSocket(chatController = chatController)
        }
    }
}

fun Route.getAllChats(chatController: ChatController) {
    get("chats") {
        val username = call.principal<JWTPrincipal>()?.getClaim("username", String::class) ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@get
        }

        val response = chatController.getAllChatsForUser(username = username)
        val status = HttpStatusCode.OK
        call.respond(
            status = status,
            message = status.toBasicResponse(response = response)
        )
    }
}

fun Route.getAllMessages(chatController: ChatController) {
    post("messages") {
        val username = call.principal<JWTPrincipal>()?.getClaim("username", String::class) ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }
        val request = call.receiveNullable<GetAllMessagesRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        val response = chatController.getAllMessages(user1 = username, user2 = request.participant)
        val status = HttpStatusCode.OK
        call.respond(
            status = status,
            message = status.toBasicResponse(response = response)
        )
    }
}

fun Route.chatSocket(chatController: ChatController) {
    webSocket("chat-socket") {
        val username = call.principal<JWTPrincipal>()?.getClaim("username", String::class) ?: kotlin.run {
            println("NO TOKEN")
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Unauthorized"))
            return@webSocket
        }
        val participant = call.receiveNullable<ChatRequest>()?.participant ?: kotlin.run {
            println("NO REQUEST")
            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Bad request"))
            return@webSocket
        }

        try {
            chatController.onJoin(
                currentUser = username,
                chatParticipant = participant,
                socket = this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    chatController.sendMessage(
                        sender = username,
                        receiver = participant,
                        text = frame.readText()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            chatController.tryDisconnect(
                currentUser = username,
                chatParticipant = participant
            )
        }
    }
}

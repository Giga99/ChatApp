package com.medium.chat

import com.medium.data.chat.Chat
import com.medium.data.chat.ChatDataSource
import com.medium.data.mappers.toDto
import com.medium.data.message.Message
import com.medium.data.message.MessageDataSource
import com.medium.data.responses.ChatDto
import com.medium.data.responses.MessageDto
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val messageDataSource: MessageDataSource,
    private val chatDataSource: ChatDataSource
) {
    private val chats = ConcurrentHashMap<String, ChatSocket>()

    suspend fun onJoin(
        currentUser: String,
        chatParticipant: String,
        socket: WebSocketSession
    ) {
        var chat = chatDataSource.getChat(currentUser, chatParticipant)
        if (chat != null) {
            if (chats[chat.id.toString()] != null) {
                if (chats[chat.id.toString()]?.user1 == currentUser) {
                    chats[chat.id.toString()]?.connectUser1(socket)
                } else if (chats[chat.id.toString()]?.user2 == currentUser) {
                    chats[chat.id.toString()]?.connectUser2(socket)
                }
                return
            }
        } else {
            chatDataSource.insertChat(
                Chat(
                    user1 = currentUser,
                    user2 = chatParticipant
                )
            )
            chat = chatDataSource.getChat(currentUser, chatParticipant)!!
        }

        chats[chat.id.toString()] = ChatSocket(
            id = chat.id.toString(),
            user1 = currentUser,
            user2 = chatParticipant,
            user1Socket = socket,
            user1Connected = true
        )
    }

    suspend fun sendMessage(
        sender: String,
        receiver: String,
        text: String
    ) {
        val message = Message(
            text = text,
            sender = sender,
            receiver = receiver,
            timestamp = System.currentTimeMillis()
        )
        messageDataSource.insertMessage(message)
        chats.values.find { it.isCorrectChat(sender, receiver) }?.emitMessage(message.toDto())
    }

    suspend fun getAllChatsForUser(username: String): List<ChatDto> =
        chatDataSource.getAllChats(username).map { chat ->
            val lastMessage = messageDataSource.getLastMessage(chat.user1, chat.user2).toDto()
            chat.toDto(lastMessage = lastMessage)
        }

    suspend fun getAllMessages(
        chatId: String
    ): List<MessageDto> =
        chats[chatId]?.let { messageDataSource.getAllMessages(it.user1, it.user2).map { it.toDto() } } ?: emptyList()

    suspend fun tryDisconnect(
        currentUser: String,
        chatParticipant: String
    ) {
        chatDataSource.getChat(currentUser, chatParticipant)?.let {
            chats[it.id.toString()]?.let { chat ->
                if (chat.user1 == currentUser) {
                    chat.disconnectUser1()
                } else {
                    chat.disconnectUser2()
                }

                if (!chat.user1Connected && !chat.user2Connected) {
                    chats.remove(chat.id)
                }
            }
        }
    }
}

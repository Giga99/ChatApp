package com.medium.client.domain.repositories

import com.medium.client.common.core.Result
import com.medium.client.domain.models.requests.GetAllMessagesRequest
import com.medium.client.domain.models.requests.MessageRequest
import com.medium.client.domain.models.ui.ChatModel
import com.medium.client.domain.models.ui.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {

    suspend fun getAllChats(): Result<List<ChatModel>>

    suspend fun getAllMessages(getAllMessagesRequest: GetAllMessagesRequest): Result<List<MessageModel>>

    suspend fun initSocket(participant: String): Result<Unit>

    suspend fun sendMessage(request: MessageRequest)

    fun observeMessages(): Flow<MessageModel>

    suspend fun closeSession(): Result<Unit>
}

package com.medium.client.domain.repositories

import com.medium.client.common.core.Result
import com.medium.client.domain.models.requests.GetAllMessagesRequest
import com.medium.client.domain.models.ui.ChatModel
import com.medium.client.domain.models.ui.MessageModel

interface ChatsRepository {

    suspend fun getAllChats(): Result<List<ChatModel>>

    suspend fun getAllMessages(getAllMessagesRequest: GetAllMessagesRequest): Result<List<MessageModel>>
}

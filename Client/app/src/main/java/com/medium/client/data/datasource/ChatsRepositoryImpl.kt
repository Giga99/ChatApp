package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.remote.api_handler.ApiHandler
import com.medium.client.data.remote.services.chats.ChatsService
import com.medium.client.domain.mappers.requests_mappers.toBody
import com.medium.client.domain.mappers.ui_mappers.toModel
import com.medium.client.domain.models.requests.GetAllMessagesRequest
import com.medium.client.domain.models.requests.MessageRequest
import com.medium.client.domain.models.ui.ChatModel
import com.medium.client.domain.models.ui.MessageModel
import com.medium.client.domain.repositories.ChatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val chatsService: ChatsService,
    private val apiHandler: ApiHandler
) : ChatsRepository {

    override suspend fun getAllChats(): Result<List<ChatModel>> {
        val response = apiHandler.handleCall { chatsService.getUserChats() }

        return response.data?.let { chatsResponse ->
            Result.Success(chatsResponse.map { it.toModel() })
        } ?: Result.Error(response.message ?: "")
    }

    override suspend fun getAllMessages(getAllMessagesRequest: GetAllMessagesRequest): Result<List<MessageModel>> {
        val response =
            apiHandler.handleCall {
                chatsService.getAllMessages(
                    getAllMessagesRequest.toBody()
                )
            }

        return response.data?.let { messagesResponse ->
            Result.Success(messagesResponse.map { it.toModel() })
        } ?: Result.Error(response.message ?: "")
    }

    override suspend fun initSocket(participant: String): Result<Unit> =
        apiHandler.handleSocket { chatsService.initSocket(participant) }

    override suspend fun sendMessage(request: MessageRequest) {
        apiHandler.handleSocket { chatsService.sendMessage(request.message) }
    }

    override fun observeMessages(): Flow<MessageModel> =
        chatsService.observeMessages().map { it.toModel() }

    override suspend fun closeSession(): Result<Unit> =
        apiHandler.handleSocket { chatsService.closeSession() }
}

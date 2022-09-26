package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.remote.api_handler.ApiHandler
import com.medium.client.data.remote.services.ChatsApiService
import com.medium.client.domain.mappers.requests_mappers.toBody
import com.medium.client.domain.mappers.ui_mappers.toModel
import com.medium.client.domain.models.requests.GetAllMessagesRequest
import com.medium.client.domain.models.ui.ChatModel
import com.medium.client.domain.models.ui.MessageModel
import com.medium.client.domain.repositories.ChatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val chatsApiService: ChatsApiService,
    private val apiHandler: ApiHandler
) : ChatsRepository {

    override suspend fun getAllChats(): Result<List<ChatModel>> =
        withContext(Dispatchers.IO) {
            val response = apiHandler.handleCall { chatsApiService.getUserChats() }

            response.data?.let { chatsResponse ->
                Result.Success(chatsResponse.map { it.toModel() })
            } ?: Result.Error(response.message ?: "")
        }

    override suspend fun getAllMessages(getAllMessagesRequest: GetAllMessagesRequest): Result<List<MessageModel>> =
        withContext(Dispatchers.IO) {
            val response =
                apiHandler.handleCall { chatsApiService.getAllMessages(getAllMessagesRequest.toBody()) }

            response.data?.let { messagesResponse ->
                Result.Success(messagesResponse.map { it.toModel() })
            } ?: Result.Error(response.message ?: "")
        }
}

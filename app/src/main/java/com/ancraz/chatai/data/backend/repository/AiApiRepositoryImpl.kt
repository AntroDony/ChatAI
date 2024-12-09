package com.ancraz.chatai.data.backend.repository

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.api.ApiClient
import com.ancraz.chatai.data.backend.api.models.ApiResponseModel
import com.ancraz.chatai.domain.repository.AiApiRepository
import kotlinx.serialization.json.Json

class AiApiRepositoryImpl: AiApiRepository {

    private val apiClient: ApiClient by lazy {
        ApiClient()
    }

    override suspend fun sendMessageToAi(message: String): String?{
        try {
            val responseBody = apiClient.sendMessageToAi(message)

            val responseMessage = Json{
                ignoreUnknownKeys = true
            }.decodeFromString<ApiResponseModel>(responseBody)

            return responseMessage.messages[0].content?.substring(0, 20)
        }
        catch (e: Exception){
            debugLog("getAnswerFromAiException: ${e.message}")
            return null
        }

    }
}
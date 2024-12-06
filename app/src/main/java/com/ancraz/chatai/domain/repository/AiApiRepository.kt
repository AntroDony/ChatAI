package com.ancraz.chatai.domain.repository

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.api.ApiClient
import com.ancraz.chatai.data.backend.api.model.ApiResponseModel
import kotlinx.serialization.json.Json

class AiApiRepository {

    private val apiClient: ApiClient by lazy {
        ApiClient()
    }

    suspend fun getAnswerFromAi(message: String): String?{
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
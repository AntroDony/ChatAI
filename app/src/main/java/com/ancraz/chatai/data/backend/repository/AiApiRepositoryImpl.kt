package com.ancraz.chatai.data.backend.repository

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.api.ApiClient
import com.ancraz.chatai.data.backend.api.models.ChatMessageResponseModel
import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import com.ancraz.chatai.domain.repository.AiApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class AiApiRepositoryImpl: AiApiRepository {

    private val apiClient: ApiClient by lazy {
        ApiClient()
    }

    override suspend fun sendMessageToAi(message: String): String?{
        try {
            val botMessage = apiClient.sendMessageToAi(message)

            botMessage?.let { response ->
                val responseMessage = Json.decodeFromString<ChatMessageResponseModel>(response)

                return responseMessage.messages[0].content?.substring(0, 20)
            } ?: run {
                debugLog("AiResponse is null")
                throw NullPointerException()
            }
        }
        catch (e: Exception){
            debugLog("getAnswerFromAiException: ${e.message}")
            return null
        }
    }


    override fun getActivitiesByUser(userId: String): Flow<List<ActivityDto>> {
        return flow {
            try {
                val habits = apiClient.getHabitsByUser(userId)

                habits?.let { response ->
                    val responseHabits = Json.decodeFromString<List<ActivityDto>>(response)

                    debugLog("Response Habits: $responseHabits")

                    emit(responseHabits)
                } ?: run {
                    debugLog("AI Response is null")
                    throw NullPointerException()
                }

            }
            catch (e: Exception){
                debugLog("getActivitiesByUser Exception: ${e.message}")
            }
        }
    }
}
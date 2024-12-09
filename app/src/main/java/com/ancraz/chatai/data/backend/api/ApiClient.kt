package com.ancraz.chatai.data.backend.api

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.api.models.ChatMessageRequestModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ApiClient {

    private val BASE_URL = "https://dm1tryg.com/"
    private val CHAT_ENDPOINT = "api/chat"
    private val HABITS_ENDPOINT = "api/habits"

    private val networkClient: HttpClient by lazy {
        HttpClient(CIO)
    }

    suspend fun sendMessageToAi(message: String): String?{
        try {
            val response = networkClient.post(BASE_URL + CHAT_ENDPOINT){
                contentType(ContentType.Application.Json)
                setBody(message.toJsonBody())
            }

            debugLog("AI Message response body: ${response.bodyAsText()}")

            return response.bodyAsText()
        }
        catch (e: Exception){
            debugLog("sendMessageToAi Exception: ${e.message}")
            return ""
        }
    }


    suspend fun getHabitsByUser(userID: String): String?{
        try {
            val response = networkClient.get(BASE_URL + HABITS_ENDPOINT){
                //todo add userId parameter
            }

            debugLog("Habits response body: ${response.bodyAsText()}")

            return response.bodyAsText()
        }
        catch (e: Exception){
            debugLog("getHabitsByUser Exception: ${e.message}")
            return null
        }
    }


    private fun String.toJsonBody(): String {
        val requestBody = Json.encodeToString(ChatMessageRequestModel(this))
        return requestBody
    }
}
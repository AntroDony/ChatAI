package com.ancraz.chatai.domain.repository

interface AiApiRepository {

    suspend fun sendMessageToAi(message: String): String?
}
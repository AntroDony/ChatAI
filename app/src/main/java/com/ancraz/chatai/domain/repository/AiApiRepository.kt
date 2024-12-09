package com.ancraz.chatai.domain.repository

import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import kotlinx.coroutines.flow.Flow

interface AiApiRepository {

    suspend fun sendMessageToAi(message: String): String?

    fun getActivitiesByUser(userId: String): Flow<List<ActivityDto>>
}
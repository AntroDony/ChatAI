package com.ancraz.chatai.domain.repository

import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import com.ancraz.chatai.data.backend.superbase.models.UserDto
import kotlinx.coroutines.flow.Flow

interface SupabaseRepository {

    fun getAllMessagesByUser(userId: String): Flow<List<MessageDto>>

    fun getNewMessageByUser(userId: String): Flow<MessageDto>

    fun getActivitiesByUser(userId: String): Flow<ActivityDto>

    suspend fun getUserById(userId: String): UserDto?

    suspend fun addNewUser(user: UserDto)

    suspend fun addNewMessage(message: MessageDto)

}
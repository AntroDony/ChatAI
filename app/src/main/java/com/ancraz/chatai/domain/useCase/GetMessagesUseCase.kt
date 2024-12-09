package com.ancraz.chatai.domain.useCase

import com.ancraz.chatai.data.backend.repository.SupabaseRepositoryImpl
import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(
    private val supabaseRepository: SupabaseRepositoryImpl
) {

    fun invoke(userId: String): Flow<List<MessageDto>>{
        return supabaseRepository.getAllMessagesByUser(userId)
    }

    fun startObserveNewMessages(userId: String): Flow<MessageDto>{
        return supabaseRepository.getRealtimeMessagesByUser(userId)
    }
}
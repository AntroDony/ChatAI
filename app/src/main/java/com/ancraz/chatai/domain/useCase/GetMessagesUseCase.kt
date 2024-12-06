package com.ancraz.chatai.domain.useCase

import com.ancraz.chatai.data.backend.superbase.SupabaseRepository
import com.ancraz.chatai.data.models.MessageDto
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(
    private val supabaseRepository: SupabaseRepository
) {

    fun invoke(userId: String): Flow<List<MessageDto>>{
        return supabaseRepository.getMessagesByUser(userId)
    }

    fun startObserveNewMessages(userId: String): Flow<MessageDto>{
        return supabaseRepository.getRealtimeMessagesByUser(userId)
    }
}
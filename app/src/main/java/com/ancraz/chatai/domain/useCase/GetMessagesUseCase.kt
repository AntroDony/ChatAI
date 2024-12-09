package com.ancraz.chatai.domain.useCase

import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import com.ancraz.chatai.domain.repository.SupabaseRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(
    private val supabaseRepository: SupabaseRepository
) {

    fun invoke(userId: String): Flow<List<MessageDto>>{
        return supabaseRepository.getAllMessagesByUser(userId)
    }

    fun startObserveNewMessages(userId: String): Flow<MessageDto>{
        return supabaseRepository.getNewMessageByUser(userId)
    }
}
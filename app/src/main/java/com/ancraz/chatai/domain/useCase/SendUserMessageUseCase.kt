package com.ancraz.chatai.domain.useCase

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.AppPrefs
import com.ancraz.chatai.data.backend.repository.SupabaseRepositoryImpl
import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import com.ancraz.chatai.data.backend.repository.AiApiRepositoryImpl
import com.ancraz.chatai.domain.repository.AiApiRepository
import com.ancraz.chatai.domain.repository.SupabaseRepository
import java.util.Calendar

class SendUserMessageUseCase(
    private val supabaseRepository: SupabaseRepository,
    private val aiApiRepository: AiApiRepository
) {

    suspend fun invoke(messageText: String){
        val chatId = AppPrefs.userId

        insertMessageToDb(messageText, chatId, isBot = false)

        val apiResponse = aiApiRepository.sendMessageToAi(messageText)
        apiResponse?.let { response ->
            insertMessageToDb(response, chatId, isBot = true)
        } ?: run {
            debugLog("api response is null")
        }
    }

    private suspend fun insertMessageToDb(messageText: String, chatId: String, isBot: Boolean){
        val messageDto = createMessageDto(
            message = messageText,
            chatId = chatId,
            isBot = isBot
        )
        supabaseRepository.addNewMessage(messageDto)
    }


    private fun createMessageDto(
        message: String,
        chatId: String,
        isBot: Boolean = false
    ): MessageDto {
        return MessageDto(
            message = message,
            messageTime = Calendar.getInstance().timeInMillis,
            chatId = chatId,
            isBotMessage = isBot
        )
    }
}
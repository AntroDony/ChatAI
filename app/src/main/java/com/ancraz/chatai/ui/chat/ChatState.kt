package com.ancraz.chatai.ui.chat

import com.ancraz.chatai.data.backend.superbase.models.MessageDto


sealed interface ChatState {

    data object Loading: ChatState

    data class Messages(val messageList: List<MessageDto>): ChatState

    data class Error(val errorMessage: String): ChatState
}
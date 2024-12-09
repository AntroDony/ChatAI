package com.ancraz.chatai.data.backend.superbase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(

    @SerialName("text")
    val message: String,

    @SerialName("timestamp")
    val messageTime: Long,

    @SerialName("chat_id")
    val chatId: String,

    @SerialName("is_bot")
    val isBotMessage: Boolean
)

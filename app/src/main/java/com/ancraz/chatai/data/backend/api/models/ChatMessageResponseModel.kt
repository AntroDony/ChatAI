package com.ancraz.chatai.data.backend.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponseModel(

    @SerialName("messages")
    val messages: List<AiBotMessageModel>
)
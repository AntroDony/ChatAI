package com.ancraz.chatai.data.backend.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequestModel(

    @SerialName("content")
    val message: String
)
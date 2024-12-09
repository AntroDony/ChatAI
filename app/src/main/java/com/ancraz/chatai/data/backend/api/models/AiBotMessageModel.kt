package com.ancraz.chatai.data.backend.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiBotMessageModel(

    @SerialName("role")
    val role: String,

    @SerialName("content")
    val content: String?
)
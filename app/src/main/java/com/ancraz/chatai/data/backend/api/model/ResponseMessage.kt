package com.ancraz.chatai.data.backend.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage(

    @SerialName("role")
    val role: String,

    @SerialName("content")
    val content: String?
)
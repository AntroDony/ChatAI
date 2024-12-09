package com.ancraz.chatai.data.backend.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseModel(

    @SerialName("messages")
    val messages: List<ResponseMessage>
)
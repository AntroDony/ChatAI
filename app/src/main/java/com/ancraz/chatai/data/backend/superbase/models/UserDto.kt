package com.ancraz.chatai.data.backend.superbase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    //val id: Long? = null,

    @SerialName("chat_id")
    val chatId: String,

    @SerialName("name")
    val name: String,

    @SerialName("e-mail")
    val email: String,

    @SerialName("password")
    val password: String
)
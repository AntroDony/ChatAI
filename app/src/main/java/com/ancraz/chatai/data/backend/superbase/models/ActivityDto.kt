package com.ancraz.chatai.data.backend.superbase.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityDto(

    @SerialName("topic")
    val activityName: String,

    @SerialName("description")
    val description: String,

    @SerialName("value")
    val activityValue: Int,

    @SerialName("unit")
    val unit: String

)
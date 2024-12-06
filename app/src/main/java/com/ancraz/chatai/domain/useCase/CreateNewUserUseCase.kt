package com.ancraz.chatai.domain.useCase

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.superbase.SupabaseRepository
import com.ancraz.chatai.data.models.UserDto

class CreateNewUserUseCase(
    private val supabaseRepository: SupabaseRepository
) {

    suspend fun invoke(userId: String){
        if (supabaseRepository.getUserById(userId) == null){
            val newUser = UserDto(
                chatId = userId,
                name = "Name",
                email = "E-mail",
                password = "Password"
            )
            supabaseRepository.insertUser(newUser)
            debugLog("insertUser = ${newUser.chatId}")
        }
    }
}
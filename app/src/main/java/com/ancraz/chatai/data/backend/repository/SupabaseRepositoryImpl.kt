package com.ancraz.chatai.data.backend.repository

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.superbase.SupabaseClient
import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import com.ancraz.chatai.data.backend.superbase.models.UserDto
import com.ancraz.chatai.domain.repository.SupabaseRepository
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class SupabaseRepositoryImpl: SupabaseRepository {

    private val supabaseClient: SupabaseClient by lazy {
        SupabaseClient()
    }

//    suspend fun getUsers(): List<UserDto>{
//        val users = supabaseClient.supabase
//            .from("Users")
//            .select()
//            .decodeList<UserDto>()
//
//        debugLog("Users from Supabase: $users")
//
//        return users
//    }


    override suspend fun getUserById(chatId: String): UserDto?{
        try {
            val user = supabaseClient.supabase
                .from("Users")
                .select{
                    filter {
                        eq("chat_id", chatId)
                    }
                }
                .decodeSingle<UserDto>()

            debugLog("User from withId: $user")

            return user
        }
        catch (e: Exception){
            debugLog("getUserByIdExc: ${e.message}")

            return null
        }
    }


    override fun getAllMessagesByUser(userId: String): Flow<List<MessageDto>>{
        return flow {
            val messages = supabaseClient.supabase
                .from("Messages")
                .select{
                    filter {
                        eq("chat_id", userId)
                    }
                }
                .decodeList<MessageDto>()

            emit(messages)
        }
    }


    override fun getNewMessageByUser(userId: String): Flow<MessageDto> {
        return flow {
            try {
                val insertChannel = supabaseClient.supabase.channel("messagesChannel")
                val dbFlow =  insertChannel.postgresChangeFlow<PostgresAction.Insert>(schema = "public"){
                    table = "Messages"
                    filter("chat_id", FilterOperator.EQ, userId)
                }
                insertChannel.subscribe()

                dbFlow.collect { result ->
                    val messageDto = Json{ignoreUnknownKeys = true}
                        .decodeFromString<MessageDto>(result.record.toString())

                    emit(messageDto)
                }
            }
            catch (e: Exception){
                debugLog("getRealtimeMessageException: ${e.message}")
            }
        }
    }


    override fun getActivitiesByUser(userId: String): Flow<ActivityDto> {
        //TODO("Not yet implemented")

        return flow {

        }
    }


    override suspend fun addNewUser(user: UserDto){
        try {
            val newUser = supabaseClient.supabase
                .from("Users")
                .insert(user){
                    select()
                }.decodeSingle<UserDto>()

            debugLog("New User: $newUser")
        }
        catch (e: Exception){
            debugLog("insertUserExc: ${e.message}")
        }

    }


    override suspend fun addNewMessage(message: MessageDto){
        try {
            val newMessage = supabaseClient.supabase
                .from("Messages")
                .insert(message){
                    select()
                }.decodeSingle<MessageDto>()

            debugLog("Insert Message: ${newMessage.message}")
        }
        catch (e: Exception){
            debugLog("insertMessageExc: ${e.message}")
        }

    }


}
package com.ancraz.chatai.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.AppPrefs
import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import com.ancraz.chatai.data.backend.repository.SupabaseRepositoryImpl
import com.ancraz.chatai.data.backend.repository.AiApiRepositoryImpl
import com.ancraz.chatai.domain.repository.AiApiRepository
import com.ancraz.chatai.domain.repository.SupabaseRepository
import com.ancraz.chatai.domain.useCase.CreateNewUserUseCase
import com.ancraz.chatai.domain.useCase.GetActivitiesDataUseCase
import com.ancraz.chatai.domain.useCase.GetMessagesUseCase
import com.ancraz.chatai.domain.useCase.SendUserMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    //todo move to DI
    private val supabaseRepository: SupabaseRepository by lazy {
        SupabaseRepositoryImpl()
    }

    private val aiApiRepository: AiApiRepository by lazy {
        AiApiRepositoryImpl()
    }

    private val sendMessageUseCase: SendUserMessageUseCase by lazy {
        SendUserMessageUseCase(
            supabaseRepository = supabaseRepository,
            aiApiRepository = aiApiRepository
        )
    }

    private val getMessagesUseCase: GetMessagesUseCase by lazy {
        GetMessagesUseCase(supabaseRepository)
    }

    private val createNewUserUseCase: CreateNewUserUseCase by lazy {
        CreateNewUserUseCase(supabaseRepository)
    }

    private val getActivitiesDataUseCase: GetActivitiesDataUseCase by lazy {
        GetActivitiesDataUseCase(aiApiRepository)
    }


    private val _messagesState = MutableStateFlow<List<MessageDto>>(emptyList())
    val messageState = _messagesState.asStateFlow()


    init {
        initializeUser()
        getChatMessages()
    }


    private fun getChatMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            getMessagesUseCase.invoke(AppPrefs.userId).collect { messageList ->
                debugLog("getChatMessages(): $messageList")
                _messagesState.value = messageList.sortByTimestamp() //ChatState.Messages()
            }
            getActivitiesDataUseCase.invoke(AppPrefs.userId).collect{

            }

            collectMessagesFromDatabase()
        }
    }


    private suspend fun collectMessagesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            getMessagesUseCase.startObserveNewMessages(AppPrefs.userId).collect { newMessage ->
//            val newChatList = if (messageState.value is ChatState.Messages){
//                (messageState.value as ChatState.Messages).messageList.toMutableList()
//            } else emptyList()

                val newChatList = _messagesState.value.toMutableList()

                newChatList.add(newMessage)

                _messagesState.emit(newChatList.sortByTimestamp()) //ChatState.Messages()
                //_messagesState.value = messageList.reversed()

                debugLog("newChatList: ${_messagesState.value}")
            }
        }
    }


    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendMessageUseCase.invoke(message)
        }
    }


    private fun initializeUser() {
        debugLog("initializeUser")
        viewModelScope.launch(Dispatchers.IO) {
            createNewUserUseCase.invoke(AppPrefs.userId)
        }
    }


    private fun List<MessageDto>.sortByTimestamp(): List<MessageDto>{
        return this.sortedBy {
            it.messageTime
        }.reversed()
    }
}

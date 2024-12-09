package com.ancraz.chatai.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.AppPrefs
import com.ancraz.chatai.data.backend.superbase.models.MessageDto
import com.ancraz.chatai.data.backend.repository.SupabaseRepositoryImpl
import com.ancraz.chatai.data.backend.repository.AiApiRepositoryImpl
import com.ancraz.chatai.domain.useCase.CreateNewUserUseCase
import com.ancraz.chatai.domain.useCase.GetMessagesUseCase
import com.ancraz.chatai.domain.useCase.SendUserMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    //todo move to DI
    private val supabaseRepository: SupabaseRepositoryImpl by lazy {
        SupabaseRepositoryImpl()
    }

    private val sendMessageUseCase: SendUserMessageUseCase by lazy {
        SendUserMessageUseCase(
            supabaseRepository = supabaseRepository,
            aiApiRepository = AiApiRepositoryImpl()
        )
    }

    private val getMessagesUseCase: GetMessagesUseCase by lazy {
        GetMessagesUseCase(supabaseRepository)
    }

    private val createNewUserUseCase: CreateNewUserUseCase by lazy {
        CreateNewUserUseCase(supabaseRepository)
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
            collectMessagesFromDatabase()
        }
    }


    private suspend fun collectMessagesFromDatabase() {
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


    fun sendMessage(message: String) {
        viewModelScope.launch {
            sendMessageUseCase.invoke(message)
        }

    }


    private fun initializeUser() {
        debugLog("initializeUser")
        viewModelScope.launch {
            createNewUserUseCase.invoke(AppPrefs.userId)
        }
    }

    private fun List<MessageDto>.sortByTimestamp(): List<MessageDto>{
        return this.sortedBy {
            it.messageTime
        }.reversed()
    }
}

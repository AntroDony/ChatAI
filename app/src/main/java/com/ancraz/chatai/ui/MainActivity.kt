package com.ancraz.chatai.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ancraz.chatai.data.AppPrefs
import com.ancraz.chatai.ui.chat.ChatScreen
import com.ancraz.chatai.ui.theme.ChatAiTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        //enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        AppPrefs.initPrefs(context = applicationContext)

        setContent {
            ChatAiTheme {
                val chatState by viewModel.messageState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    ChatScreen(
                       chatState
                    ){ messageText ->
                        viewModel.sendMessage(messageText)
                    }
                }
            }
        }
    }
}

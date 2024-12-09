package com.ancraz.chatai.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ancraz.chatai.R
import com.ancraz.chatai.data.AppPrefs
import com.ancraz.chatai.ui.chat.ChatScreen
import com.ancraz.chatai.ui.chat.ChatViewModel
import com.ancraz.chatai.ui.dashboard.DashboardScreen
import com.ancraz.chatai.ui.dashboard.DashboardViewModel
import com.ancraz.chatai.ui.navigation.BottomNavigationItem
import com.ancraz.chatai.ui.theme.BotMessageBoxColor
import com.ancraz.chatai.ui.theme.ChatAiTheme
import com.ancraz.chatai.ui.theme.ChatBackgroundColor
import com.ancraz.chatai.ui.theme.MainTextColor

class MainActivity : ComponentActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    private val dashboardViewModel: DashboardViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        //enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        AppPrefs.initPrefs(context = applicationContext)

        setContent {
            ChatAiTheme {
                val chatMessages by chatViewModel.messageState.collectAsState()
                val activities by dashboardViewModel.activitiesState.collectAsState()

                val navigationItems = listOf(
                    BottomNavigationItem(
                        title = "Chat",
                        selectedIcon = R.drawable.chat_nav_icon,
                        unselectedIcon = R.drawable.chat_nav_icon
                        ),

                    BottomNavigationItem(
                        title = "Activities",
                        selectedIcon = R.drawable.dashboard_nav_icon,
                        unselectedIcon = R.drawable.dashboard_nav_icon
                    )
                )

                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = ChatBackgroundColor
                        ) {
                            navigationItems.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        //navController.navigate(item.title)
                                    },
                                    label = {
                                        Text(
                                            text = item.title
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(item.selectedIcon),
                                            contentDescription = item.title,
                                            tint = if (index == selectedItemIndex){
                                                BotMessageBoxColor
                                            } else {
                                                BotMessageBoxColor
                                            },
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                    }
                                )
                            }

                        }
                    }
                ) { innerPadding ->
                    if (selectedItemIndex == 0){
                        ChatScreen(
                            messages = chatMessages,
                            modifier = Modifier.padding(innerPadding),
                            sendMessage = { messageText ->
                                chatViewModel.sendMessage(messageText)
                            }
                        )
                    } else {
                        DashboardScreen(
                            activities = activities,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                }
            }
        }
    }
}


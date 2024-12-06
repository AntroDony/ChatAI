package com.ancraz.chatai.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.models.MessageDto
import com.ancraz.chatai.ui.theme.BotMessageBoxColor
import com.ancraz.chatai.ui.theme.ChatBackgroundColor
import com.ancraz.chatai.ui.theme.MessageTextFieldColor
import com.ancraz.chatai.ui.theme.MyMessageBoxColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    messages: List<MessageDto>,
    sendMessage: (String) -> Unit
) {
//    LaunchedEffect(key1 = true) {
//        viewModel.getChatMessages()
//    }

    debugLog("recompose ChatScreen")

    //val messagesState by viewModel.messageState.collectAsState()

    debugLog("MutableState in ChatScreen: ${messages}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(ChatBackgroundColor)
            .padding(horizontal = 16.dp)
    ) {


        ChatMessagesList(
            modifier = Modifier.weight(1f),
            messages = messages
        )


        MessageSenderBlock(
            onSendMessage = { messageText ->
                sendMessage(messageText)
            }
        )
    }
}


@Composable
fun ChatMessagesList(
    modifier: Modifier = Modifier,
    messages: List<MessageDto>
) {
    debugLog("recompose ChatMessagesList")
    debugLog("MutableState in ChatMessagesList: ${messages}")

    LazyColumn(
        modifier = modifier
            //.weight(1f)
            .fillMaxWidth(),
        reverseLayout = true
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        items(messages) { message ->
            val isOwnMessage = !message.isBotMessage

            Box(
                contentAlignment = if (isOwnMessage) {
                    Alignment.CenterEnd
                } else {
                    Alignment.CenterStart
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .drawBehind {
                            val cornerRadius = 10.dp.toPx()
                            val triangleHeight = 30.dp.toPx()
                            val triangleWidth = 25.dp.toPx()

                            val trianglePath = Path()
                                .apply {
                                    if (isOwnMessage) {
                                        moveTo(size.width, size.height - cornerRadius)
                                        lineTo(size.width, size.height + triangleHeight)
                                        lineTo(
                                            size.width - triangleWidth,
                                            size.height - cornerRadius
                                        )
                                        close()
                                    } else {
                                        moveTo(0f, size.height - cornerRadius)
                                        lineTo(0f, size.height + triangleHeight)
                                        lineTo(
                                            triangleWidth,
                                            size.height - cornerRadius
                                        )
                                        close()
                                    }
                                }

                            drawPath(
                                path = trianglePath,
                                color = if (isOwnMessage) MyMessageBoxColor else BotMessageBoxColor
                            )
                        }
                        .background(
                            color = if (isOwnMessage) MyMessageBoxColor else BotMessageBoxColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = message.message,
                        color = Color.White
                    )

                    Text(
                        text = message.messageTime.toTimeFormat(),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun MessageSenderBlock(
    onSendMessage: (String) -> Unit
) {

    val inputMessageState = remember { mutableStateOf("") }
    val placeholderTextColor = remember { mutableStateOf(Color.White) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        TextField(
            value = inputMessageState.value,
            onValueChange = {
                inputMessageState.value = it
            },
            placeholder = {
                Text(
                    text = "Enter a message",
                    color = placeholderTextColor.value
                )
            },
            modifier = Modifier
                .weight(1f)
                .background(MessageTextFieldColor)
        )

        IconButton(
            onClick = {
                if (inputMessageState.value.isNotEmpty()) {
                    onSendMessage(inputMessageState.value)

                    inputMessageState.value = ""
                    placeholderTextColor.value = Color.White
                } else {
                    placeholderTextColor.value = Color.Red
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send message"
            )
        }
    }
}


private fun Long.toTimeFormat(): String {
    val date = Date(this)
    // Format date to "HH:mm" format
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}
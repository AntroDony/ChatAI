package com.ancraz.chatai.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.chatai.R
import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.models.MessageDto
import com.ancraz.chatai.ui.theme.BotMessageBoxColor
import com.ancraz.chatai.ui.theme.ChatAiTheme
import com.ancraz.chatai.ui.theme.ChatBackgroundColor
import com.ancraz.chatai.ui.theme.MainTextColor
import com.ancraz.chatai.ui.theme.MyMessageBoxColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    messages: List<MessageDto>,
    sendMessage: (String) -> Unit
) {

    debugLog("recompose ChatScreen")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(ChatBackgroundColor)
            .padding(horizontal = 16.dp)
    ) {

        if (messages.isNotEmpty()){
            ChatMessagesList(
                modifier = Modifier.weight(1f),
                messages = messages
            )
        }
        else {
            EmptyMessageListPlaceholder(
                modifier = Modifier.weight(1f)
            )
        }

        ChatBox(
            onSendMessage = { messageText ->
                sendMessage(messageText)
            }
        )
    }
}


@Composable
fun EmptyMessageListPlaceholder(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.empty_chat_placeholder),
                contentDescription = "Chat Icon",
                tint = Color.White,
                modifier = Modifier.size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome to ChatAI! \nNo messages in a chat yet. Try ChatAI right now.",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }


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
                    .animateItem()
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
                        color = MainTextColor
                    )

                    Text(
                        text = message.messageTime.toTimeFormat(),
                        color = MainTextColor,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun ChatBox(
    onSendMessage: (String) -> Unit
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    var hasTextFieldError by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .onFocusChanged {  },
            value = chatBoxValue,
            onValueChange = { newText ->
                hasTextFieldError = false
                chatBoxValue = newText
            },
            maxLines = 1,
            placeholder = {
                Text(text = "Message...")
            },
            textStyle = TextStyle(color = MainTextColor, fontSize = 16.sp),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = if (hasTextFieldError) Color.Red else MyMessageBoxColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color.Gray,
                cursorColor = MainTextColor,

                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),

            
        )
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MyMessageBoxColor,
                contentColor = MainTextColor
            ),
            onClick = {
                if (chatBoxValue.text.isNotEmpty()) {
                    onSendMessage(chatBoxValue.text)
                    chatBoxValue = TextFieldValue("")
                }
                else {
                    hasTextFieldError = true
                }
            },
            content = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message"
                )
            }
        )
    }
}


private fun Long.toTimeFormat(): String {
    val date = Date(this)
    // Format date to "HH:mm" format
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}


@PreviewLightDark
@Composable
fun ChatScreenPreview(){
    ChatAiTheme {
        ChatScreen(
            messages = listOf(
                MessageDto(message = "Hello", messageTime = Calendar.getInstance().timeInMillis, chatId = "1", isBotMessage = false),
                MessageDto(message = "Hello", messageTime = Calendar.getInstance().timeInMillis, chatId = "1", isBotMessage = true),
                MessageDto(message = "How are you?", messageTime = Calendar.getInstance().timeInMillis, chatId = "1", isBotMessage = false),
                MessageDto(message = "I'm fine. And you?", messageTime = Calendar.getInstance().timeInMillis, chatId = "1", isBotMessage = true),
                MessageDto(message = "Me too.", messageTime = Calendar.getInstance().timeInMillis, chatId = "1", isBotMessage = false)
            )
        ) { }
    }
}
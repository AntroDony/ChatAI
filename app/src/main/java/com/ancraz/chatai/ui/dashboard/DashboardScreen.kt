package com.ancraz.chatai.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ancraz.chatai.R
import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import com.ancraz.chatai.ui.theme.ChatAiTheme
import com.ancraz.chatai.ui.theme.ChatBackgroundColor
import com.ancraz.chatai.ui.theme.MainTextColor
import com.ancraz.chatai.ui.theme.MyMessageBoxColor

@Composable
fun DashboardScreen(
    activities: List<ActivityDto>,
    modifier: Modifier = Modifier
){
    debugLog("recompose DashboardScreen")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ChatBackgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        if (activities.isNotEmpty()) {
            ActivitiesList(activities = activities)
        }
        else {
            EmptyActivityListPlaceholder()
        }
    }
}


@Composable
private fun ActivitiesList(
    activities: List<ActivityDto>,
    modifier: Modifier = Modifier
){
    debugLog("recompose ActivitiesList")

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        items(activities){ activity ->
            ActivityItem(activity)
        }
    }
}


@Composable
private fun ActivityItem(
    activityDto: ActivityDto,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MyMessageBoxColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = activityDto.activityName,
                    fontSize = 20.sp,
                    color = MainTextColor
                )

                Text(
                    text = activityDto.description,
                    fontSize = 14.sp,
                    color = MainTextColor
                )
            }

            Text(
                text = activityDto.activityValue.toString(),
                fontSize = 22.sp,
                color = MainTextColor,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(32.dp))
}


@Composable
private fun EmptyActivityListPlaceholder(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxSize()
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
                text = "You don't have any activities yet!",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview
@Composable
fun  DashboardScreenPreview(){
    ChatAiTheme {
        DashboardScreen(
            activities = listOf(
                ActivityDto(activityName = "Activity 1", description = "This is description", activityValue = 1, unit = "count"),
                ActivityDto(activityName = "Activity 2", description = "This is description", activityValue = 1000, unit = "count"),
                ActivityDto(activityName = "Activity 3", description = "This is description", activityValue = 112, unit = "count"),
                ActivityDto(activityName = "Activity 4", description = "This is description", activityValue = 2099102, unit = "count"),
                ActivityDto(activityName = "Activity 5", description = "This is description", activityValue = 122, unit = "count"),
                ActivityDto(activityName = "Activity 6", description = "This is description", activityValue = 10, unit = "count")
            )
        )
    }
}

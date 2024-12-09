package com.ancraz.chatai.ui.navigation

import androidx.annotation.DrawableRes

data class BottomNavigationItem(
    val title: String,

    @DrawableRes
    val selectedIcon: Int,

    @DrawableRes
    val unselectedIcon: Int
)
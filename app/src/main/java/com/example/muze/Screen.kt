package com.example.muze

import okhttp3.Route

sealed class Screen (val route: String) {
    object HomeScreen: Screen("home_screen")

    object PlayerScreen: Screen("player_screen")
}
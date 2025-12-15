package com.example.muze

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muze.ui.theme.HomeView
import com.example.muze.ui.theme.PlayerView

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val viewModel: MusicViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeView(viewModel, navController)
        }

        composable(route = Screen.PlayerScreen.route) {
            PlayerView(viewModel, navController)
        }
    }
}
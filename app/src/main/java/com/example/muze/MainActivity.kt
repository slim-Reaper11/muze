package com.example.muze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.muze.ui.theme.MuzeTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.muze.data.Song
import com.example.muze.ui.theme.HomeView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MusicViewModel = viewModel()
            MuzeTheme {
                HomeView(
                    viewModel = viewModel
                )
            }
        }
    }
}


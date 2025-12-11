package com.example.muze.ui.theme

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.muze.R
import com.example.muze.data.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    songs: List<Song>,
    onSongClick: (Song) -> Unit
) {


    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
        }
    }

    if (!hasPermission) {
        PermissionScreen(
            onRetry = {
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
            }
        )
    } else {
        HomeContent(songs = songs, onSongClick = onSongClick)
    }
}

@Composable
fun PermissionScreen(onRetry: () -> Unit) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            colorResource(R.color.background_dark),
            colorResource(R.color.black)
        )
    )
    Box(
        modifier = Modifier
            .background(gradient)
            .fillMaxSize(),

        ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = { TopBar() },

            ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "The app needs access to audio files.", style = Typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    songs: List<Song>,
    onSongClick: (Song) -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            colorResource(R.color.background_dark),
            colorResource(R.color.black)
        )
    )

    Box(modifier = Modifier.background(gradient)) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = { TopBar() }
        ) { padding ->

            LazyColumn(modifier = Modifier.padding(padding)) {
                items(songs) { song ->
                    SongItem(song, onSongClick)
                }
            }
        }
    }
}

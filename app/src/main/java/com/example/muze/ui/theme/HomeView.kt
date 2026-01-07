package com.example.muze.ui.theme

import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.muze.MusicViewModel
import com.example.muze.R
import com.example.muze.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    viewModel: MusicViewModel,
    navController: NavController
) {


    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
        systemUiController.setNavigationBarColor(Color.Black, darkIcons = false)
    }


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
        HomeContent(viewModel = viewModel, navController)
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
    viewModel: MusicViewModel,
    navController: NavController
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            colorResource(R.color.background_dark),
            colorResource(R.color.black)
        )
    )

    val songs = viewModel.allSongs.collectAsState()
    val currentSong = viewModel.currentSong.collectAsState().value








    Box(modifier = Modifier.background(gradient)) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = { TopBar() },


            ) { padding ->


            LazyColumn(modifier = Modifier.padding(padding)) {
                items(songs.value) { song ->
                    SongItem(
                        song, { song ->
                            viewModel.play(songs.value, songs.value.indexOf(song))
                        }, currentSong = currentSong
                    )
                }
                if (currentSong != null) {
                    item {
                        Spacer(modifier = Modifier.height(71.dp))
                    }
                }
            }
        }
        currentSong?.let { song ->
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.PlayerScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                shape = RectangleShape,
                containerColor = Color.Black.copy(alpha = 0.9f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(0.75f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val uri =
                            remember(currentSong.albumID) { albumArtUri(currentSong.albumID) }
                        AsyncImage(
                            model = uri,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(
                                    width = 55.dp,
                                    height = 55.dp
                                )
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.FillBounds,
                            placeholder = painterResource(R.drawable.muze__1_),
                            error = painterResource(R.drawable.muze__1_),
                        )
                        Column {
                            Text(
                                text = song.title,
                                color = Color.White,
                                style = Typography.bodyLarge.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.width(220.dp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = song.artist,
                                color = colorResource(R.color.text_dark),
                                style = Typography.bodyLarge.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.width(220.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.weight(0.25f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {},
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "",
                                modifier = Modifier.size(25.dp),
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                if (viewModel.isPlaying.value) {
                                    viewModel.pause()
                                } else {
                                    viewModel.resume()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = if (viewModel.isPlaying.collectAsState().value) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "",
                                modifier = Modifier.size(25.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


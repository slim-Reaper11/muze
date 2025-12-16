package com.example.muze.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.muze.MusicViewModel
import com.example.muze.R
import com.example.muze.getCornerColors
import io.github.om252345.composemeshgradient.MeshGradient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    viewModel: MusicViewModel,
    navController: NavController
) {

    val state by viewModel.playerState.collectAsState()
    val song = state.currentSong ?: return

    val uri = remember(song.albumID) { albumArtUri(song.albumID) }

    var colors by remember {
        mutableStateOf(
            arrayOf<Color>(
                Color.Black, Color.Black, Color.Black,
                Color.Black, Color.Black, Color.Black,
                Color.Black, Color.Black, Color.Black
            )
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MeshGradient(
            width = 3,
            height = 3,
            points = arrayOf(
                Offset(0f, 0f), Offset(0.5f, 0f), Offset(1f, 0f),
                Offset(0f, 0.5f), Offset(0.5f, 0.5f), Offset(1f, 0.5f),
                Offset(0f, 1f), Offset(0.5f, 1f), Offset(1f, 1f)
            ),
            colors = colors,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        title = {
                            Text(
                                text = "MUZE",
                                style = Typography.titleLarge,
                                color = colorResource(R.color.main_color),
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(start = 8.dp, top = 8.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.navigateUp()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(32.dp)
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {

                            }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(32.dp)
                                )
                            }
                        }
                    )
                }
            )
            { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(75.dp))
                    Box {
//                        Box(
//                            modifier = Modifier
//                                .background(boxGradiant)
//                                .size(380.dp)
//                                .align(Alignment.Center)
//                                .fillMaxWidth()
//                        )
                        //                    Image(
                        //                        painter = painterResource(R.drawable.ic_launcher_background),
                        //                        contentDescription = "",
                        //                        modifier = Modifier
                        //                            .fillMaxWidth()
                        //                            .size(380.dp)
                        //                            .align(Alignment.Center)
                        //                    )
                        AsyncImage(
                            model = uri,
                            contentDescription = "",
                            modifier = Modifier
                                .size(320.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.muze),
                            error = painterResource(R.drawable.muze),
                            onSuccess = { success ->

                                val bitmap = success.result.drawable
                                    .toBitmap()
                                    .copy(Bitmap.Config.ARGB_8888, false)

//                                Palette.from(bitmap).generate { palette ->
//                                    val colorInt =
//                                        palette?.vibrantSwatch?.rgb
//                                            ?: palette?.mutedSwatch?.rgb
//                                            ?: palette?.getDominantColor(Color.Black.toArgb())
//                                            ?: Color.Black.toArgb()
//
//                                    dominantColor = Color(colorInt)
//                                }
                                val corners = getCornerColors(bitmap)

                                colors = arrayOf(
                                    corners.bottomLeft.darkenWithMin(),
                                    corners.middleBottom.darkenWithMin(),
                                    corners.bottomRight.darkenWithMin(),
                                    corners.middleLeft.darkenWithMin(),
                                    corners.middle.darkenWithMin(),
                                    corners.middleRight.darkenWithMin(),
                                    corners.topLeft.darkenWithMin(),
                                    corners.middleTop.darkenWithMin(),
                                    corners.topRight.darkenWithMin(),
                                )


//                                boxGradiant = Brush.verticalGradient(
//                                    colors = listOf(
//                                        corners.topLeft,
//                                        corners.topRight,
//                                        corners.bottomRight.darken(),
//                                        corners.bottomLeft,
//                                        corners.middleTop,
//                                        corners.middle,
//                                        corners.middleBottom
//                                    )
//                                )


//

//                                boxGradiant = Brush.verticalGradient(
//                                    colorStops = arrayOf(
//                                        0.0f to corners.topLeft,
//                                        0.15f to corners.middleTop,
//                                        0.25f to corners.topRight,
//
//                                        0.40f to corners.middleLeft,
//                                        0.50f to corners.middle,       // dominant center
//                                        0.60f to corners.middleRight,
//
//                                        0.75f to corners.bottomLeft,
//                                        0.85f to corners.middleBottom,
//                                        1.0f to corners.bottomRight
//                                    )
//                                )

                            }
                        )


                    }
                    Spacer(modifier = Modifier.height(80.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 46.dp, end = 46.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = song.title,
                                style = Typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.width(280.dp),
                                softWrap = true,
                                color = Color.White
                            )
                            Text(
                                text = song.artist,
                                style = Typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.width(270.dp),
                                color = colorResource(R.color.text_dark)
                            )
                        }
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                    //TODO add the progressbar
                    Spacer(Modifier.height(20.dp))
                    Text("progress bar")
                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                Icons.Default.Shuffle,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White,

                                )
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.SkipPrevious,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                if (state.isPlaying) {
                                    viewModel.pause()
                                } else {
                                    viewModel.resume()
                                }
                            },
                            modifier = Modifier.size(90.dp)
                        ) {
                            if (state.isPlaying) {
                                Icon(
                                    Icons.Default.PauseCircle,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = Color.White
                                )
                            } else {
                                Icon(
                                    Icons.Default.PlayCircle,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = Color.White
                                )
                            }
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.SkipNext,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                    }

                }
            }
        }
    }
}

fun Color.darken(factor: Float = 0.65f): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}

fun Color.darkenWithMin(factor: Float = 0.65f, min: Float = 0.15f): Color {
    return Color(
        red = maxOf(red, min),
        green = maxOf(green, min),
        blue = maxOf(blue, min),
        alpha = alpha
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PlayerPreview() {
//    PlayerView()
//}

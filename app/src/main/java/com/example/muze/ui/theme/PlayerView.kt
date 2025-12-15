package com.example.muze.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.muze.MusicViewModel
import com.example.muze.R
import com.example.muze.data.Song
import com.example.muze.getCornerColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    viewModel: MusicViewModel,
    navController: NavController
) {

    val state by viewModel.playerState.collectAsState()
    val song = state.currentSong ?: return

    val uri = remember(song.albumID) { albumArtUri(song.albumID) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            colorResource(R.color.background_dark),
            colorResource(R.color.black)
        )
    )

    var boxGradiant by remember {
        mutableStateOf(
            Brush.verticalGradient(
                listOf(Color.Black, Color.Black)
            )
        )
    }

    Box(
        modifier = Modifier
            .background(boxGradiant)
            .fillMaxSize()
    ) {
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
                                    Icons.Default.ArrowDropDown,
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
//                                .blur(radius = 1.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
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

                                val corners = getCornerColors(bitmap)
                                boxGradiant = Brush.linearGradient(
                                    colors = listOf(
                                        corners.topLeft,
                                        corners.topRight,
                                        corners.bottomRight.darken(),
                                        corners.bottomLeft.darken()
                                    )
                                )
                            }
                        )


                    }
                    Spacer(modifier = Modifier.height(60.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 46.dp, end = 46.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column() {
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
                    IconButton(
                        onClick = {
                            viewModel.pause()
                        }
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "")
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


//@Preview(showBackground = true)
//@Composable
//fun PlayerPreview() {
//    PlayerView()
//}

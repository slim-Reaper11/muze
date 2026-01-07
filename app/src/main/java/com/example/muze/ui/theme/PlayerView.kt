package com.example.muze.ui.theme

import android.graphics.Bitmap
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.Player
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.muze.MusicViewModel
import com.example.muze.R
import com.example.muze.darkenWithMin
import com.example.muze.getCornerColors
import com.example.muze.longToMinutes
import io.github.om252345.composemeshgradient.MeshGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    viewModel: MusicViewModel,
    navController: NavController
) {

    val song by viewModel.currentSong.collectAsState()


    var colors by remember {
        mutableStateOf(
            arrayOf<Color>(
                Color.Black, Color.Black, Color.Black,
                Color.Black, Color.Black, Color.Black,
                Color.Black, Color.Black, Color.Black
            )
        )
    }

    val transition = updateTransition(
        targetState = viewModel.isPlaying.collectAsState(),
        label = null
    )

    val imageSize by transition.animateDp(
        transitionSpec = { tween(100, easing = LinearEasing) },
        label = "",
        targetValueByState = { isPlaying ->
            if (isPlaying.value) 320.dp else 310.dp
        },
    )
    val backgroundShade by transition.animateColor(
        transitionSpec = { tween(100, easing = LinearEasing) },
        targetValueByState = { isPlaying ->
            if (isPlaying.value) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.6f)
        }
    )
    val textColor by transition.animateColor(
        transitionSpec = { tween(100, easing = LinearEasing) },
        targetValueByState = { isPlaying ->
            if (isPlaying.value) Color.White else Color.White.copy(alpha = 0.8f)
        }
    )

    val alpha = remember { Animatable(0.4f) }
    val scope = rememberCoroutineScope()


    song?.let { song ->
        val uri = remember(song.albumID) { albumArtUri(song.albumID) }

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
                    .background(Color.Black.copy(alpha = alpha.value))
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
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        //image
                        Box(modifier = Modifier.size(320.dp)) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(imageSize)
                                    .align(Alignment.Center),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.muze__1_),
                                error = painterResource(R.drawable.muze__1_),
                                onError = {
                                    colors = arrayOf(
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin(),
                                        Color.Black.darkenWithMin()
                                    )
                                },
                                onSuccess = { success ->

                                    val bitmap = success.result.drawable
                                        .toBitmap()
                                        .copy(Bitmap.Config.ARGB_8888, false)
                                    val corners = getCornerColors(bitmap)

                                    scope.launch {
                                        delay(250)
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
                                    }
                                }
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            // song name and favorite button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 46.dp, end = 46.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(0.75f)) {
                                    Text(
                                        text = song.title,
                                        style = Typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        ),
                                        maxLines = 1,
//                                        modifier = Modifier.width(280.dp),
                                        overflow = TextOverflow.Clip,
                                        color = textColor
                                    )
                                    Text(
                                        text = song.artist,
                                        style = Typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp
                                        ),
//                                        modifier = Modifier.width(270.dp),
                                        color = textColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                    )
                                }
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "",
                                    modifier = Modifier.size(30.dp),
                                    tint = Color.White
                                )
                            }

                            //slider

                            Slider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                                    .height(35.dp),
                                value = viewModel.currentPosition.collectAsState().value.toFloat(),
                                onValueChange = {
                                    viewModel.updateIsChanging(true)
                                    val currentPosition = it.toLong()
                                    viewModel.updateCurrentPosition(currentPosition)
                                },
                                onValueChangeFinished = {
                                    viewModel.goToCurrentPosition(viewModel.currentPosition.value)
                                    viewModel.updateIsChanging(false)
                                },
                                valueRange = 0f..viewModel.duration.collectAsState().value.toFloat(),
                                thumb = {
                                    Box(
                                        Modifier
                                            .size(15.dp)
                                            .background(Color.White, shape = CircleShape)
                                    )
                                },
                                track = {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color.White, RectangleShape)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            )

                            //song duration
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 46.dp, end = 46.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = longToMinutes(viewModel.currentPosition.collectAsState().value),
                                    color = Color.White,
                                    fontSize = 14.sp

                                )
                                Text(
                                    text = longToMinutes(viewModel.duration.collectAsState().value),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                            //buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 30.dp, end = 30.dp, top = 30.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                NoRippleIconButton(
                                    onClick = {
                                        viewModel.shuffleMode()
                                    },
                                    size = 30.dp
                                ) {
                                    Icon(
                                        Icons.Default.Shuffle,
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = if (viewModel.isShuffled.collectAsState().value) colorResource(
                                            R.color.main_color
                                        ).copy(alpha = 0.9f) else Color.White
                                    )
                                }
                                NoRippleIconButton(
                                    onClick = {
//                                        scope.launch {
//                                            if (viewModel.isPlaying.value) {
//                                                alpha.animateTo(
//                                                    targetValue = 1f,
//                                                    animationSpec = tween(
//                                                        durationMillis = 250,
//                                                        easing = LinearEasing
//                                                    )
//                                                )
//                                                alpha.animateTo(
//                                                    targetValue = 0.4f,
//                                                    animationSpec = tween(
//                                                        durationMillis = 250,
//                                                        easing = LinearEasing
//                                                    )
//                                                )
//                                            } else {
//                                                alpha.animateTo(
//                                                    targetValue = 1f,
//                                                    animationSpec = tween(
//                                                        durationMillis = 250,
//                                                        easing = LinearEasing
//                                                    )
//                                                )
//                                                alpha.animateTo(
//                                                    targetValue = 0.6f,
//                                                    animationSpec = tween(
//                                                        durationMillis = 250,
//                                                        easing = LinearEasing
//                                                    )
//                                                )
//                                            }
//                                        }

                                        viewModel.previousSong()
                                    },
                                    size = 40.dp
                                ) {
                                    Icon(
                                        Icons.Default.SkipPrevious,
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color.White
                                    )
                                }
                                NoRippleIconButton(
                                    onClick = {

                                        if (viewModel.isPlaying.value) {

                                            viewModel.pause()
                                        } else {
                                            viewModel.resume()
                                        }
                                    },
                                    size = 100.dp
                                ) {
                                    if (viewModel.isPlaying.collectAsState().value) {
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

                                NoRippleIconButton(
                                    onClick = {
                                        viewModel.nextSong()
                                    },
                                    size = 40.dp
                                ) {
                                    Icon(
                                        Icons.Default.SkipNext,
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color.White
                                    )
                                }
//
                                NoRippleIconButton(
                                    onClick = {
                                        viewModel.toggleRepeatMode()
                                    },
                                    size = 30.dp
                                ) {
                                    Icon(
                                        when (viewModel.repeatMode.collectAsState().value) {
                                            Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
                                            Player.REPEAT_MODE_ALL -> Icons.Default.Repeat
                                            else -> Icons.Default.Repeat
                                        },
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = if (viewModel.repeatMode.collectAsState().value == Player.REPEAT_MODE_OFF) Color.White else colorResource(
                                            R.color.main_color
                                        ).copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoRippleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size) // touch target
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

package com.example.muze.ui.theme

import android.content.ContentUris
import android.graphics.drawable.shapes.RoundRectShape
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.muze.R
import com.example.muze.data.Song

@Composable
fun SongItem(
    song: Song,
    onSongClick: (Song) -> Unit,
    currentSong: Song?
) {

    val uri = remember(song.albumID) { albumArtUri(song.albumID) }


    Box (
        modifier = Modifier.padding(horizontal = 4.dp)
    ){
        Box(
            modifier = if (song == currentSong) Modifier
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.song_border),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 4.dp, horizontal = 2.dp) else Modifier.padding(
                vertical = 4.dp,
                horizontal = 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 12.dp, bottom = 4.dp)
                    .clickable(onClick = { onSongClick(song) }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Row(
                    modifier = Modifier.weight(0.9f),
                    verticalAlignment = Alignment.CenterVertically,
                    ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(
                                width = 60.dp,
                                height = 60.dp
                            )
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.muze__1_),
                        error = painterResource(R.drawable.muze__1_),
                        alignment = Alignment.Center,

                        )
                    Column {
                        Text(
                            text = song.title,
                            color = if (song == currentSong) colorResource(R.color.main_color) else Color.White,
                            style = Typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = song.artist,
                            color = if (song == currentSong) Color.White else colorResource(R.color.text_dark),
                            style = Typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }

                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = {},
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }

            }
        }
    }
}

fun albumArtUri(albumId: Long): Uri =
    ContentUris.withAppendedId(
        "content://media/external/audio/albumart".toUri(),
        albumId
    )
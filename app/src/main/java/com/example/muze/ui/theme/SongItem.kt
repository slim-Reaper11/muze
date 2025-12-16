package com.example.muze.ui.theme

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.muze.R
import com.example.muze.data.Song
import androidx.core.net.toUri

@Composable
fun SongItem(
    song: Song,
    onSongClick: (Song) -> Unit,
    currentSong: Song?
) {

    val uri = remember(song.albumID) { albumArtUri(song.albumID) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 16.dp, start = 24.dp)
            .clickable(onClick = { onSongClick(song) }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            AsyncImage(
                model = uri,
                contentDescription = "",
                modifier = Modifier
                    .size(
                        width = 75.dp,
                        height = 75.dp
                    )
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        0.4.dp,
                        colorResource(R.color.song_border),
                        RoundedCornerShape(4.dp)
                    ),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(R.drawable.muze),
                error = painterResource(R.drawable.muze),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = song.title,
                    color = if (song == currentSong) colorResource(R.color.main_color) else Color.White,
                    style = Typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    modifier = Modifier.width(260.dp)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = song.artist,
                    color = if (song == currentSong) Color.White else colorResource(R.color.text_dark),
                    style = Typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }

        IconButton(
            onClick = {},
        ) {
//            Icon(Icons.Default.MoreVert, contentDescription = "", tint = Color.White)
        }

    }
}

fun albumArtUri(albumId: Long): Uri =
    ContentUris.withAppendedId(
        "content://media/external/audio/albumart".toUri(),
        albumId
    )
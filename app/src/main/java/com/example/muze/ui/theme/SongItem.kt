package com.example.muze.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muze.R
import com.example.muze.data.Song

@Composable
fun SongItem(
    song: Song,
    onSongClick: (Song) -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp, top = 16.dp, start = 16.dp)
            .clickable(onClick = { onSongClick(song) }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(
                        width = 70.dp,
                        height = 70.dp
                    )
                    .clip(
                        RoundedCornerShape(4.dp)
                    )
                    .border(
                        1.dp,
                        colorResource(R.color.song_border),
                        RoundedCornerShape(4.dp)
                    ),
                painter = painterResource(R.drawable.muze),
                contentDescription = "",
                contentScale = ContentScale.Fit,

                )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = song.title,
                    color = Color.White,
                    style = Typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                )
                Text(
                    text = song.artist,
                    color = colorResource(R.color.text_dark),
                    style = Typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
            }
        }

        IconButton(
            onClick = {},
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "", tint = Color.White)
        }

    }
}

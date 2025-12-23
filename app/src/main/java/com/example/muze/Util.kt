package com.example.muze

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class CornerColors(
    val topLeft: Color,
    val topRight: Color,
    val bottomLeft: Color,
    val bottomRight: Color,
    val middleRight: Color,
    val middleLeft: Color,
    val middleTop: Color,
    val middleBottom: Color,
    val middle: Color
)

fun sampleColor(
    bitmap: Bitmap,
    startX: Int,
    startY: Int,
    sampleSize: Int = 24
): Color {
    var r = 0
    var g = 0
    var b = 0
    var count = 0

    val endX = (startX + sampleSize).coerceAtMost(bitmap.width)
    val endY = (startY + sampleSize).coerceAtMost(bitmap.height)

    for (x in startX until endX) {
        for (y in startY until endY) {
            val pixel = bitmap.getPixel(x, y)
            r += android.graphics.Color.red(pixel)
            g += android.graphics.Color.green(pixel)
            b += android.graphics.Color.blue(pixel)
            count++
        }
    }

    return Color(
        red = r / count,
        green = g / count,
        blue = b / count
    )
}

fun getCornerColors(bitmap: Bitmap): CornerColors {
    val w = bitmap.width
    val h = bitmap.height
    val s = 24

    return CornerColors(
        topLeft = sampleColor(bitmap, 0, 0, s),
        topRight = sampleColor(bitmap, w - s, 0, s),
        bottomLeft = sampleColor(bitmap, 0, h - s, s),
        bottomRight = sampleColor(bitmap, w - s, h - s, s),
        middleTop = sampleColor(bitmap, w / 2, 0, s),
        middleLeft = sampleColor(bitmap, 0, h / 2, s),
        middleRight = sampleColor(bitmap, w - s, h / w, s),
        middleBottom = sampleColor(bitmap, w / 2, h - s, s),
        middle = sampleColor(bitmap, w / 2, h / 2, s)
    )
}


fun longToMinutes(long: Long): String {
    val totalSeconds = long / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun Color.darkenWithMin(factor: Float = 0.65f, min: Float = 0.15f): Color {
    return Color(
        red = maxOf(red, min),
        green = maxOf(green, min),
        blue = maxOf(blue, min),
        alpha = alpha
    )
}
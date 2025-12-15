package com.example.muze

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class CornerColors(
    val topLeft: Color,
    val topRight: Color,
    val bottomLeft: Color,
    val bottomRight: Color
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
        bottomRight = sampleColor(bitmap, w - s, h - s, s)
    )
}

package com.softcross.motikoc.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor

@Composable
fun DashedProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Int,
    totalNumberOfBars: Int
) {
    Canvas(modifier = modifier) {
        val barArea = size.width / totalNumberOfBars
        val barLength = barArea - 10.dp.toPx()

        var nextBarStartPosition = 0F

        for (i in 0..<totalNumberOfBars) {
            val barStartPosition = nextBarStartPosition + 5.dp.toPx()
            val barEndPosition = barStartPosition + barLength

            val start = Offset(x = barStartPosition, y = size.height / 2)
            val end = Offset(x = barEndPosition, y = size.height / 2)

            drawLine(
                cap = StrokeCap.Round,
                color = if (i < progress) TextColor else TextColor.copy(alpha = .5F),
                start = start,
                end = end,
                strokeWidth = 13F
            )

            nextBarStartPosition = barEndPosition + 5.dp.toPx()
        }
    }
}
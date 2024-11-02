package com.softcross.motikoc.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.TextColor
import kotlin.math.round

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    height: Dp = 500.dp,
) {

    val chartData = listOf(
        Pair("16.08", 90),
        Pair("II", 110),
        Pair("III", 70),
        Pair("IV", 205),
        Pair("V", 150),
        Pair("VI", 175),
        Pair("IV", 205),
        Pair("V", 150),
        Pair("VI", 175),
        Pair("IV", 205),
        Pair("V", 150),
        Pair("VI", 175),
        Pair("IV", 205),
        Pair("V", 150),
        Pair("VI", 175)
    )

    val spacingFromLeft = 0f
    val spacingFromBottom = 0f

    val upperValue = remember { (chartData.maxOfOrNull { it.second }?.plus(1)) ?: 0 }
    val lowerValue = remember { (chartData.minOfOrNull { it.second }?.toInt() ?: 0) }

    val density = LocalDensity.current

    //paint for the text shown in data values
    val textPaint = remember(density) {
        Paint().apply {
            color = TextColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    androidx.compose.foundation.Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(BackgroundColor)
            .padding(16.dp)
    ) {

        val canvasHeight = size.height
        val canvasWidth = size.width

        val spacePerData = (canvasWidth - spacingFromLeft) / chartData.size


        val valuesToShow = 5f  //we will show 5 data values on vertical line

        val eachStep = (upperValue - lowerValue) / valuesToShow
        //data shown vertically


        //Horizontal line
        drawLine(
            start = Offset(spacingFromLeft + 10f, canvasHeight - spacingFromBottom),
            end = Offset(canvasWidth - 25f, canvasHeight - spacingFromBottom),
            color = GoldColor,
            strokeWidth = 15f
        )

        //draw bars
        chartData.forEachIndexed { index, chartPair ->

            //draw text at top of each bar
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    chartPair.second.toString(),
                    spacingFromLeft + 30f + index * spacePerData,
                    (upperValue - chartPair.second.toFloat()) / upperValue * canvasHeight - 10f,
                    textPaint
                )
            }

            //draw Bar for each value
            drawRoundRect(
                color = GoldColor,
                topLeft = Offset(
                    spacingFromLeft + 10f + index * spacePerData,
                    (upperValue - chartPair.second.toFloat()) / upperValue * canvasHeight
                ),
                size = Size(
                    30f,
                    (chartPair.second.toFloat() / upperValue) * canvasHeight - spacingFromBottom
                ),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }
    }
}

@Preview
@Composable
private fun Prew() {
    MotikocTheme {
        BarChart()
    }
}



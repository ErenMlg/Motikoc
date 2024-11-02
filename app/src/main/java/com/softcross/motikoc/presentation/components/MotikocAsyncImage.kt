package com.softcross.motikoc.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import com.softcross.motikoc.R

@Composable
fun MotikocAsyncImage(
    model: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorIcon: Int = R.drawable.ic_launcher_foreground
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        loading = {
            Image(
                painter = painterResource(id = R.drawable.icon_loading),
                contentScale = FixedScale(4f),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = "Loading"
            )
        },
        error = {
            Image(
                painter = painterResource(id = errorIcon),
                contentScale = FixedScale(4f),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "Error"
            )
        }
    )
}
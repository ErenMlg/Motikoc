package com.softcross.motikoc.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.presentation.theme.LightBackground
import com.softcross.motikoc.presentation.theme.PrimaryGreen
import com.softcross.motikoc.presentation.theme.PrimaryRed

enum class SnackbarType {
    ERROR,
    SUCCESS
}

@Composable
fun MotikocSnackbar(
    type: SnackbarType,
    message: String,
    modifier: Modifier,
    clear: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            clear()
        }
    }

    val backgroundColor = when (type) {
        SnackbarType.ERROR -> PrimaryRed
        SnackbarType.SUCCESS -> PrimaryGreen
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = message,
            color = LightBackground,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Preview
@Composable
fun SnackbarPreviewError() {
    MaterialTheme {
        MotikocSnackbar(
            type = SnackbarType.ERROR,
            message = "Sel",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun SnackbarPreviewSuccess() {
    MaterialTheme {
        MotikocSnackbar(
            type = SnackbarType.SUCCESS,
            message = "Sel",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
package com.softcross.motikoc.presentation.introduction

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.R
import com.softcross.motikoc.presentation.components.FilledButton
import com.softcross.motikoc.presentation.components.IconFilledButton
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.PoppinsMedium
import com.softcross.motikoc.presentation.theme.TextColor

@Composable
fun IntroductionRoute(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.graduation),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(top = 32.dp)
        )
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier
                    .width(80.dp)
                    .height(50.dp)
                    .align(Alignment.End)
                    .rotate(25f)
            ) {
                drawPath(
                    path = Path().apply {
                        moveTo(20f, 80f)
                        quadraticBezierTo(50f, 90f, 80f, 50.dp.toPx())
                        moveTo(120f, 40f)
                        quadraticBezierTo(120f, 90f, 120f, 50.dp.toPx())
                        moveTo(220f, 80f)
                        quadraticBezierTo(190f, 90f, 160f, 50.dp.toPx())
                    },
                    color = TextColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                )
            }
            Text(
                text = stringResource(R.string.introduction_title),
                color = TextColor,
                fontFamily = PoppinsMedium,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 28.sp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            )
            Text(
                text = stringResource(R.string.introduction_subtitle),
                color = TextColor.copy(alpha = 0.8f),
                fontFamily = PoppinsLight,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 8.dp)
            )
        }
        Column(Modifier.fillMaxWidth(0.8f)) {
            IconFilledButton(
                text = stringResource(R.string.sign_with_google),
                iconID = R.drawable.icon_google
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                FilledButton(
                    text = stringResource(R.string.sign),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) { onRegisterClick() }
                FilledButton(
                    text = stringResource(R.string.login),
                    modifier = Modifier
                ) { onLoginClick() }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun IntroductionPreviewLight() {
    MotikocTheme {
        IntroductionRoute()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IntroductionPreviewDark() {
    MotikocTheme {
        IntroductionRoute()
    }
}
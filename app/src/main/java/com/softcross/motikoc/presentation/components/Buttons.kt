package com.softcross.motikoc.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.DarkBackground
import com.softcross.motikoc.presentation.theme.DarkSurface
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.LightBackground
import com.softcross.motikoc.presentation.theme.LightSurface
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.Poppins
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.TextColor

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = PrimarySurface.copy(alpha = 0.8f),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = text, fontSize = 14.sp,
            color = TextColor,
            fontFamily = Poppins,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun IconOutlinedButton(
    modifier: Modifier = Modifier,
    iconID: Int,
    text: String,
    color: Color = PrimarySurface.copy(alpha = 0.8f),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(3.dp, shape = RoundedCornerShape(8.dp)),
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = iconID),
                tint = TextColor,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text, fontSize = 14.sp,
                color = TextColor,
                fontFamily = Poppins,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun IconFilledButton(
    text: String,
    modifier: Modifier = Modifier,
    iconID: Int,
    color: Color = PrimarySurface,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(3.dp, shape = RoundedCornerShape(8.dp)),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = iconID),
                tint = TextColor,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text, fontSize = 14.sp,
                color = TextColor,
                fontFamily = Poppins,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun FilledButton(
    text: String,
    modifier: Modifier,
    color: Color = PrimarySurface,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(3.dp, shape = RoundedCornerShape(8.dp)),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
        )
    ) {
        Text(
            text = text, fontSize = 14.sp,
            color = TextColor,
            fontFamily = PoppinsLight,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun LoadingFilledButton(
    text: String,
    modifier: Modifier,
    color: Color = PrimarySurface,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(3.dp, shape = RoundedCornerShape(8.dp)),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                trackColor = if (isSystemInDarkTheme()) LightSurface else GoldColor,
                color = if (isSystemInDarkTheme()) DarkSurface else GoldColor.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text, fontSize = 14.sp,
                color = TextColor,
                fontFamily = PoppinsLight,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
fun CircularCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(
                color = LightBackground,
                shape = CircleShape
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = DarkBackground,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
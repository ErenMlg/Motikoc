package com.softcross.motikoc.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.R
import com.softcross.motikoc.presentation.theme.PrimaryRed
import com.softcross.motikoc.presentation.theme.TextColor

@Composable
fun MotikocDefaultErrorField(modifier: Modifier = Modifier, errorMessage:String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Close,
            contentDescription = "",
            modifier = Modifier.size(12.dp),
            tint = PrimaryRed
        )
        Text(
            text = errorMessage,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
            color = TextColor
        )
    }
}

@Composable
fun MotikocPasswordChecker(
    password: String
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.str_password_validation_title),
            fontSize = 12.sp,
            color = TextColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (password.length >= 8) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_one),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if ("([0-9])".toRegex().containsMatchIn(password)) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_two),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if ("([a-z])".toRegex().containsMatchIn(password)) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_three),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if ("([A-Z])".toRegex().containsMatchIn(password)) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            } else {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = PrimaryRed
                )
            }
            Text(
                text = stringResource(R.string.str_password_validation_four),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = TextColor
            )
        }
    }
}

package com.softcross.motikoc.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.common.extensions.emailRegex
import com.softcross.motikoc.common.extensions.passwordRegex
import com.softcross.motikoc.presentation.components.IconTextField
import com.softcross.motikoc.presentation.components.LoadingFilledButton
import com.softcross.motikoc.presentation.components.MotikocDefaultErrorField
import com.softcross.motikoc.presentation.components.MotikocHeader
import com.softcross.motikoc.presentation.components.MotikocPasswordChecker
import com.softcross.motikoc.presentation.components.MotikocSnackbar
import com.softcross.motikoc.presentation.components.SnackbarType
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow

import com.softcross.motikoc.presentation.login.LoginContract.UiAction
import com.softcross.motikoc.presentation.login.LoginContract.UiEffect
import com.softcross.motikoc.presentation.login.LoginContract.UiState
import com.softcross.motikoc.presentation.theme.GoldColor
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginRoute(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToRegister: () -> Unit,
    navigateToJobWizard: () -> Unit,
    navigateToHome: () -> Unit
) {

    var errorMessage by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.ShowSnackbar -> {
                        errorMessage = effect.message
                    }

                    is UiEffect.NavigateToHome -> {
                        navigateToHome()
                    }

                    is UiEffect.NavigateToJobWizard -> {
                        navigateToJobWizard()
                    }

                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        LoginScreen(
            isLoading = uiState.isLoading,
            email = uiState.email,
            password = uiState.password,
            onEmailChanged = { onAction(UiAction.EmailChanged(it)) },
            onPasswordChanged = { onAction(UiAction.PasswordChanged(it)) },
            onLoginClick = { onAction(UiAction.LoginClick) },
            onRegisterClick = { navigateToRegister() }
        )
        if (errorMessage.isNotEmpty()) {
            MotikocSnackbar(
                message = errorMessage,
                type = SnackbarType.ERROR,
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                clear = { errorMessage = "" }
            )
        }
    }

}

@Composable
fun LoginScreen(
    email: String,
    password: String,
    isLoading: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MotikocHeader(title = R.string.app_name, subtitle = R.string.login_subtitle)
        Column(
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            IconTextField(
                modifier = Modifier.padding(top = 16.dp),
                givenValue = email,
                keyboardType = KeyboardType.Email,
                placeHolder = stringResource(R.string.email),
                onValueChange = onEmailChanged,
                regex = String::emailRegex,
            ) {
                MotikocDefaultErrorField(errorMessage = "Lütfen geçerli bir email adresi giriniz!")
            }
            IconTextField(
                modifier = Modifier.padding(top = 16.dp),
                givenValue = password,
                placeHolder = stringResource(R.string.password),
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = if (passwordVisibility) R.drawable.icon_hide_password else R.drawable.icon_show_password),
                        contentDescription = "",
                        tint = GoldColor,
                        modifier = Modifier.clickable {
                            passwordVisibility = !passwordVisibility
                        }
                    )
                },
                onValueChange = onPasswordChanged,
                regex = String::passwordRegex
            ) {
                MotikocPasswordChecker(password = password)
            }
            Text(
                text = stringResource(R.string.lets_register),
                color = TextColor,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .clickable { onRegisterClick() },
            )
        }
        LoadingFilledButton(
            text = stringResource(id = R.string.login),
            isLoading = isLoading,
            isEnabled = email.emailRegex() && password.passwordRegex() && !isLoading,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 16.dp),
            onClick = { onLoginClick(); keyboardController?.hide() }
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun LoginPreviewLight() {
    MotikocTheme {
        LoginRoute(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToRegister = {},
            navigateToJobWizard = {},
            navigateToHome = {}
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun LoginPreviewDark() {
    MotikocTheme {
        LoginRoute(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToRegister = {},
            navigateToJobWizard = {},
            navigateToHome = {}
        )
    }
}
package com.softcross.motikoc.presentation.register


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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.common.extensions.emailRegex
import com.softcross.motikoc.common.extensions.nameSurnameRegexWithSpace
import com.softcross.motikoc.common.extensions.passwordRegex
import com.softcross.motikoc.presentation.components.IconTextField
import com.softcross.motikoc.presentation.components.LoadingFilledButton
import com.softcross.motikoc.presentation.components.MotikocDefaultErrorField
import com.softcross.motikoc.presentation.components.MotikocHeader
import com.softcross.motikoc.presentation.components.MotikocSnackbar
import com.softcross.motikoc.presentation.components.MotikocPasswordChecker
import com.softcross.motikoc.presentation.components.SnackbarType
import com.softcross.motikoc.presentation.register.RegisterContract.UiAction
import com.softcross.motikoc.presentation.register.RegisterContract.UiEffect
import com.softcross.motikoc.presentation.register.RegisterContract.UiState
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun RegisterRoute(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToJobWizard: () -> Unit
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
                        navigateToJobWizard()
                    }

                    UiEffect.NavigateToJobWizard -> {
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
        RegisterScreen(
            isLoading = uiState.isLoading,
            fullName = uiState.fullName,
            email = uiState.email,
            password = uiState.password,
            rePassword = uiState.rePassword,
            onFullNameChanged = { onAction(UiAction.FullNameChanged(it)) },
            onEmailChanged = { onAction(UiAction.EmailChanged(it)) },
            onPasswordChanged = { onAction(UiAction.PasswordChanged(it)) },
            onRePasswordChanged = { onAction(UiAction.ConfirmPasswordChanged(it)) },
            onRegisterClick = { onAction(UiAction.RegisterClick) },
            onLoginClick = { navigateToLogin() }
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
fun RegisterScreen(
    isLoading: Boolean,
    fullName: String,
    email: String,
    password: String,
    rePassword: String,
    onFullNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRePasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MotikocHeader(title = R.string.app_name, subtitle = R.string.register_subtitle)
        Column(
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            IconTextField(
                modifier = Modifier.padding(top = 16.dp),
                givenValue = fullName,
                placeHolder = stringResource(
                    R.string.fullname
                ),
                keyboardType = KeyboardType.Text,
                onValueChange = onFullNameChanged,
                regex = String::nameSurnameRegexWithSpace
            ) {
                MotikocDefaultErrorField(errorMessage = "Lütfen ad boşluk soyad olacak şekilde giriniz!")
            }
            IconTextField(
                modifier = Modifier.padding(top = 16.dp),
                givenValue = email,
                placeHolder = stringResource(R.string.email),
                keyboardType = KeyboardType.Email,
                onValueChange = onEmailChanged,
                regex = String::emailRegex
            ) {
                MotikocDefaultErrorField(errorMessage = "Lütfen geçerli bir email adresi giriniz!")
            }
            IconTextField(
                modifier = Modifier.padding(top = 16.dp),
                givenValue = password,
                placeHolder = stringResource(R.string.password),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = onPasswordChanged,
                regex = String::passwordRegex
            ) {
                MotikocPasswordChecker(password = password)
            }
            IconTextField(modifier = Modifier.padding(top = 16.dp),
                givenValue = rePassword,
                placeHolder = stringResource(
                    R.string.confirm_password
                ),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = onRePasswordChanged,
                regex = { rePassword == password }
            ) {
                MotikocDefaultErrorField(errorMessage = "Şifreleriniz eşleşmiyor!")
            }
            Text(
                text = stringResource(R.string.do_you_have_acc_login),
                color = TextColor,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .clickable { onLoginClick() }
            )
        }
        LoadingFilledButton(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 16.dp),
            text = stringResource(R.string.register),
            isLoading = isLoading,
            isEnabled = fullName.nameSurnameRegexWithSpace() && email.emailRegex() && password.passwordRegex() && rePassword == password && !isLoading,
            onClick = {
                onRegisterClick()
                keyboardController?.hide()
            }
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun RegisterPreviewLight() {
    MotikocTheme {
        RegisterRoute(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToLogin = {},
            navigateToJobWizard = {})
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RegisterPreviewDark() {
    MotikocTheme {
        RegisterRoute(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToLogin = {},
            navigateToJobWizard = {})
    }
}
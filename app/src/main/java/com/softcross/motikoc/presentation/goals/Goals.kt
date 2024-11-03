package com.softcross.motikoc.presentation.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.presentation.components.FilledButton
import com.softcross.motikoc.presentation.components.IdentifyTextField
import com.softcross.motikoc.presentation.components.MotikocDefaultErrorField
import com.softcross.motikoc.presentation.components.MotikocHeader
import com.softcross.motikoc.presentation.components.MotikocSnackbar
import com.softcross.motikoc.presentation.components.SnackbarType
import com.softcross.motikoc.presentation.theme.MotikocTheme

import com.softcross.motikoc.presentation.goals.GoalsContract.UiState
import com.softcross.motikoc.presentation.goals.GoalsContract.UiAction
import com.softcross.motikoc.presentation.goals.GoalsContract.UiEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun Goals(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    uiAction: (UiAction) -> Unit,
    navigateToHome: () -> Unit,
) {
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    var errorMessage by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.NavigateToHome -> navigateToHome()
                    is UiEffect.ShowErrorToast -> { errorMessage = effect.message }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                MotikocHeader(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth(),
                    title = R.string.app_name,
                    subtitle = R.string.goals,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    IdentifyTextField(
                        givenValue = uiState.dreamUniversity,
                        onValueChange = { uiAction(UiAction.OnDreamUniversityChanged(it)) },
                        placeHolder = "Üniversite adı giriniz(X Üniversitesi)",
                        focusRequester = focusRequester,
                        wantedLength = 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        MotikocDefaultErrorField(errorMessage = "10 karakterden az olamaz")
                    }
                    IdentifyTextField(
                        givenValue = uiState.dreamDepartment,
                        onValueChange = { uiAction(UiAction.OnDreamDepartmentChanged(it)) },
                        placeHolder = "Bölüm adı giriniz(Y Bölümü)",
                        focusRequester = focusRequester,
                        wantedLength = 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        MotikocDefaultErrorField(errorMessage = "10 karakterden az olamaz")
                    }
                    Row {
                        IdentifyTextField(
                            givenValue = uiState.dreamPoint,
                            onValueChange = { uiAction(UiAction.OnDreamPointChanged(it)) },
                            keyboardType = KeyboardType.Number,
                            placeHolder = "Puan Hedefiniz",
                            focusRequester = focusRequester,
                            wantedLength = 1,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 8.dp, start = 16.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            MotikocDefaultErrorField(errorMessage = "Boş olamaz")
                        }
                        IdentifyTextField(
                            givenValue = uiState.dreamRank,
                            onValueChange = { uiAction(UiAction.OnDreamRankChanged(it)) },
                            keyboardType = KeyboardType.Number,
                            placeHolder = "Sıralama Hedefiniz",
                            wantedLength = 1,
                            focusRequester = focusRequester,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 16.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            MotikocDefaultErrorField(errorMessage = "Boş olamaz")
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledButton(
                    isEnabled = uiState.dreamUniversity.length > 10 &&
                            uiState.dreamDepartment.length > 10 &&
                            uiState.dreamPoint.isNotEmpty() &&
                            uiState.dreamRank.isNotEmpty(),
                    text = "Devam",
                    modifier = Modifier,
                    onClick = { uiAction(UiAction.OnSaveButtonClicked); keyboardController?.hide() }
                )
            }
        }
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

@Preview(showBackground = true)
@Composable
fun PreviewLight() {
    MotikocTheme {
        Goals(uiState = UiState(), uiAction = {}, navigateToHome = {}, uiEffect = emptyFlow())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDark() {
    MotikocTheme {
        Goals(uiState = UiState(), uiAction = {}, navigateToHome = {}, uiEffect = emptyFlow())
    }
}
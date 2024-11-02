package com.softcross.motikoc.presentation.jobWizard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.presentation.components.DashedProgressIndicator
import com.softcross.motikoc.presentation.components.FilledButton
import com.softcross.motikoc.presentation.components.IdentifyTextField
import com.softcross.motikoc.presentation.components.MotikocDefaultErrorField
import com.softcross.motikoc.presentation.components.MotikocHeader
import com.softcross.motikoc.presentation.components.MotikocSnackbar
import com.softcross.motikoc.presentation.components.MultipleInputCollector
import com.softcross.motikoc.presentation.components.MultipleInputSelector
import com.softcross.motikoc.presentation.components.SnackbarType
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow

import com.softcross.motikoc.presentation.jobWizard.JobWizardContract.UiState
import com.softcross.motikoc.presentation.jobWizard.JobWizardContract.UiEffect
import com.softcross.motikoc.presentation.jobWizard.JobWizardContract.UiAction
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun JobWizard(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToJobSelection: (String, String, String, String, String) -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val state = rememberPagerState(pageCount = { 6 }, initialPage = currentPage)
    var errorMessage by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.ShowSnackbar -> errorMessage = effect.message
                }
            }
        }
    }

    LaunchedEffect(currentPage) {
        state.animateScrollToPage(currentPage)
        if (currentPage != 0) {
            focusManager.moveFocus(FocusDirection.Next)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DashedProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp),
                progress = state.currentPage + 1,
                totalNumberOfBars = state.pageCount
            )
            HorizontalPager(
                state = state,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> JobWizardIntroduction(
                        navigateToInterests = { currentPage = 1 }
                    )

                    1 -> JobWizardInterests(
                        navigateToSkills = { currentPage = 2 },
                        interests = uiState.interests,
                        onInterestAdded = { onAction(UiAction.OnInterestAdded(it)) },
                        onInterestRemoved = { onAction(UiAction.OnInterestRemoved(it)) },
                        focusRequester = focusRequester
                    )

                    2 -> JobWizardSkills(
                        navigateToPersonal = { currentPage = 3 },
                        navigateToInterests = { currentPage = 1 },
                        abilities = uiState.abilities,
                        onAbilityAdded = { onAction(UiAction.OnAbilityAdded(it)) },
                        onAbilityRemoved = { onAction(UiAction.OnAbilityRemoved(it)) },
                        focusRequester = focusRequester
                    )

                    3 -> JobWizardPersonal(
                        navigateToArea = { currentPage = 4 },
                        navigateToSkills = { currentPage = 2 },
                        personalProperties = uiState.personalProperties,
                        onPersonalPropertyAdded = { onAction(UiAction.OnPersonalPropertyAdded(it)) },
                        onPersonalPropertyRemoved = { onAction(UiAction.OnPersonalPropertyRemoved(it)) },
                        focusRequester = focusRequester
                    )

                    4 -> JobWizardArea(
                        navigateToIdentify = { currentPage = 5 },
                        navigateToPersonal = { currentPage = 3 },
                        areas = uiState.area,
                        onAreaAdded = { onAction(UiAction.OnAreaAdded(it)) },
                        onAreaRemoved = { onAction(UiAction.OnAreaRemoved(it)) },
                        focusRequester = focusRequester
                    )

                    5 -> JobWizardIdentify(
                        navigateToJobSelection = {
                            navigateToJobSelection(
                                uiState.personalProperties.joinToString(","),
                                uiState.interests.joinToString(","),
                                uiState.abilities.joinToString(","),
                                uiState.area.joinToString(","),
                                uiState.identifyPrompt
                            )
                        },
                        navigateToArea = { currentPage = 4 },
                        identifyText = uiState.identifyPrompt,
                        onIdentifyPromptChanged = { onAction(UiAction.OnIdentifyPromptChanged(it)) },
                        focusRequester = focusRequester
                    )
                }
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

@Composable
fun JobWizardIntroduction(
    navigateToInterests: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        MotikocHeader(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            title = R.string.job_wizard,
            subtitle = R.string.job_wizard_introduction
        )
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilledButton(
                text = "Devam",
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                onClick = {
                    navigateToInterests()
                }
            )
        }
    }
}

@Composable
fun JobWizardInterests(
    interests: List<String>,
    navigateToSkills: () -> Unit,
    onInterestAdded: (String) -> Unit,
    onInterestRemoved: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val verticalScroll = rememberScrollState()
    var selectionSize by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectionSize) {
        verticalScroll.scrollTo(selectionSize)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .verticalScroll(verticalScroll)
        ) {
            MotikocHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                title = R.string.job_wizard,
                subtitle = R.string.job_wizard_interests
            )
            Column(
                modifier = Modifier
                    .onSizeChanged { selectionSize = it.height }
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Hangi alanlara ilgi duyuyorsun?",
                    color = TextColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                MultipleInputCollector(
                    modifier = Modifier.padding(top = 4.dp),
                    dataList = MotikocSingleton.interestsList,
                    taggedWords = interests,
                    onAdd = onInterestAdded,
                    onRemove = onInterestRemoved,
                    focusRequester = focusRequester
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilledButton(
                text = "Devam",
                isEnabled = interests.size >= 3,
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = {
                    navigateToSkills()
                }
            )
        }
    }
}

@Composable
fun JobWizardSkills(
    navigateToPersonal: () -> Unit,
    navigateToInterests: () -> Unit,
    abilities: List<String>,
    onAbilityAdded: (String) -> Unit,
    onAbilityRemoved: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val verticalScroll = rememberScrollState()
    var selectionSize by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectionSize) {
        if (selectionSize > 0) {
            verticalScroll.scrollTo(selectionSize)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .verticalScroll(verticalScroll)
        ) {
            MotikocHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                title = R.string.job_wizard,
                subtitle = R.string.job_wizard_abilities
            )
            Column(
                modifier = Modifier
                    .onSizeChanged { selectionSize = it.height }
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Hangi yeteneklere sahipsin?",
                    color = TextColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                MultipleInputCollector(
                    modifier = Modifier.padding(top = 4.dp),
                    dataList = MotikocSingleton.abilityList,
                    taggedWords = abilities,
                    onAdd = onAbilityAdded,
                    onRemove = onAbilityRemoved,
                    focusRequester = focusRequester
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledButton(
                text = "Geri",
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = {
                    navigateToInterests()
                }
            )
            FilledButton(
                isEnabled = abilities.size >= 3,
                text = "Devam",
                modifier = Modifier,
                onClick = {
                    navigateToPersonal()
                }
            )
        }
    }
}

@Composable
fun JobWizardPersonal(
    navigateToArea: () -> Unit,
    navigateToSkills: () -> Unit,
    personalProperties: List<String>,
    onPersonalPropertyAdded: (String) -> Unit,
    onPersonalPropertyRemoved: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val verticalScroll = rememberScrollState()
    var selectionSize by remember { mutableIntStateOf(0) }
    LaunchedEffect(selectionSize) {
        if (selectionSize > 0) {
            verticalScroll.scrollTo(selectionSize)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .verticalScroll(verticalScroll)
        ) {
            MotikocHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                title = R.string.job_wizard,
                subtitle = R.string.job_wizard_personal
            )
            Column(
                modifier = Modifier
                    .onSizeChanged { selectionSize = it.height }
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Hangi kişisel özelliklere sahipsin?",
                    color = TextColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                MultipleInputCollector(
                    modifier = Modifier.padding(top = 16.dp),
                    dataList = MotikocSingleton.personalProperties,
                    taggedWords = personalProperties,
                    onAdd = onPersonalPropertyAdded,
                    onRemove = onPersonalPropertyRemoved,
                    focusRequester = focusRequester
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledButton(
                text = "Geri",
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = {
                    navigateToSkills()
                }
            )
            FilledButton(
                isEnabled = personalProperties.size >= 3,
                text = "Devam",
                modifier = Modifier,
                onClick = {
                    navigateToArea()
                }
            )
        }
    }
}

@Composable
fun JobWizardArea(
    navigateToIdentify: () -> Unit,
    navigateToPersonal: () -> Unit,
    areas: List<String>,
    onAreaAdded: (String) -> Unit,
    onAreaRemoved: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val verticalScroll = rememberScrollState()
    var selectionSize by remember { mutableIntStateOf(0) }
    LaunchedEffect(selectionSize) {
        if (selectionSize > 0) {
            verticalScroll.scrollTo(selectionSize)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .verticalScroll(verticalScroll)
        ) {
            MotikocHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                title = R.string.job_wizard,
                subtitle = R.string.job_wizard_area
            )
            Column(
                modifier = Modifier
                    .onSizeChanged { selectionSize = it.height }
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Hangi alana yönelmek istiyorsun?(Sayısal,Sözel,Dil,Eşit Ağırlık)",
                    color = TextColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                MultipleInputSelector(
                    modifier = Modifier.padding(top = 4.dp),
                    dataList = MotikocSingleton.areaList,
                    taggedWords = areas,
                    onAdd = onAreaAdded,
                    onRemove = onAreaRemoved,
                    focusRequester = focusRequester
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledButton(
                text = "Geri",
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = {
                    navigateToPersonal()
                    focusRequester.requestFocus()
                }
            )
            FilledButton(
                text = "Devam",
                modifier = Modifier,
                onClick = {
                    navigateToIdentify()
                },
                isEnabled = areas.isNotEmpty()
            )
        }
    }
}


@Composable
fun JobWizardIdentify(
    navigateToJobSelection: () -> Unit,
    navigateToArea: () -> Unit,
    identifyText: String,
    onIdentifyPromptChanged: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
                title = R.string.job_wizard,
                subtitle = R.string.job_wizard_identify
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Text(
                    text = "Biraz kendinden bahsedebilir misin?",
                    color = TextColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IdentifyTextField(
                    givenValue = identifyText,
                    onValueChange = onIdentifyPromptChanged,
                    placeHolder = "Biraz kendinden bahset",
                    focusRequester = focusRequester,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp)
                ) {
                    MotikocDefaultErrorField(errorMessage = "20 karakterden az olamaz")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledButton(
                text = "Geri",
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = navigateToArea
            )
            FilledButton(
                isEnabled = identifyText.length > 20,
                text = "Devam",
                modifier = Modifier,
                onClick = { navigateToJobSelection(); keyboardController?.hide() }
            )
        }
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun IdentifyFormPreviewDarkMode() {
    MotikocTheme {
        JobWizard(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToJobSelection = { _, _, _, _, _ -> }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IdentifyFormPreviewLightMode() {
    MotikocTheme {
        JobWizard(
            uiState = UiState(),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToJobSelection = { _, _, _, _, _ -> }
        )
    }
}
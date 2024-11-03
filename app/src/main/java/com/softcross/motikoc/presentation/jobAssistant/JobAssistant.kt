package com.softcross.motikoc.presentation.jobAssistant

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.common.extensions.emailRegex
import com.softcross.motikoc.common.extensions.shimmerBackground
import com.softcross.motikoc.domain.model.ChatItem
import com.softcross.motikoc.presentation.components.MotikocAsyncImage
import com.softcross.motikoc.presentation.components.TextFieldWithoutError
import com.softcross.motikoc.presentation.jobAssistant.JobAssistantContract.UiAction
import com.softcross.motikoc.presentation.jobAssistant.JobAssistantContract.UiEffect
import com.softcross.motikoc.presentation.jobAssistant.JobAssistantContract.UiState
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun JobAssistant(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToJobSelection: () -> Unit,
    navigateToGoals: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    UiEffect.NavigateToGoals -> {
                        navigateToGoals()
                    }
                }
            }
        }
    }

    JobAssistantContent(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        chatList = uiState.chatList,
        selectedJob = uiState.selectedJob ?: uiState.jobList.first(),
        jobRecommendList = uiState.jobList,
        prompt = uiState.prompt,
        onJobSelected = { onAction(UiAction.OnJobSelectionChanged(it)) },
        onPromptChanged = { onAction(UiAction.OnPromptChanged(it)) },
        onSendMessage = { onAction(UiAction.SendMessage) },
        navigateToJobSelection = navigateToJobSelection,
        onDoneClick = { onAction(UiAction.OnDoneClicked) }
    )
}

@Composable
fun JobAssistantContent(
    isLoading: Boolean,
    isError: Boolean,
    prompt: String,
    selectedJob: AssistantJobItem,
    chatList: List<ChatItem>,
    jobRecommendList: List<AssistantJobItem>,
    onJobSelected: (AssistantJobItem) -> Unit,
    onPromptChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    navigateToJobSelection: () -> Unit,
    onDoneClick: () -> Unit
) {
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            IconButton(
                onClick = { navigateToJobSelection() },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(PrimarySurface, CircleShape)
                    .align(Alignment.CenterStart)
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.icon_back),
                    tint = TextColor,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Kariyer Asistanı",
                color = TextColor,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { onDoneClick() },
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(PrimarySurface, CircleShape)
                    .align(Alignment.CenterEnd)
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.icon_done),
                    tint = TextColor,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        val state = rememberLazyListState()
        LaunchedEffect(chatList.size, keyboardHeight) {
            state.scrollToItem(chatList.size)
        }
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .align(Alignment.Start),
        ) {
            items(chatList.size) {
                val currentItem = chatList[it]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (!currentItem.isFromUser) {
                        Icon(
                            painter = painterResource(id = R.drawable.ai_question),
                            contentDescription = "",
                            tint = TextColor,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PrimarySurface, CircleShape)
                                .padding(8.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .background(PrimarySurface, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = currentItem.message,
                            color = TextColor
                        )
                    }
                    if (currentItem.isFromUser) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_user),
                            contentDescription = "",
                            tint = TextColor,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PrimarySurface, CircleShape)
                                .padding(8.dp)
                        )
                    }
                }
            }
            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ai_question),
                            contentDescription = "",
                            tint = TextColor,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PrimarySurface, CircleShape)
                                .padding(8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .weight(1f)
                                .background(PrimarySurface, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "",
                                color = TextColor,
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(20.dp)
                                    .shimmerBackground()
                            )
                            Text(
                                text = "",
                                color = TextColor,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(200.dp)
                                    .height(20.dp)
                                    .shimmerBackground()
                            )
                            Text(
                                text = "",
                                color = TextColor,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(120.dp)
                                    .height(20.dp)
                                    .shimmerBackground()
                            )
                        }
                    }
                }
            }
            if (isError) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ai_question),
                            contentDescription = "",
                            tint = TextColor,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PrimarySurface, CircleShape)
                                .padding(8.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .weight(1f)
                                .background(PrimarySurface, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "Bir hata oluştu lütfen tekrar deneyin.",
                                color = TextColor
                            )
                            Text(
                                text = "Tekrar denemek için tıklayınız.",
                                color = GoldColor,
                                modifier = Modifier.clickable { onSendMessage() })
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .height(200.dp)
                .navigationBarsPadding()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .align(Alignment.End),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(jobRecommendList.size) {
                    val currentJob = jobRecommendList[it]
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(PrimarySurface, CircleShape)
                            .clip(CircleShape)
                            .clickable { onJobSelected(currentJob) }
                            .then(
                                if (selectedJob == currentJob) Modifier.border(
                                    2.dp,
                                    GoldColor,
                                    CircleShape
                                )
                                else Modifier
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MotikocAsyncImage(
                            model = currentJob.image,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = currentJob.name,
                            color = TextColor,
                            fontSize = 16.sp,
                            fontFamily = PoppinsLight,
                            modifier = Modifier.padding(end = 8.dp, start = 4.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextFieldWithoutError(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    givenValue = prompt,
                    keyboardType = KeyboardType.Text,
                    placeHolder = "Sorunuzu giriniz",
                    onValueChange = onPromptChanged,
                    regex = String::emailRegex,
                )
                IconButton(
                    onClick = { onSendMessage(); keyboardController?.hide() },
                    enabled = !isLoading,
                    colors = IconButtonDefaults.iconButtonColors(
                        disabledContainerColor = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray,
                        containerColor = PrimarySurface
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.ai_question),
                        tint = TextColor,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JobAssistantPreviewLight() {
    MotikocTheme {
        JobAssistant(
            uiState = UiState(
                chatList = listOf(
                    ChatItem(
                        "Merhaba! Size nasıl yardımcı olabilirim?",
                        false
                    )
                ),
                jobList = listOf(
                    AssistantJobItem(
                        name = "Software Developer",
                        image = "https://www.softcross.com.tr/wp-content/uploads/2021/09/softcross-logo.png"
                    )
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToJobSelection = {},
            navigateToGoals = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun JobAssistantPreviewDark() {
    MotikocTheme {
        JobAssistant(
            uiState = UiState(
                chatList = listOf(
                    ChatItem(
                        "Merhaba! Size nasıl yardımcı olabilirim?",
                        false
                    )
                ),
                jobList = listOf(
                    AssistantJobItem(
                        name = "Software Developer",
                        image = "https://www.softcross.com.tr/wp-content/uploads/2021/09/softcross-logo.png"
                    )
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToJobSelection = {},
            navigateToGoals = {}
        )
    }
}
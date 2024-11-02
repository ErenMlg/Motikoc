package com.softcross.motikoc.presentation.planner

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.common.extensions.getDayName
import com.softcross.motikoc.common.extensions.stringToLocalDate
import com.softcross.motikoc.common.extensions.toLessonImage
import com.softcross.motikoc.common.extensions.toMonthString
import com.softcross.motikoc.domain.model.PlannerItem
import com.softcross.motikoc.presentation.components.MotikocProgramAddDialog
import com.softcross.motikoc.presentation.components.MotikocSnackbar
import com.softcross.motikoc.presentation.components.SnackbarType
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerAction
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerEffect
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerState
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.DarkBackground
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.PoppinsMedium
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDate

@Composable
fun Planner(
    uiState: PlannerState,
    uiEffect: Flow<PlannerEffect>,
    onAction: (PlannerAction) -> Unit
) {
    var message by remember { mutableStateOf("") }
    var messageType by remember { mutableStateOf(SnackbarType.ERROR) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is PlannerEffect.ShowError -> {
                        message = effect.message
                        messageType = SnackbarType.ERROR
                    }

                    is PlannerEffect.ShowSuccess -> {
                        message = effect.message
                        messageType = SnackbarType.SUCCESS
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
        PlannerScreen(
            isLoading = uiState.isLoading,
            selectedDay = uiState.selectedDay,
            maxDayOfMonth = uiState.maxDayOfMonth,
            selectedMonth = uiState.selectedMonth,
            selectedYear = uiState.selectedYear,
            onDaySelected = { onAction(PlannerAction.OnDaySelected(it)) },
            onNextClick = { onAction(PlannerAction.OnNextMonthClicked) },
            onPreviousClick = { onAction(PlannerAction.OnPreviousMonthClicked) },
            onAddNewPlan = { onAction(PlannerAction.OnAddNewPlan(it)) },
            selectedDayPlans = uiState.plannerItems
        )
        if (message.isNotEmpty()) {
            MotikocSnackbar(
                message = message,
                type = messageType,
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                clear = { message = "" }
            )
        }
    }
}

@Composable
fun PlannerScreen(
    isLoading: Boolean,
    selectedDay: LocalDate,
    maxDayOfMonth: Int,
    selectedMonth: Int,
    selectedYear: Int,
    onDaySelected: (LocalDate) -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onAddNewPlan: (PlannerItem) -> Unit,
    selectedDayPlans: List<PlannerItem>,
) {
    var showDialog by remember { mutableStateOf(false) }
    val columCount = if (maxDayOfMonth > 30) {
        7
    } else {
        6
    }
    if (showDialog) {
        MotikocProgramAddDialog(
            onAddButtonClicked = onAddNewPlan,
            onDialogDismiss = { showDialog = false }
        )
    }
    Column(
        Modifier
            .background(BackgroundColor)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 16.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AnimatedContent(targetState = selectedMonth, label = "") { month ->
                Text(
                    text = "${month.toMonthString()}, $selectedYear",
                    fontFamily = PoppinsMedium,
                    color = TextColor,
                    fontSize = 24.sp
                )
            }
            Row {
                IconButton(
                    onClick = onPreviousClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PrimarySurface,
                    ),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_left),
                        contentDescription = "",
                        tint = TextColor
                    )
                }
                IconButton(
                    onClick = onNextClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PrimarySurface,
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_right_arrow),
                        contentDescription = "",
                        tint = TextColor
                    )
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(columCount),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(maxDayOfMonth) {
                val currentDate =
                    ("${selectedYear}-"
                            + "${if (selectedMonth < 10) "0${selectedMonth}" else "${selectedMonth}"}-"
                            + if (it + 1 < 10) "0${it + 1}" else "${it + 1}").stringToLocalDate()
                val getDayName = getDayName(selectedMonth, currentDate.dayOfMonth, selectedYear)
                Column {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(GoldColor)
                            .then(
                                if (currentDate == selectedDay) {
                                    Modifier
                                        .background(PrimarySurface)
                                        .border(2.dp, GoldColor, RoundedCornerShape(16.dp))
                                } else {
                                    Modifier.background(GoldColor)
                                }
                            )
                            .clickable {
                                onDaySelected(currentDate)
                            }
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val calendarTextColor = if (currentDate == selectedDay) {
                                TextColor
                            } else {
                                DarkBackground
                            }
                            Text(
                                text = getDayName,
                                color = calendarTextColor,
                                fontFamily = PoppinsMedium
                            )
                            Text(
                                text = (currentDate.dayOfMonth).toString(),
                                color = calendarTextColor,
                                fontFamily = PoppinsMedium,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = "Yapılacaklar",
            fontFamily = PoppinsMedium,
            color = TextColor,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 24.dp, top = 8.dp)
        )
        if (selectedDayPlans.isEmpty() && !isLoading) {
            EmptyPlanner(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f)
            )
        } else if (isLoading) {
            LoadingPlanner(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                items(selectedDayPlans.size) {
                    PlannerItemCart(plannerItem = selectedDayPlans[it])
                }
            }
        }


        IconButton(
            onClick = { showDialog = true },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = GoldColor,
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "",
                tint = DarkBackground
            )
        }
    }
}


@Composable
fun PlannerItemCart(plannerItem: PlannerItem) {
    val detail =
        if (plannerItem.questionCount.isNotEmpty()) plannerItem.questionCount else if (plannerItem.examType.isNotEmpty()) plannerItem.examType else plannerItem.workTime
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PrimarySurface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = plannerItem.lessonName.toLessonImage()),
            contentDescription = "",
            tint = GoldColor,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${plannerItem.workType} - $detail",
                color = TextColor,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = plannerItem.topicName,
                color = TextColor,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .border(2.dp, GoldColor, CircleShape)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_done),
                contentDescription = "",
                tint = PrimarySurface,
                modifier = Modifier.padding(3.dp)
            )
        }
    }
}

@Composable
fun EmptyPlanner(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_sad),
            tint = TextColor,
            contentDescription = ""
        )
        Text(text = "Yapılacak bir şey yok", color = TextColor, fontFamily = PoppinsMedium)
    }
}

@Composable
fun LoadingPlanner(modifier: Modifier){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_loading),
            tint = TextColor,
            contentDescription = ""
        )
        Text(text = "Yükleniyor...", color = TextColor, fontFamily = PoppinsMedium)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PlannerPreviewLight() {
    MotikocTheme {
        Planner(
            uiState = PlannerState(
                maxDayOfMonth = 30,
                isLoading = true
            ),
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlannerPreviewDark() {
    MotikocTheme {
        Planner(
            uiState = PlannerState(
                maxDayOfMonth = 30,
                isLoading = true
            ),
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}



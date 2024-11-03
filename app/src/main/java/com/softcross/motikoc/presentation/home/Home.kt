package com.softcross.motikoc.presentation.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.extensions.calculateLevel
import com.softcross.motikoc.common.extensions.getDayName
import com.softcross.motikoc.common.extensions.toLessonImage
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.MotikocUser
import com.softcross.motikoc.domain.model.PlannerItem
import com.softcross.motikoc.presentation.components.FilledButton
import com.softcross.motikoc.presentation.components.MotikocLottieAnimation
import com.softcross.motikoc.presentation.home.HomeContract.UiAction
import com.softcross.motikoc.presentation.home.HomeContract.UiEffect
import com.softcross.motikoc.presentation.home.HomeContract.UiState
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.DarkBackground
import com.softcross.motikoc.presentation.theme.DarkSurface
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.Poppins
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.PoppinsMedium
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun Home(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToIntroduce: () -> Unit,
    navigateToPlans: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    UiEffect.NavigateToIntroduce -> {
                        navigateToIntroduce()
                    }
                }
            }
        }
    }


    if (uiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MotikocLottieAnimation(
                rawRes = R.raw.loading_lesson,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(DarkSurface.copy(0.5f)),
            )
            Text(
                text = "Verileriniz yükleniyor...",
                fontFamily = PoppinsLight,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = TextColor,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    } else if (uiState.errorMessage.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MotikocLottieAnimation(
                rawRes = R.raw.loading_lesson,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(DarkSurface.copy(0.5f)),
            )
            Text(
                text = "Bir hata oluştu, lütfen tekrar deneyin...\nHata: ${uiState.errorMessage}",
                fontFamily = PoppinsLight,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = TextColor,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(top = 16.dp)
            )
            FilledButton(
                text = "Tekrar dene",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp),
                onClick = { onAction(UiAction.TryAgain) }
            )
        }
    } else {
        val user = MotikocSingleton.getUser()
        Log.e("Home","Assignment : ${user?.assignmentHistory} \nSchedule : ${user?.schedule} \nMessage : ${user?.motivationMessage}")
        HomeContent(
            uiState = uiState,
            onFinishAssignment = { assignment ->
                onAction(UiAction.FinishAssignment(assignment))
            },
            onDaySelect = { day ->
                onAction(UiAction.DaySelected(day))
            },
            onExitClick = { onAction(UiAction.OnExitClicked) },
            navigateToPlans = navigateToPlans,
            dayList = uiState.days,
            selectedDay = uiState.selectedDay,
            plannerLoading = uiState.plannerLoading,
            plannerItems = uiState.plannerItems
        )
    }
}

@Composable
private fun HomeContent(
    uiState: UiState,
    onFinishAssignment: (Assignment) -> Unit,
    onDaySelect: (LocalDate) -> Unit,
    onExitClick: () -> Unit,
    navigateToPlans: () -> Unit,
    dayList: List<LocalDate>,
    selectedDay: LocalDate,
    plannerLoading: Boolean,
    plannerItems: List<PlannerItem>
) {
    val localConfiguration = LocalConfiguration.current
    val tabList = listOf("Günlük Görevlerim", "Hedeflerim", "Rozetlerim")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabList.size })

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeHeader(
            onExitClick = onExitClick
        )
        HomeLevelContent(uiState.totalUserXP)
        ScrollableTabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp,
            containerColor = BackgroundColor,
            contentColor = TextColor,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(
                        tabPositions[selectedTabIndex]
                    ),
                    height = 2.dp,
                    color = GoldColor,
                )
            },
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    text = {
                        Text(
                            text = text,
                            color = TextColor,
                            fontFamily = Poppins
                        )
                    },
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height((localConfiguration.screenHeightDp * 0.18f).dp)
        ) { page ->
            when (page) {
                0 -> HomeDailyAssignments(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .height((localConfiguration.screenHeightDp * 0.16f).dp),
                    assignments = uiState.assignments,
                    onFinishAssignment = { assignment ->
                        onFinishAssignment(assignment)
                    },
                )

                1 -> HomeGoals()

                2 -> HomeBadges()
            }
        }
        Text(
            text = "Motivasyon Mesajı",
            color = TextColor,
            fontFamily = PoppinsMedium,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
        )
        var lineCount by remember {
            mutableIntStateOf(3)
        }
        var overflow by remember {
            mutableStateOf(false)
        }
        Text(
            text = uiState.motivationMessage,
            color = TextColor,
            fontFamily = PoppinsLight,
            overflow = TextOverflow.Ellipsis,
            maxLines = lineCount,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .animateContentSize { initialValue, targetValue ->
                    if (initialValue.height < targetValue.height) {
                        expandVertically()
                    } else {
                        shrinkVertically()
                    }
                },
            onTextLayout = {
                overflow = it.hasVisualOverflow
            },
        )
        Text(
            text = if (overflow) "Read More" else "Read Less",
            color = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    lineCount = if (overflow) Int.MAX_VALUE else 3
                },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Programım",
                color = TextColor,
                fontFamily = PoppinsMedium,
                fontSize = 18.sp,
                modifier = Modifier
            )
            IconButton(
                onClick = { navigateToPlans() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = PrimarySurface,
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_right_arrow),
                    contentDescription = "",
                    tint = TextColor
                )
            }
        }
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(dayList.size, key = { dayList[it] }) {
                val itDay = dayList[it].dayOfMonth
                Column {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(GoldColor)
                            .clickable { onDaySelect(dayList[it]) }
                            .then(
                                if (selectedDay == dayList[it]) {
                                    Modifier
                                        .background(PrimarySurface)
                                        .border(2.dp, GoldColor, RoundedCornerShape(16.dp))
                                } else {
                                    Modifier.background(GoldColor)
                                }
                            )
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val calendarTextColor = if (selectedDay == dayList[it]) {
                                TextColor
                            } else {
                                DarkBackground
                            }
                            Text(
                                text = getDayName(
                                    dayList[it].monthValue,
                                    dayList[it].dayOfMonth,
                                    dayList[it].year
                                ),
                                color = calendarTextColor,
                                fontFamily = PoppinsMedium
                            )
                            Text(
                                text = itDay.toString(),
                                color = calendarTextColor,
                                fontFamily = PoppinsMedium,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
        HomeSelectedDayProgram(
            modifier = Modifier.height((localConfiguration.screenHeightDp * 0.31f).dp),
            plannerItems = plannerItems,
            plannerLoading = plannerLoading
        )
    }
}

@Composable
fun HomeHeader(
    onExitClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.7f)) {
            Text(
                text = "Hoşgeldin",
                color = TextColor,
                fontSize = 18.sp,
            )
            Text(
                text = MotikocSingleton.getUserName(),
                color = TextColor,
                fontSize = 18.sp,
                fontFamily = PoppinsMedium
            )
        }
        IconButton(
            onClick = { onExitClick() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = PrimarySurface,
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_exit),
                contentDescription = "",
                tint = TextColor
            )
        }
    }

}

@Composable
fun HomeLevelContent(
    totalXP: Int
) {
    val userLevelInfo = calculateLevel(totalXP)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Seviye ${userLevelInfo.level}",
                color = TextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = userLevelInfo.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = TextColor,
                fontFamily = PoppinsMedium
            )
        }
        LinearProgressIndicator(
            progress = { userLevelInfo.progressXP.toFloat() / userLevelInfo.requiredXP.toFloat() },
            color = GoldColor,
            trackColor = PrimarySurface,
            drawStopIndicator = {},
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(3f)
        )
        Text(
            text = "${userLevelInfo.progressXP} / ${userLevelInfo.requiredXP} XP",
            color = TextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
fun HomeDailyAssignments(
    modifier: Modifier,
    assignments: List<Assignment>,
    onFinishAssignment: (Assignment) -> Unit
) {
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.Center
    ) {
        items(assignments.size, key = { assignments[it].assignmentID }) { index ->
            val currentAssignment = assignments[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentAssignment.assignmentName,
                    color = TextColor,
                    maxLines = 1,
                    textDecoration = if (currentAssignment.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
                Text(
                    text = "${currentAssignment.assignmentXP} XP",
                    color = TextColor,
                    maxLines = 1,
                    textDecoration = if (currentAssignment.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                ) {
                    IconButton(
                        onClick = {
                                onFinishAssignment(currentAssignment)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .then(
                                if (currentAssignment.isCompleted) {
                                    Modifier.background(GoldColor)
                                } else {
                                    Modifier.border(2.dp, GoldColor, CircleShape)
                                }
                            )
                            .align(Alignment.Center)
                            .size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_done),
                            contentDescription = "",
                            tint = BackgroundColor,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeGoals() {
    val user = MotikocSingleton.getUser() ?: MotikocUser()
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_job),
                contentDescription = "",
                tint = GoldColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = user.dreamJob,
                color = TextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_education),
                contentDescription = "",
                tint = GoldColor,
                modifier = Modifier.size(20.dp)
            )
            if (user.dreamUniversity.isEmpty()) {
                Text(
                    text = "Hedef Belirle",
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            } else {
                Text(
                    text = user.dreamUniversity,
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_departmant),
                contentDescription = "",
                tint = GoldColor,
                modifier = Modifier.size(20.dp)
            )
            if (user.dreamDepartment.isEmpty()) {
                Text(
                    text = "Hedef Belirle",
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            } else {
                Text(
                    text = user.dreamDepartment,
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_point),
                contentDescription = "",
                tint = GoldColor,
                modifier = Modifier.size(20.dp)
            )
            if (user.dreamPoint == 0 || user.dreamRank.isEmpty()) {
                Text(
                    text = "Hedef Belirle",
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            } else {
                Text(
                    text = "${user.dreamPoint} Puan / ${user.dreamRank} Sıralama",
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HomeBadges() {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(3) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(GoldColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_point),
                        contentDescription = "",
                        tint = BackgroundColor,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
                Text(
                    text = "Rozet Adı",
                    color = TextColor,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HomeSelectedDayProgram(
    modifier: Modifier,
    plannerItems: List<PlannerItem>,
    plannerLoading: Boolean
) {
    if (plannerLoading) {
        Column(
            modifier = modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_loading),
                tint = TextColor,
                contentDescription = ""
            )
            Text(text = "Yükleniyor...", color = TextColor, fontFamily = PoppinsMedium)
        }
    } else if (plannerItems.isEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_sad),
                tint = TextColor,
                contentDescription = ""
            )
            Text(text = "Yapılacak bir şey yok", color = TextColor, fontFamily = PoppinsMedium)
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            items(plannerItems.size, key = { plannerItems[it].id }) { index ->
                val plannerItem = plannerItems[index]
                val detail =
                    if (plannerItem.questionCount.isNotEmpty()) plannerItem.questionCount + " Soru" else if (plannerItem.examType.isNotEmpty()) plannerItem.examType else plannerItem.workTime
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
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreviewDark() {
    MotikocTheme {
        Home(
            uiState = UiState(
                isLoading = false,
                assignments = listOf(
                    Assignment(
                        assignmentID = "1",
                        assignmentName = "Matematik Çalış",
                        assignmentXP = 100,
                        isCompleted = false,
                        dueDate = LocalDateTime.now(),
                        assignmentDetail = ""
                    ),
                    Assignment(
                        assignmentID = "2",
                        assignmentName = "Matematik Çalış",
                        assignmentXP = 100,
                        isCompleted = false,
                        dueDate = LocalDateTime.now(),
                        assignmentDetail = ""
                    ),
                    Assignment(
                        assignmentID = "3",
                        assignmentName = "Matematik Çalış",
                        assignmentXP = 100,
                        isCompleted = false,
                        dueDate = LocalDateTime.now(),
                        assignmentDetail = ""
                    ),
                    Assignment(
                        assignmentID = "4",
                        assignmentName = "Matematik Çalış",
                        assignmentXP = 100,
                        isCompleted = false,
                        dueDate = LocalDateTime.now(),
                        assignmentDetail = ""
                    ),
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToIntroduce = {},
            navigateToPlans = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomePreviewLight() {
    MotikocTheme {
        Home(
            uiState = UiState(
                isLoading = false,
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToIntroduce = {},
            navigateToPlans = {}
        )
    }
}

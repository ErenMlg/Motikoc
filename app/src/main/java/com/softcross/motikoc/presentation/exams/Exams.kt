package com.softcross.motikoc.presentation.exams

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.R
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.extensions.stringToLocalDate
import com.softcross.motikoc.common.extensions.toTurkishDateString
import com.softcross.motikoc.domain.model.AIExamAnalyzeResult
import com.softcross.motikoc.domain.model.ExamItem
import com.softcross.motikoc.domain.model.FutureGoal
import com.softcross.motikoc.domain.model.GeneralAnalysis
import com.softcross.motikoc.domain.model.LessonAnalysis
import com.softcross.motikoc.domain.model.LessonItem
import com.softcross.motikoc.domain.model.TimeManagement
import com.softcross.motikoc.presentation.components.BarChart
import com.softcross.motikoc.presentation.components.CustomDateTimePicker
import com.softcross.motikoc.presentation.components.CustomSelectionDialog
import com.softcross.motikoc.presentation.components.ExamLessonResultDialog
import com.softcross.motikoc.presentation.components.ExamType
import com.softcross.motikoc.presentation.components.FilledButton
import com.softcross.motikoc.presentation.components.IconTextField
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.PoppinsBold
import com.softcross.motikoc.presentation.theme.PoppinsMedium
import com.softcross.motikoc.presentation.theme.PrimaryGreen
import com.softcross.motikoc.presentation.theme.PrimaryRed
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor

import com.softcross.motikoc.presentation.exams.ExamsContract.ExamState
import com.softcross.motikoc.presentation.exams.ExamsContract.ExamEvent
import com.softcross.motikoc.presentation.exams.ExamsContract.ExamEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDate
import java.util.Calendar

@Composable
fun Exams(
    uiState: ExamState,
    uiEffect: Flow<ExamEffect>,
    onEvent: (ExamEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        ExamsContent(
            isPreviousLoading = uiState.isLoading,
            isEnable = uiState.isEnable,
            examName = uiState.examName,
            examDate = uiState.examDate,
            onExamNameChanged = { onEvent(ExamEvent.OnExamNameChanged(it)) },
            onExamDateChanged = { onEvent(ExamEvent.OnExamDateChanged(it)) },
            onTurkishLessonItemChanged = { onEvent(ExamEvent.OnTurkishLessonItemChanged(it)) },
            onHistoryLessonItemChanged = { onEvent(ExamEvent.OnHistoryLessonItemChanged(it)) },
            onGeographyLessonItemChanged = { onEvent(ExamEvent.OnGeographyLessonItemChanged(it)) },
            onPhilosophyLessonItemChanged = { onEvent(ExamEvent.OnPhilosophyLessonItemChanged(it)) },
            onReligionLessonItemChanged = { onEvent(ExamEvent.OnReligionLessonItemChanged(it)) },
            onMathLessonItemChanged = { onEvent(ExamEvent.OnMathLessonItemChanged(it)) },
            onGeometryLessonItemChanged = { onEvent(ExamEvent.OnGeometryLessonItemChanged(it)) },
            onPhysicsLessonItemChanged = { onEvent(ExamEvent.OnPhysicsLessonItemChanged(it)) },
            onChemistryLessonItemChanged = { onEvent(ExamEvent.OnChemistryLessonItemChanged(it)) },
            onBiologyLessonItemChanged = { onEvent(ExamEvent.OnBiologyLessonItemChanged(it)) },
            onSaveExam = { onEvent(ExamEvent.OnSaveExamClicked) },
            onLoadExams = { onEvent(ExamEvent.OnLoadExams) },
            examList = uiState.exams.sortedByDescending { it.examDate },
            onAIAnalyzeRequested = { onEvent(ExamEvent.OnAIResponseClicked) },
            aiResponse = uiState.aiResponse
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsContent(
    isPreviousLoading: Boolean,
    isEnable: Boolean,
    examName: String,
    examDate: LocalDate?,
    onExamNameChanged: (String) -> Unit,
    onExamDateChanged: (LocalDate) -> Unit,
    onAIAnalyzeRequested: () -> Unit,
    onTurkishLessonItemChanged: (LessonItem) -> Unit,
    onHistoryLessonItemChanged: (LessonItem) -> Unit,
    onGeographyLessonItemChanged: (LessonItem) -> Unit,
    onPhilosophyLessonItemChanged: (LessonItem) -> Unit,
    onReligionLessonItemChanged: (LessonItem) -> Unit,
    onMathLessonItemChanged: (LessonItem) -> Unit,
    onGeometryLessonItemChanged: (LessonItem) -> Unit,
    onPhysicsLessonItemChanged: (LessonItem) -> Unit,
    onChemistryLessonItemChanged: (LessonItem) -> Unit,
    onBiologyLessonItemChanged: (LessonItem) -> Unit,
    onLoadExams: () -> Unit,
    onSaveExam: () -> Unit,
    examList: List<ExamItem>,
    aiResponse: AIExamAnalyzeResult?
) {
    var showPreviousExams by remember { mutableStateOf(false) }
    var showExamTable by remember { mutableStateOf(false) }
    var showSaveExam by remember { mutableStateOf(false) }
    var showAIAnalyze by remember { mutableStateOf(false) }
    var heightOfSection by remember { mutableIntStateOf(0) }
    val localConfiguration = LocalConfiguration.current
    val verticalScroll = rememberScrollState()

    LaunchedEffect(heightOfSection) {
        println("heightOfSection $heightOfSection")
        if (heightOfSection > 0) {
            verticalScroll.animateScrollTo(heightOfSection)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Denemelerim",
            color = TextColor,
            fontFamily = PoppinsMedium,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarySurface)
                .padding(16.dp)
                .clickable {
                    if (examList.isNotEmpty()) {
                        onAIAnalyzeRequested()
                    }
                    heightOfSection = 0
                    showAIAnalyze = !showAIAnalyze
                    showSaveExam = false
                    showExamTable = false
                    showPreviousExams = false
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "AI Deneme Analizi",
                color = TextColor,
                fontFamily = PoppinsMedium,
            )
            val rotation by animateFloatAsState(
                targetValue = if (showAIAnalyze) 180f else 0f,
                label = ""
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_down_arrow),
                contentDescription = "",
                tint = TextColor,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )
        }
        AnimatedVisibility(visible = showAIAnalyze) {
            if (isPreviousLoading) {
                Column(
                    modifier = Modifier
                        .onSizeChanged { heightOfSection = it.height }
                        .fillMaxWidth()
                        .height((localConfiguration.screenHeightDp * 0.4).dp),
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
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "AI Deneme Analizi",
                        color = TextColor,
                        fontFamily = PoppinsMedium,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    if (examList.isEmpty()) {
                        Text(
                            text = "Daha önceden bir sınavınız yok",
                            color = TextColor,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else {
                        aiResponse?.let {
                            Text(
                                text = "Genel Analiz",
                                color = TextColor,
                                fontFamily = PoppinsMedium,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "Güçlü Yönler",
                                color = TextColor,
                                fontSize = 16.sp,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = aiResponse.generalAnalysis.strongPoints.joinToString(","),
                                color = TextColor,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "Gelişim Alanları",
                                color = TextColor,
                                fontFamily = PoppinsMedium,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = aiResponse.generalAnalysis.improvementAreas.joinToString(","),
                                color = TextColor,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = "Sıralama Tahmini",
                                color = TextColor,
                                fontFamily = PoppinsMedium,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = aiResponse.generalAnalysis.rankingEstimation,
                                color = TextColor,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = "Ders Analizi",
                                color = TextColor,
                                fontSize = 18.sp,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .height(localConfiguration.screenHeightDp.dp * 0.3f)
                                    .padding(horizontal = 16.dp),
                            ) {
                                items(aiResponse.lessonAnalysis.size) { index ->
                                    val lesson = aiResponse.lessonAnalysis[index]
                                    val rotation by animateFloatAsState(
                                        targetValue = if (showExamTable) 180f else 0f,
                                        label = ""
                                    )
                                    var showLesson by remember { mutableStateOf(false) }
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(PrimarySurface)
                                            .clickable { showLesson = !showLesson },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = lesson.lesson,
                                            color = TextColor,
                                            fontFamily = PoppinsMedium,
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 8.dp
                                            )
                                        )
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_down_arrow),
                                            contentDescription = "",
                                            tint = TextColor,
                                            modifier = Modifier
                                                .graphicsLayer {
                                                    rotationZ = rotation
                                                }
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                )
                                        )
                                    }
                                    AnimatedVisibility(visible = showLesson) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 8.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.icon_done),
                                                        contentDescription = "",
                                                        tint = PrimaryGreen,
                                                        modifier = Modifier
                                                            .padding(end = 4.dp)
                                                            .size(18.dp)
                                                    )
                                                    Text(
                                                        text = lesson.correct.toString(),
                                                        color = TextColor
                                                    )
                                                }
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.icon_close),
                                                        contentDescription = "",
                                                        tint = PrimaryRed,
                                                        modifier = Modifier
                                                            .padding(start = 8.dp, end = 4.dp)
                                                            .size(18.dp)
                                                    )
                                                    Text(
                                                        text = lesson.wrong.toString(),
                                                        color = TextColor
                                                    )
                                                }
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = "Net",
                                                        color = TextColor,
                                                        fontFamily = PoppinsMedium
                                                    )
                                                    Text(
                                                        text = lesson.net.toString(),
                                                        color = TextColor
                                                    )
                                                }
                                            }
                                            Text(
                                                text = "Öneriler",
                                                color = TextColor,
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsMedium,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                            Text(
                                                text = lesson.suggestions.joinToString(),
                                                color = TextColor,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                            Text(
                                                text = "Öncelikli Konular",
                                                color = TextColor,
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsMedium,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                            Text(
                                                text = lesson.priorityTopics.joinToString(),
                                                color = TextColor,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "Zaman Yönetimi",
                                color = TextColor,
                                fontSize = 18.sp,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "Problem Alanları",
                                color = TextColor,
                                fontSize = 16.sp,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = aiResponse.timeManagement.problemAreas.joinToString(","),
                                color = TextColor,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "Öneriler",
                                color = TextColor,
                                fontSize = 16.sp,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Text(
                                text = aiResponse.timeManagement.suggestions.joinToString(","),
                                color = TextColor,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "Gelecek Hedefler",
                                color = TextColor,
                                fontSize = 18.sp,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .height(localConfiguration.screenHeightDp.dp * 0.3f)
                                    .padding(horizontal = 16.dp),
                            ) {
                                items(aiResponse.futureGoals.size) { index ->
                                    val goal = aiResponse.futureGoals[index]
                                    Text(
                                        text = "Hedef : ${goal.goal}",
                                        color = TextColor,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Text(
                                        text = "Süre : ${goal.time}",
                                        color = TextColor,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Text(
                                        text = "Strateji : ${goal.strategy}",
                                        color = TextColor,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = GoldColor,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarySurface)
                .padding(16.dp)
                .clickable {
                    heightOfSection = 0
                    showExamTable = !showExamTable
                    showAIAnalyze = false
                    showSaveExam = false
                    showPreviousExams = false
                    onLoadExams()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Deneme Tablom",
                color = TextColor,
                fontFamily = PoppinsMedium,
            )
            val rotation by animateFloatAsState(
                targetValue = if (showExamTable) 180f else 0f,
                label = ""
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_down_arrow),
                contentDescription = "",
                tint = TextColor,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )
        }
        AnimatedVisibility(showExamTable) {
            if (isPreviousLoading) {
                Column(
                    modifier = Modifier
                        .onSizeChanged { heightOfSection = it.height }
                        .fillMaxWidth()
                        .height((localConfiguration.screenHeightDp * 0.4).dp),
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
            } else if (examList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .onSizeChanged { heightOfSection = it.height }
                        .fillMaxWidth()
                        .height((localConfiguration.screenHeightDp * 0.4).dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_sad),
                        tint = TextColor,
                        contentDescription = ""
                    )
                    Text(
                        text = "Daha önceden bir sınavınız yok",
                        color = TextColor,
                        fontFamily = PoppinsMedium
                    )
                }
            } else {
                Column(
                    modifier = Modifier.onSizeChanged { heightOfSection = it.height }
                ) {
                    Text(
                        text = "Tarihe göre sıralanmıştır.",
                        color = Color.Gray,
                        fontFamily = PoppinsMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                    BarChart(
                        height = (localConfiguration.screenHeightDp * 0.3).dp,
                        chartData = examList.map { it.examName to it.net }
                    )
                }
            }
        }
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarySurface)
                .padding(16.dp)
                .clickable {
                    heightOfSection = 0
                    showPreviousExams = !showPreviousExams;
                    onLoadExams()
                    showSaveExam = false
                    showExamTable = false
                    showAIAnalyze = false
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Önceki Denemelerim",
                color = TextColor,
                fontFamily = PoppinsMedium,
            )
            val rotation by animateFloatAsState(
                targetValue = if (showPreviousExams) 180f else 0f,
                label = ""
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_down_arrow),
                contentDescription = "",
                tint = TextColor,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )
        }
        AnimatedVisibility(visible = showPreviousExams) {
            if (isPreviousLoading) {
                Column(
                    modifier = Modifier
                        .onSizeChanged { heightOfSection = it.height }
                        .fillMaxWidth()
                        .height((localConfiguration.screenHeightDp * 0.5).dp),
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
            } else if (examList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .onSizeChanged { heightOfSection = it.height }
                        .fillMaxWidth()
                        .height((localConfiguration.screenHeightDp * 0.5).dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_sad),
                        tint = TextColor,
                        contentDescription = ""
                    )
                    Text(
                        text = "Daha önceden bir sınavınız yok",
                        color = TextColor,
                        fontFamily = PoppinsMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .height((localConfiguration.screenHeightDp * 0.5).dp)
                        .onSizeChanged { heightOfSection = it.height }
                        .padding(horizontal = 16.dp)
                ) {
                    items(examList.size, key = { index -> examList[index].examID }) { index ->
                        ExamItemView(examList[index])
                    }
                }
            }
        }
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarySurface)
                .padding(16.dp)
                .clickable {
                    heightOfSection = 0
                    showSaveExam = !showSaveExam
                    showExamTable = false
                    showPreviousExams = false
                    showAIAnalyze = false
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Yeni Deneme Kaydet",
                color = TextColor,
                fontFamily = PoppinsMedium,
            )
            val rotation by animateFloatAsState(
                targetValue = if (showSaveExam) 180f else 0f,
                label = ""
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_down_arrow),
                contentDescription = "",
                tint = TextColor,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )
        }
        AnimatedVisibility(visible = showSaveExam) {
            Column(
                modifier = Modifier.onSizeChanged { heightOfSection = it.height }
            ) {
                IconTextField(
                    givenValue = examName,
                    placeHolder = "Deneme Adı",
                    onValueChange = { onExamNameChanged(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {}
                CustomDateTimePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeHolder = "Tarih seçiniz",
                    onDateSelected = {
                        onExamDateChanged(it.stringToLocalDate("MM-dd-yyyy"))
                    },
                    selectedDate = examDate?.toString() ?: "",
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            return utcTimeMillis <= Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 23)
                                set(Calendar.MINUTE, 59)
                                set(Calendar.SECOND, 59)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                        }
                    }
                )
                var selectedExamType by remember { mutableStateOf("") }
                CustomSelectionDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    data = ExamType.entries.map { it.title },
                    placeHolder = "Deneme Türü Seçiniz",
                    onDataSelected = { selectedExamType = it },
                    title = "Deneme Türü Seçimi"
                )
                AnimatedVisibility(visible = selectedExamType.isNotEmpty()) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f),
                                "Türkçe",
                                "Türkçe ",
                                onSaveClick = onTurkishLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 40 else 24
                            )
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                                "Coğrafya",
                                "Coğrafya ",
                                onSaveClick = onGeographyLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 5 else 17
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f),
                                "Matematik",
                                "Matematik ",
                                onSaveClick = onMathLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 32 else 40
                            )
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                                "Geometri",
                                "Geometri ",
                                onSaveClick = onGeometryLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 9 else 10
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f),
                                "Tarih",
                                "Tarih",
                                onSaveClick = onHistoryLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 5 else 21
                            )
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(1f),
                                "Felsefe",
                                "Felsefe",
                                onSaveClick = onPhilosophyLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 5 else 12
                            )
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                                "Din",
                                "Din",
                                onSaveClick = onReligionLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 5 else 6
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f),
                                "Biyoloji",
                                "Biyoloji ",
                                onSaveClick = onBiologyLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 6 else 13
                            )
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(1f),
                                "Fizik",
                                "Fizik",
                                onSaveClick = onPhysicsLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 7 else 13
                            )
                            ExamLessonResultDialog(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                                "Kimya",
                                "Kimya",
                                onSaveClick = onChemistryLessonItemChanged,
                                maxQuestionCount = if (selectedExamType == ExamType.TYT.title) 7 else 14
                            )
                        }
                        FilledButton(
                            text = "Kaydet",
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                            isEnabled = isEnable,
                            onClick = {
                                onSaveExam()
                                showSaveExam = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExamItemView(exam: ExamItem) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PrimarySurface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = exam.examName, color = TextColor, fontFamily = PoppinsBold)
            Text(
                text = exam.examDate.toTurkishDateString(),
                color = TextColor,
                fontFamily = PoppinsBold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Soru Sayısı: ${exam.questionCount}", color = TextColor)
            Text(text = "Süre: ${exam.examTime}", color = TextColor)
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = GoldColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row {
            Column(
                modifier = Modifier.fillMaxWidth(0.3f),
            ) {
                Text(text = "Türkçe", color = TextColor)
                Text(text = "Tarih", color = TextColor)
                Text(text = "Coğrafya", color = TextColor)
                Text(text = "Din", color = TextColor)
                Text(text = "Felsefe", color = TextColor)
                Text(text = "Matematik", color = TextColor)
                Text(text = "Geometri", color = TextColor)
                Text(text = "Biyoloji", color = TextColor)
                Text(text = "Fizik", color = TextColor)
                Text(text = "Kimya", color = TextColor)
            }
            Column {
                ExamLessonItemView(exam.turkishLessonItem)
                ExamLessonItemView(exam.historyLessonItem)
                ExamLessonItemView(exam.geographyLessonItem)
                ExamLessonItemView(exam.religionLessonItem)
                ExamLessonItemView(exam.philosophyLessonItem)
                ExamLessonItemView(exam.mathLessonItem)
                ExamLessonItemView(exam.geometryLessonItem)
                ExamLessonItemView(exam.biologyLessonItem)
                ExamLessonItemView(exam.physicsLessonItem)
                ExamLessonItemView(exam.chemistryLessonItem)
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = GoldColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(text = "Net : ${exam.net}", color = TextColor, fontFamily = PoppinsBold)

    }
}

@Composable
fun ExamLessonItemView(lessonItem: LessonItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_done),
            contentDescription = "",
            tint = PrimaryGreen,
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp)
                .size(18.dp)
        )
        Text(text = lessonItem.lessonCorrectCount.toString(), color = TextColor)
        Icon(
            painter = painterResource(id = R.drawable.icon_close),
            contentDescription = "",
            tint = PrimaryRed,
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp)
                .size(18.dp)
        )
        Text(text = lessonItem.lessonWrongCount.toString(), color = TextColor)
        Icon(
            painter = painterResource(id = R.drawable.icon_empty),
            contentDescription = "",
            tint = Color.Black,
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp)
                .size(16.dp)
        )
        Text(text = lessonItem.lessonEmptyCount.toString(), color = TextColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExamsLight() {
    MotikocTheme {
        Exams(
            uiState = ExamState(
                aiResponse = AIExamAnalyzeResult(
                    generalAnalysis = GeneralAnalysis(
                        strongPoints = listOf("madde1", "madde2"),
                        improvementAreas = listOf("madde1", "madde2"),
                        rankingEstimation = "tahmin_detayı"
                    ),
                    lessonAnalysis = listOf(
                        LessonAnalysis(
                            lesson = "ders_adı",
                            correct = 1,
                            wrong = 2,
                            net = 3,
                            suggestions = listOf("öneri1", "öneri2"),
                            priorityTopics = listOf("konu1", "konu2")
                        ),
                        LessonAnalysis(
                            lesson = "ders_adı",
                            correct = 1,
                            wrong = 2,
                            net = 3,
                            suggestions = listOf("öneri1", "öneri2"),
                            priorityTopics = listOf("konu1", "konu2")
                        ),
                        LessonAnalysis(
                            lesson = "ders_adı",
                            correct = 1,
                            wrong = 2,
                            net = 3,
                            suggestions = listOf("öneri1", "öneri2"),
                            priorityTopics = listOf("konu1", "konu2")
                        )
                    ),
                    timeManagement = TimeManagement(
                        problemAreas = listOf("alan1", "alan2"),
                        suggestions = listOf("öneri1", "öneri2")
                    ),
                    futureGoals = listOf(
                        FutureGoal(
                            goal = "hedef1",
                            time = "süre1",
                            strategy = "strateji1"
                        )
                    )
                ),
                exams = listOf(
                    ExamItem(
                        examID = "ubique",
                        examName = "Matt McBride",
                        examDate = LocalDate.now(),
                        examTime = "sanctus",
                        questionCount = 3048,
                        turkishLessonItem = LessonItem(
                            lessonCorrectCount = 8385,
                            lessonWrongCount = 4969,
                            lessonEmptyCount = 4536
                        ),
                        historyLessonItem = LessonItem(
                            lessonCorrectCount = 7861,
                            lessonWrongCount = 1860,
                            lessonEmptyCount = 4837
                        ),
                        geographyLessonItem = LessonItem(
                            lessonCorrectCount = 7125,
                            lessonWrongCount = 8705,
                            lessonEmptyCount = 7700
                        ),
                        philosophyLessonItem = LessonItem(
                            lessonCorrectCount = 9986,
                            lessonWrongCount = 8970,
                            lessonEmptyCount = 5284
                        ),
                        religionLessonItem = LessonItem(
                            lessonCorrectCount = 2622,
                            lessonWrongCount = 8976,
                            lessonEmptyCount = 1236
                        ),
                        mathLessonItem = LessonItem(
                            lessonCorrectCount = 5702,
                            lessonWrongCount = 3134,
                            lessonEmptyCount = 4559
                        ),
                        geometryLessonItem = LessonItem(
                            lessonCorrectCount = 9876,
                            lessonWrongCount = 7572,
                            lessonEmptyCount = 9546
                        ),
                        physicsLessonItem = LessonItem(
                            lessonCorrectCount = 5243,
                            lessonWrongCount = 5750,
                            lessonEmptyCount = 2164
                        ),
                        chemistryLessonItem = LessonItem(
                            lessonCorrectCount = 8814,
                            lessonWrongCount = 4263,
                            lessonEmptyCount = 9597
                        ),
                        biologyLessonItem = LessonItem(
                            lessonCorrectCount = 7991,
                            lessonWrongCount = 1195,
                            lessonEmptyCount = 7026
                        ),
                        net = 2.3f

                    )
                )
            ),
            uiEffect = emptyFlow(),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExamsDark() {
    MotikocTheme(darkTheme = true) {
        Exams(
            uiState = ExamState(),
            uiEffect = emptyFlow(),
            onEvent = {}
        )
    }
}
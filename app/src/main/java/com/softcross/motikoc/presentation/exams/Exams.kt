package com.softcross.motikoc.presentation.exams

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.R
import com.softcross.motikoc.common.extensions.stringToLocalDate
import com.softcross.motikoc.domain.model.LessonItem
import com.softcross.motikoc.presentation.components.BarChart
import com.softcross.motikoc.presentation.components.CustomDateTimePicker
import com.softcross.motikoc.presentation.components.ExamLessonResultDialog
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
            isEnable = uiState.isEnable,
            examName = uiState.examName,
            examDate = uiState.examDate,
            questionCount = uiState.questionCount,
            duration = uiState.questionTime,
            onExamNameChanged = { onEvent(ExamEvent.OnExamNameChanged(it)) },
            onExamDateChanged = { onEvent(ExamEvent.OnExamDateChanged(it)) },
            onQuestionCountChanged = { onEvent(ExamEvent.OnQuestionCountChanged(it)) },
            onQuestionTimeChanged = { onEvent(ExamEvent.OnQuestionTimeChanged(it)) },
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
            onSaveExam = { onEvent(ExamEvent.OnSaveExamClicked) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsContent(
    isEnable: Boolean,
    examName: String,
    examDate: LocalDate?,
    questionCount: Int = 0,
    duration: String,
    onExamNameChanged: (String) -> Unit,
    onExamDateChanged: (LocalDate) -> Unit,
    onQuestionCountChanged: (Int) -> Unit,
    onQuestionTimeChanged: (String) -> Unit,
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
    onSaveExam: () -> Unit
) {
    var showPreviousExams by remember { mutableStateOf(false) }
    var showExamTable by remember { mutableStateOf(false) }
    val localConfiguration = LocalConfiguration.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                Log.e("FirebaseRepositoryImpl",it.stringToLocalDate("MM-dd-yyyy").toString())
            },
            selectedDate = examDate?.toString() ?: "",
            selectableDates = DatePickerDefaults.AllDates
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            IconTextField(
                givenValue = if (questionCount == 0) "" else questionCount.toString(),
                placeHolder = "Soru Sayısı",
                keyboardType = KeyboardType.Number,
                onValueChange = { onQuestionCountChanged(it.toIntOrNull() ?: 0) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            ) {}
            IconTextField(
                givenValue = duration,
                placeHolder = "Süre",
                keyboardType = KeyboardType.Number,
                onValueChange = { onQuestionTimeChanged(it) },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {}
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
                "Türkçe",
                "Türkçe ",
                onSaveClick = onTurkishLessonItemChanged
            )
            ExamLessonResultDialog(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                "Coğrafya",
                "Coğrafya ",
                onSaveClick = onGeographyLessonItemChanged
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
                onSaveClick = onMathLessonItemChanged
            )
            ExamLessonResultDialog(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                "Geometri",
                "Geometri ",
                onSaveClick = onGeometryLessonItemChanged
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
                onSaveClick = onHistoryLessonItemChanged
            )
            ExamLessonResultDialog(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                "Felsefe",
                "Felsefe",
                onSaveClick = onPhilosophyLessonItemChanged
            )
            ExamLessonResultDialog(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                "Din",
                "Din",
                onSaveClick = onReligionLessonItemChanged
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
                onSaveClick = onBiologyLessonItemChanged
            )
            ExamLessonResultDialog(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                "Fizik",
                "Fizik",
                onSaveClick = onPhysicsLessonItemChanged
            )
            ExamLessonResultDialog(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                "Kimya",
                "Kimya",
                onSaveClick = onChemistryLessonItemChanged
            )
        }
        FilledButton(
            text = "Kaydet",
            modifier = Modifier.padding(vertical = 8.dp),
            isEnabled = isEnable,
            onClick = onSaveExam
        )
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarySurface)
                .padding(16.dp)
                .clickable { showExamTable = !showExamTable },
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
            BarChart(
                modifier = Modifier
                    .padding(top = 16.dp),
                height = 300.dp
            )
        }
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarySurface)
                .padding(16.dp)
                .clickable { showPreviousExams = !showPreviousExams },
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
            LazyColumn(
                modifier = Modifier
                    .height((localConfiguration.screenHeightDp * 0.5).dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(10) {
                    ExamItemView()
                }
            }
        }
    }
}

@Composable
fun ExamItemView() {
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
            Text(text = "Limit AYT Denemesi", color = TextColor, fontFamily = PoppinsBold)
            Text(text = "16.08.2024", color = TextColor, fontFamily = PoppinsBold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Soru Sayısı: 180", color = TextColor)
            Text(text = "Süre: 180 dk", color = TextColor)
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
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
                    Text(text = "20", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "",
                        tint = PrimaryRed,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(18.dp)
                    )
                    Text(text = "10", color = TextColor)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_empty),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .size(16.dp)
                    )
                    Text(text = "7", color = TextColor)
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = GoldColor,
            modifier = Modifier.padding(top = 8.dp)
        )
        Column(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Net : 78.5", color = TextColor, fontFamily = PoppinsBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExamsLight() {
    MotikocTheme {
        Exams(
            uiState = ExamState(),
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
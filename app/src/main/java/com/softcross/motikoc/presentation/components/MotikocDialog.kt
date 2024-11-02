package com.softcross.motikoc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.softcross.motikoc.R
import com.softcross.motikoc.common.extensions.dateTimeToFormattedDate
import com.softcross.motikoc.common.extensions.toDate
import com.softcross.motikoc.domain.model.LessonItem
import com.softcross.motikoc.domain.model.PlannerItem
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.TextColor
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.abs

val lessonsHashMap = hashMapOf(
    "Matematik" to R.drawable.icon_math,
    "Geometri" to R.drawable.icon_geo,
    "Fizik" to R.drawable.icon_physic,
    "Kimya" to R.drawable.icon_chemical,
    "Biyoloji" to R.drawable.icon_biology,
    "Tarih" to R.drawable.icon_history,
    "Coğrafya" to R.drawable.icon_geography,
    "Felsefe" to R.drawable.icon_philosopy,
    "Din Kültürü" to R.drawable.icon_mosque,
    "Türkçe" to R.drawable.icon_turkish,
    "Yabancı Dil" to R.drawable.icon_foreign_language,
)

enum class WorkType(val title: String) {
    TEST("Test Çözümü"),
    LECTURE("Konu Çalışması"),
    READING("Okuma"),
    RESEARCH("Araştırma"),
    EXAM("Deneme Çözme");
}

enum class ExamType(val title: String) {
    TYT("TYT"),
    AYT("AYT"),
    DIL("DİL");
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotikocProgramAddDialog(
    onAddButtonClicked: (PlannerItem) -> Unit,
    onDialogDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedLesson by remember { mutableStateOf("") }
    var selectedTopic by remember { mutableStateOf("") }
    var selectedWorkType by remember { mutableStateOf("") }
    var selectedQuestionCount by remember { mutableStateOf("") }
    var selectedWorkTime by remember { mutableStateOf("") }
    var selectedExamType by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDialogDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Çalışma Ekleme",
                fontSize = 18.sp,
                color = TextColor,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            CustomDateTimePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                placeHolder = "Tarih seçiniz",
                onDateSelected = { selectedDate = it },
                selectedDate = selectedDate
            )
            ImagedCustomSelectionDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                data = lessonsHashMap,
                placeHolder = "Ders Seçiniz",
                onDataSelected = { selectedLesson = it },
                title = "Ders Seçimi",
                selected = selectedLesson
            )
            IconTextField(
                givenValue = selectedTopic,
                placeHolder = "Konu adı giriniz",
                onValueChange = { selectedTopic = it },
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                regex = String::isNotBlank
            ) {
                MotikocDefaultErrorField(errorMessage = "Konu adı boş olamaz")
            }
            CustomSelectionDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                data = WorkType.entries.map { it.title },
                placeHolder = "Çalışma Türü Seçiniz",
                onDataSelected = { selectedWorkType = it },
                title = "Çalışma Türü Seçimi",
                selected = selectedWorkType
            )
            AnimatedVisibility(visible = selectedWorkType == WorkType.TEST.title) {
                IconTextField(
                    givenValue = selectedQuestionCount,
                    placeHolder = "Soru sayısı giriniz",
                    onValueChange = { selectedQuestionCount = it },
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    regex = String::isNotBlank
                ) {
                    MotikocDefaultErrorField(errorMessage = "Soru sayısı boş olamaz")
                }
            }
            AnimatedVisibility(visible = selectedWorkType == WorkType.READING.title || selectedWorkType == WorkType.RESEARCH.title || selectedWorkType == WorkType.LECTURE.title) {
                IconTextField(
                    givenValue = selectedWorkTime,
                    placeHolder = "Çalışan süreyi giriniz",
                    onValueChange = { selectedWorkTime = it },
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    regex = String::isNotBlank
                ) {
                    MotikocDefaultErrorField(errorMessage = "Süre boş olamaz")
                }
            }
            AnimatedVisibility(visible = selectedWorkType == WorkType.EXAM.title) {
                CustomSelectionDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    data = ExamType.entries.map { it.title },
                    placeHolder = "Deneme Türü Seçiniz",
                    onDataSelected = { selectedExamType = it },
                    title = "Deneme Türü Seçimi"
                )
            }
            FilledButton(
                text = "Ekle",
                isEnabled = selectedDate.isNotBlank() &&
                        selectedLesson.isNotBlank() &&
                        selectedTopic.isNotBlank() &&
                        selectedWorkType.isNotBlank() &&
                        (selectedWorkType == WorkType.TEST.title && selectedQuestionCount.isNotBlank() || selectedWorkType == WorkType.LECTURE.title && selectedWorkTime.isNotBlank() || selectedWorkType == WorkType.READING.title && selectedWorkTime.isNotBlank() || selectedWorkType == WorkType.RESEARCH.title && selectedWorkTime.isNotBlank() || selectedWorkType == WorkType.EXAM.title && selectedWorkTime.isNotBlank()),
                modifier = Modifier
                    .padding(vertical = 8.dp),
                onClick = {
                    onAddButtonClicked(
                        PlannerItem(
                            lessonName = selectedLesson,
                            topicName = selectedTopic,
                            plannerDate = selectedDate,
                            workType = selectedWorkType,
                            questionCount = selectedQuestionCount,
                            workTime = selectedWorkTime,
                            examType = selectedExamType
                        )
                    )
                    onDialogDismiss()
                }
            )
        }
    }
}

@Composable
fun ExamLessonResultDialog(
    modifier: Modifier = Modifier,
    placeHolder: String,
    title: String,
    onSaveClick: (LessonItem) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var trueCount by remember { mutableStateOf("0") }
    var wrongCount by remember { mutableStateOf("0") }
    var emptyCount by remember { mutableStateOf("0") }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BackgroundColor)
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    color = TextColor,
                    fontSize = 18.sp
                )
                IconTextField(
                    givenValue = trueCount.toString(),
                    placeHolder = "Doğru Sayısı",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { trueCount = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_done),
                            tint = TextColor,
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {}
                IconTextField(
                    givenValue = wrongCount.toString(),
                    placeHolder = "Yanlış Sayısı",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { wrongCount = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_close),
                            tint = TextColor,
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {}
                IconTextField(
                    givenValue = emptyCount.toString(),
                    placeHolder = "Boş Sayısı",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { emptyCount = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_empty),
                            tint = TextColor,
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {}
                FilledButton(
                    text = "Kaydet",
                    isEnabled = trueCount.isNotEmpty() && wrongCount.isNotEmpty() && emptyCount.isNotEmpty(),
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        val lessonItem = LessonItem(
                            lessonCorrectCount = trueCount.toInt(),
                            lessonWrongCount = wrongCount.toInt(),
                            lessonEmptyCount = emptyCount.toInt()
                        )
                        onSaveClick(lessonItem)
                        showDialog = false
                    }
                )
            }
        }
    }
    IconTextField(
        givenValue = if (trueCount.isEmpty() || wrongCount.isEmpty() || emptyCount.isEmpty()) "" else "$trueCount / $wrongCount / $emptyCount",
        placeHolder = placeHolder,
        onValueChange = { },
        regex = String::isNotEmpty,
        enabled = false,
        modifier = modifier.clickable {
            showDialog = true
        }
    ) {

    }
}


@Composable
fun CustomSelectionDialog(
    modifier: Modifier = Modifier,
    data: List<String>,
    placeHolder: String,
    enabled: Boolean = true,
    onDataSelected: (String) -> Unit,
    title: String,
    selected: String = ""
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedData by remember { mutableStateOf(selected) }
    val state = rememberLazyListState()

    val centerItem by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val centerOffset = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                val itemCenter = it.offset + it.size / 2
                abs(itemCenter - centerOffset)
            }?.index ?: 0
        }
    }

    LaunchedEffect(key1 = showDialog) {
        if (showDialog) state.scrollToItem(0)
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                color = BackgroundColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                    LazyColumn(
                        state = state,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(((LocalConfiguration.current.screenHeightDp * 0.5) * 0.20).dp))
                        }
                        if (data.isEmpty()) {
                            item {
                                Text(
                                    text = "No data available",
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = TextColor,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        items(data.size) { item ->
                            Text(
                                text = data[item],
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = TextColor,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        rotationX = when (item) {
                                            centerItem -> 0f
                                            else -> (centerItem - item) * 7.25f
                                        }

                                    }
                                    .clickable {
                                        selectedData = data[item]
                                        onDataSelected(selectedData)
                                        showDialog = false
                                    }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(((LocalConfiguration.current.screenHeightDp * 0.5) * 0.20).dp))
                        }
                    }
                }
            }
        }
    }

    IconTextField(
        givenValue = selectedData,
        placeHolder = placeHolder,
        onValueChange = { selectedData = it },
        regex = String::isNotEmpty,
        enabled = false,
        modifier = modifier
            .then(
                if (enabled) Modifier.clickable {
                    showDialog = true
                } else {
                    Modifier
                }
            )
    ) {

    }
}


@Composable
fun ImagedCustomSelectionDialog(
    modifier: Modifier = Modifier,
    data: HashMap<String, Int>,
    placeHolder: String,
    enabled: Boolean = true,
    onDataSelected: (String) -> Unit,
    title: String,
    selected: String = ""
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedData by remember { mutableStateOf(selected) }
    val state = rememberLazyListState()

    val centerItem by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val centerOffset = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                val itemCenter = it.offset + it.size / 2
                abs(itemCenter - centerOffset)
            }?.index ?: 0
        }
    }

    LaunchedEffect(key1 = showDialog) {
        if (showDialog) state.scrollToItem(0)
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                color = BackgroundColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        color = TextColor,
                        fontSize = 18.sp
                    )
                    LazyColumn(
                        state = state,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(((LocalConfiguration.current.screenHeightDp * 0.5) * 0.20).dp))
                        }
                        if (data.isEmpty()) {
                            item {
                                Text(
                                    text = "No data available",
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = TextColor,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        items(data.size) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        rotationX = when (item) {
                                            centerItem -> 0f
                                            else -> (centerItem - item) * 7.25f
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = data.keys.elementAt(item),
                                    textAlign = TextAlign.Center,
                                    color = TextColor,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(end = 16.dp, top = 4.dp, bottom = 4.dp)
                                        .clickable {
                                            selectedData = data.keys.elementAt(item)
                                            onDataSelected(selectedData)
                                            showDialog = false
                                        }
                                )
                                Icon(
                                    painter = painterResource(id = data.values.elementAt(item)),
                                    contentDescription = "",
                                    tint = TextColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(((LocalConfiguration.current.screenHeightDp * 0.5) * 0.20).dp))
                        }
                    }
                }
            }
        }
    }

    IconTextField(
        givenValue = selectedData,
        placeHolder = placeHolder,
        onValueChange = { selectedData = it },
        regex = String::isNotEmpty,
        enabled = false,
        modifier = modifier
            .then(
                if (enabled) Modifier.clickable {
                    showDialog = true
                } else {
                    Modifier
                }
            )
    ) {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateTimePicker(
    modifier: Modifier = Modifier,
    placeHolder: String,
    onDateSelected: (String) -> Unit,
    selectedDate: String = "",
    selectableDates: SelectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }
    }
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(selectedDate) }
    val currentYear = LocalDate.now().year
    val datePickerState =
        rememberDatePickerState(
            yearRange = currentYear..(currentYear + 1),
            selectableDates = selectableDates
        )

    if (showDatePicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = BackgroundColor,
                dayContentColor = GoldColor,
                weekdayContentColor = GoldColor,
                selectedDayContentColor = GoldColor,
                selectedDayContainerColor = GoldColor,
                currentYearContentColor = GoldColor,
                yearContentColor = GoldColor,
                selectedYearContainerColor = GoldColor,
                todayDateBorderColor = GoldColor,
            ),
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        date = datePickerState.selectedDateMillis?.toDate() ?: ""
                        onDateSelected(date)
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = "Seç")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "İptal")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = BackgroundColor,
                    dayContentColor = GoldColor,
                    weekdayContentColor = GoldColor,
                    selectedDayContentColor = GoldColor,
                    selectedDayContainerColor = GoldColor,
                    currentYearContentColor = GoldColor,
                    yearContentColor = GoldColor,
                    selectedYearContainerColor = GoldColor,
                    todayDateBorderColor = GoldColor,

                    )
            )
        }
    }


    IconTextField(
        givenValue = if (date.isEmpty()) "" else date.dateTimeToFormattedDate(),
        placeHolder = placeHolder,
        onValueChange = { date = it },
        regex = String::isNotEmpty,
        enabled = false,
        modifier = modifier.clickable {
            showDatePicker = true
        }
    ) {}
}


@Preview(showBackground = true)
@Composable
private fun MotikocDialogPreview() {
    MotikocTheme {
        ExamLessonResultDialog(Modifier, "", "", {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MotikocDialogPreviewDark() {
    MotikocTheme(darkTheme = true) {
        ExamLessonResultDialog(Modifier, "", "", {})
    }
}
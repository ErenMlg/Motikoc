package com.softcross.motikoc.presentation.assignments

import com.softcross.motikoc.presentation.assignments.AssignmentContract.AssignmentState
import com.softcross.motikoc.presentation.assignments.AssignmentContract.AssignmentAction
import com.softcross.motikoc.presentation.assignments.AssignmentContract.AssignmentEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.softcross.motikoc.common.extensions.hoursUntil
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.LevelItem
import com.softcross.motikoc.presentation.home.HomeContract.UiEffect
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.DarkBackground
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.PoppinsMedium
import com.softcross.motikoc.presentation.theme.PrimaryGreen
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDateTime

@Composable
fun Assignments(
    uiState: AssignmentState,
    uiEffect: Flow<AssignmentEffect>,
    onAction: (AssignmentAction) -> Unit,
) {
    val userLevelInfo = calculateLevel(uiState.totalXP)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is AssignmentEffect.ShowToast -> TODO()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PrimarySurface)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_user),
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
            Column {
                Text(
                    text = MotikocSingleton.getUserName(),
                    color = TextColor,
                    fontSize = 18.sp,
                    fontFamily = PoppinsMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = "Seviye ${userLevelInfo.level} : ${userLevelInfo.title}",
                    color = TextColor,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        AssignmentLevelContent(userLevelInfo = userLevelInfo)
        Text(
            text = "Günlük Görevler",
            color = TextColor,
            fontFamily = PoppinsMedium,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
        )
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(
                uiState.assignments.size,
                key = { uiState.assignments[it].assignmentID }) { assignment ->
                val currentAssignment = uiState.assignments[assignment]
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimarySurface)
                        .then(
                            if (currentAssignment.isCompleted) {
                                Modifier.border(2.dp, PrimaryGreen, RoundedCornerShape(8.dp))
                            } else {
                                Modifier
                            }
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(end = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentAssignment.assignmentName,
                                textDecoration = if (currentAssignment.isCompleted) TextDecoration.LineThrough else null,
                                color = TextColor,
                                fontFamily = PoppinsMedium,
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = currentAssignment.assignmentDetail,
                            textDecoration = if (currentAssignment.isCompleted) TextDecoration.LineThrough else null,
                            color = TextColor,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Son ${currentAssignment.dueDate.hoursUntil()} Saat",
                                color = if (currentAssignment.isCompleted) PrimaryGreen else GoldColor,
                                fontFamily = PoppinsMedium,
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .padding(end = 8.dp),
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(CircleShape)
                                    .then(
                                        if (currentAssignment.isCompleted) {
                                            Modifier.background(PrimaryGreen)
                                        } else {
                                            Modifier.background(GoldColor)
                                        }
                                    )
                                    .padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = "${currentAssignment.assignmentXP}XP",
                                    color = if (currentAssignment.isCompleted) BackgroundColor else DarkBackground,
                                    fontFamily = PoppinsMedium,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        IconButton(
                            onClick = {
                                onAction(AssignmentAction.FinishAssignment(currentAssignment))
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .then(
                                    if (currentAssignment.isCompleted) {
                                        Modifier.background(PrimaryGreen)
                                    } else {
                                        Modifier.border(2.dp, GoldColor, CircleShape)
                                    }
                                )
                                .size(32.dp)
                                .align(Alignment.Center)
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
}

@Composable
fun AssignmentLevelContent(
    userLevelInfo: LevelItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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

@Preview(showBackground = true)
@Composable
private fun AssignmentPrewLight() {
    MotikocTheme {
        Assignments(
            uiState = AssignmentState(
                assignments = listOf(
                    Assignment(
                        assignmentID = "1",
                        assignmentName = "Türev'den 100 soru çöz",
                        assignmentDetail = "Türevden 100 adet soru çözüp yanlışlarını kontrol et",
                        assignmentXP = 500,
                        dueDate = LocalDateTime.now().plusDays(1),
                        isCompleted = false
                    ),
                    Assignment(
                        assignmentID = "2",
                        assignmentName = "Türev'den 100 soru çöz",
                        assignmentDetail = "Türevden 100 adet soru çözüp yanlışlarını kontrol et",
                        assignmentXP = 500,
                        dueDate = LocalDateTime.now().plusDays(1),
                        isCompleted = false
                    ),
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssignmentPrewDark() {
    MotikocTheme(darkTheme = true) {
        Assignments(
            uiState = AssignmentState(
                assignments = listOf(
                    Assignment(
                        assignmentID = "1",
                        assignmentName = "Türev'den 100 soru çöz",
                        assignmentDetail = "Türevden 100 adet soru çözüp yanlışlarını kontrol et",
                        assignmentXP = 500,
                        dueDate = LocalDateTime.now().plusDays(1),
                        isCompleted = false
                    ),
                    Assignment(
                        assignmentID = "2",
                        assignmentName = "Türev'den 100 soru çöz",
                        assignmentDetail = "Türevden 100 adet soru çözüp yanlışlarını kontrol et",
                        assignmentXP = 500,
                        dueDate = LocalDateTime.now().plusDays(1),
                        isCompleted = false
                    ),
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}
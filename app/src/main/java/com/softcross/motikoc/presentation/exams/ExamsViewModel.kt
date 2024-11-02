package com.softcross.motikoc.presentation.exams

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.domain.model.ExamItem
import com.softcross.motikoc.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.softcross.motikoc.presentation.exams.ExamsContract.ExamState
import com.softcross.motikoc.presentation.exams.ExamsContract.ExamEvent
import com.softcross.motikoc.presentation.exams.ExamsContract.ExamEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExamsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExamState())
    val uiState: StateFlow<ExamState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<ExamEffect>() }
    val uiEffect: Flow<ExamEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onEvent(event: ExamEvent) {
        when (event) {
            is ExamEvent.OnTurkishLessonItemChanged -> {
                updateUiState { copy(turkishLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnHistoryLessonItemChanged -> {
                updateUiState { copy(historyLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnGeographyLessonItemChanged -> {
                updateUiState { copy(geographyLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnPhilosophyLessonItemChanged -> {
                updateUiState { copy(philosophyLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnReligionLessonItemChanged -> {
                updateUiState { copy(religionLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnMathLessonItemChanged -> {
                updateUiState { copy(mathLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnGeometryLessonItemChanged -> {
                updateUiState { copy(geometryLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnPhysicsLessonItemChanged -> {
                updateUiState { copy(physicsLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnChemistryLessonItemChanged -> {
                updateUiState { copy(chemistryLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnBiologyLessonItemChanged -> {
                updateUiState { copy(biologyLessonItem = event.lessonItem) }
                checkEnable()
            }

            is ExamEvent.OnExamNameChanged -> {
                updateUiState { copy(examName = event.examName) }
                checkEnable()
            }

            is ExamEvent.OnExamDateChanged -> {
                updateUiState { copy(examDate = event.examDate) }
                checkEnable()
            }

            is ExamEvent.OnQuestionCountChanged -> {
                updateUiState { copy(questionCount = event.questionCount) }
                checkEnable()
            }

            is ExamEvent.OnQuestionTimeChanged -> {
                updateUiState { copy(questionTime = event.questionTime) }
                checkEnable()
            }

            is ExamEvent.OnSaveExamClicked -> {
                addExamToFirebase()
            }
        }
    }

    private fun addExamToFirebase() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        try {
            val uiState = uiState.value
            val totalWrongCount =
                uiState.turkishLessonItem!!.lessonWrongCount + uiState.historyLessonItem!!.lessonWrongCount + uiState.geographyLessonItem!!.lessonWrongCount + uiState.philosophyLessonItem!!.lessonWrongCount + uiState.religionLessonItem!!.lessonWrongCount + uiState.mathLessonItem!!.lessonWrongCount + uiState.geometryLessonItem!!.lessonWrongCount + uiState.physicsLessonItem!!.lessonWrongCount + uiState.chemistryLessonItem!!.lessonWrongCount + uiState.biologyLessonItem!!.lessonWrongCount
            val totalCorrectCount =
                uiState.turkishLessonItem.lessonCorrectCount + uiState.historyLessonItem.lessonCorrectCount + uiState.geographyLessonItem.lessonCorrectCount + uiState.philosophyLessonItem.lessonCorrectCount + uiState.religionLessonItem.lessonCorrectCount + uiState.mathLessonItem.lessonCorrectCount + uiState.geometryLessonItem.lessonCorrectCount + uiState.physicsLessonItem.lessonCorrectCount + uiState.chemistryLessonItem.lessonCorrectCount + uiState.biologyLessonItem.lessonCorrectCount
            val examItem = ExamItem(
                turkishLessonItem = uiState.turkishLessonItem,
                historyLessonItem = uiState.historyLessonItem,
                geographyLessonItem = uiState.geographyLessonItem,
                philosophyLessonItem = uiState.philosophyLessonItem,
                religionLessonItem = uiState.religionLessonItem,
                mathLessonItem = uiState.mathLessonItem,
                geometryLessonItem = uiState.geometryLessonItem,
                physicsLessonItem = uiState.physicsLessonItem,
                chemistryLessonItem = uiState.chemistryLessonItem,
                biologyLessonItem = uiState.biologyLessonItem,
                examName = uiState.examName,
                examDate = uiState.examDate!!,
                questionCount = uiState.questionCount,
                examTime = uiState.questionTime,
                net = (totalCorrectCount - (totalWrongCount * 0.25)).toFloat(),
            )
            firebaseRepository.addExamToFirestore(MotikocSingleton.getUserID(), examItem)
        } catch (e: Throwable) {
            Log.e("FirebaseRepositoryImpl", e.message.toString())
            emitUiEffect(ExamEffect.ShowMessage("Bir hata oluştu"))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private fun checkEnable() {
        updateUiState { copy(isEnable = turkishLessonItem != null && historyLessonItem != null && geographyLessonItem != null && philosophyLessonItem != null && religionLessonItem != null && mathLessonItem != null && geometryLessonItem != null && physicsLessonItem != null && chemistryLessonItem != null && biologyLessonItem != null && examName.isNotEmpty() && examDate != null && questionCount > 0 && questionTime.isNotEmpty()) }
    }

    private fun updateUiState(block: ExamState.() -> ExamState) {
        _uiState.value = _uiState.value.block()
    }

    private suspend fun emitUiEffect(uiEffect: ExamEffect) {
        _uiEffect.send(uiEffect)
    }

}
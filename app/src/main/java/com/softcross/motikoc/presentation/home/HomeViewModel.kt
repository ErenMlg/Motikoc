package com.softcross.motikoc.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.MotikocSingleton.updateUserXP
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.common.extensions.getCurrentDateTime
import com.softcross.motikoc.common.extensions.getMonthMaxDay
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.MotikocUser
import com.softcross.motikoc.domain.repository.FirebaseRepository
import com.softcross.motikoc.domain.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

import com.softcross.motikoc.presentation.home.HomeContract.UiState
import com.softcross.motikoc.presentation.home.HomeContract.UiEffect
import com.softcross.motikoc.presentation.home.HomeContract.UiAction
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerEffect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getUserInfo()
        initData()
    }

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.FinishAssignment -> finishAssignment(action.assignment)

            is UiAction.FinishToDo -> {}

            is UiAction.TryAgain -> {
                initData()
            }

            UiAction.OnExitClicked -> onExit()

            is UiAction.DaySelected -> {
                updateUiState { copy(selectedDay = action.day) }
                getDayPlans()
            }
        }
    }

    private fun getDayPlans() = viewModelScope.launch {
        updateUiState { copy(plannerLoading = true) }
        when (val response = firebaseRepository.getPlansFromFirestore(
            MotikocSingleton.getUserID(),
            uiState.value.selectedDay
        )) {
            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        plannerLoading = false,
                        errorMessage = response.exception.message
                            ?: "Bir hata oluştu, planlarınızı gösteremiyoruz."
                    )
                }
            }

            is ResponseState.Success -> {
                updateUiState { copy(plannerItems = response.result, plannerLoading = false) }
            }

            is ResponseState.Loading -> {}
        }
    }

    private fun getUserInfo() = viewModelScope.launch {
        val user = firebaseRepository.getUserDetailFromFirestore()
        MotikocSingleton.setUser(user)
    }

    private fun onExit() = viewModelScope.launch {
        firebaseRepository.signOutUser()
        MotikocSingleton.clearUser()
        emitUiEffect(UiEffect.NavigateToIntroduce)
    }

    private fun finishAssignment(assignment: Assignment) = viewModelScope.launch {
        val updatedAssignment = assignment.copy(isCompleted = true)
        val index =
            uiState.value.assignments.indexOfFirst { it.assignmentID == assignment.assignmentID }
        if (index != -1) {
            val updatedAssignments = uiState.value.assignments.toMutableList().apply {
                this[index] = updatedAssignment
            }
            updateUiState {
                copy(
                    assignments = updatedAssignments,
                    totalUserXP = totalUserXP + assignment.assignmentXP
                )
            }
        }
        firebaseRepository.updateAssignmentToFirestore(
            MotikocSingleton.getUserID(),
            updatedAssignment
        )
        firebaseRepository.addXpToUser(
            MotikocSingleton.getUserID(),
            uiState.value.totalUserXP
        )
        updateUserXP(uiState.value.totalUserXP)
    }


    private fun initData() {
        val localDate = LocalDate.now()
        updateUiState {
            copy(
                isLoading = true, errorMessage = "", selectedDay = localDate,
                days = listOf(
                    localDate,
                    localDate.plusDays(1),
                    localDate.plusDays(2),
                    localDate.plusDays(3),
                    localDate.plusDays(4),
                    localDate.plusDays(5),
                    localDate.plusDays(6)
                )
            )
        }
        MotikocSingleton.getUser()?.let {
            println(it)
            updateUiState {
                copy(
                    motivationPrompt = motivationPrompt +
                            "Hedef mesleğim: ${it.dreamJob}" +
                            "Hedef üniversitem: ${it.dreamUniversity}" +
                            "Hedef bölüm: ${it.dreamDepartment}" +
                            "Hedef puan: ${it.dreamPoint}" +
                            "ilgi alanları: ${it.interests}" +
                            "kişisel özellikleri: ${it.personalProperties}" +
                            "yeteneklerim: ${it.abilities}" +
                            "kendinden bahseden bir yazı: ${it.identify}"
                )
            }
        }
        getDayPlans()
        getAssignments()
        getMotivationMessage()
    }

    private fun getAssignments() = viewModelScope.launch {
        val user = MotikocSingleton.getUser()
        user?.let {
            val oldAssignments = user.assignmentHistory.filter {
                it.dueDate.isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
            }
            println(oldAssignments)
            if (oldAssignments.isEmpty()) {
                updateUiState {
                    copy(
                        assignmentPrompt = assignmentPrompt + user.assignmentHistory.joinToString(
                            ","
                        )
                    )
                }
                geminiRepository.sendAssignmentQuestion(uiState.value.assignmentPrompt)
                    .collect { response ->
                        when (response) {
                            is ResponseState.Success -> {
                                response.result.forEach { assignment ->
                                    val newAssignment = firebaseRepository.addAssignmentToFirestore(
                                        user.id,
                                        assignment
                                    )
                                    updateUiState { copy(assignments = assignments + newAssignment) }
                                }
                                MotikocSingleton.changeAssignmentHistory(firebaseRepository.getAssignmentsFromFirestore(user.id))
                                if (uiState.value.motivationMessage.isNotEmpty()) {
                                    updateUiState { copy(isLoading = false) }
                                }
                            }

                            is ResponseState.Error -> {
                                updateUiState {
                                    copy(
                                        isLoading = false,
                                        errorMessage = response.exception.message
                                            ?: "Bir hata oluştu, Lütfen tekrar deneyin."
                                    )
                                }
                            }

                            ResponseState.Loading -> {}
                        }
                    }
            } else {
                updateUiState { copy(assignments = oldAssignments) }
                if (uiState.value.motivationMessage.isNotEmpty()) {
                    updateUiState { copy(isLoading = false) }
                }
            }
        }
    }

    private fun getMotivationMessage() = viewModelScope.launch {
        geminiRepository.sendMotivationQuestion(uiState.value.motivationPrompt)
            .collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        val motivationMessage = response.result
                        updateUiState { copy(motivationMessage = motivationMessage) }
                        if (uiState.value.assignments.isNotEmpty()) {
                            updateUiState { copy(isLoading = false) }
                        }
                    }

                    is ResponseState.Error -> {
                        updateUiState {
                            copy(
                                isLoading = false,
                                errorMessage = response.exception.message
                                    ?: "Bir hata oluştu, Lütfen tekrar deneyin."
                            )
                        }
                    }

                    ResponseState.Loading -> {}
                }
            }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}
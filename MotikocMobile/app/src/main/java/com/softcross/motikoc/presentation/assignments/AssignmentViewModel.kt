package com.softcross.motikoc.presentation.assignments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.MotikocSingleton.updateUserXP
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.repository.FirebaseRepository
import com.softcross.motikoc.domain.repository.GeminiRepository
import com.softcross.motikoc.presentation.assignments.AssignmentContract.AssignmentAction
import com.softcross.motikoc.presentation.assignments.AssignmentContract.AssignmentEffect
import com.softcross.motikoc.presentation.assignments.AssignmentContract.AssignmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssignmentState())
    val uiState: StateFlow<AssignmentState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<AssignmentEffect>() }
    val uiEffect: Flow<AssignmentEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        updateUiState { copy(totalXP = MotikocSingleton.getUserTotalXP()) }
        if (MotikocSingleton.getUser()?.assignmentHistory?.isEmpty() == true) {
            getAssignments()
        }else{
            updateUiState { copy(assignments = MotikocSingleton.getUser()?.assignmentHistory ?: emptyList()) }
        }
    }

    fun onAction(action: AssignmentAction) {
        when (action) {
            is AssignmentAction.LoadAssignments -> getAssignments()
            is AssignmentAction.FinishAssignment -> finishAssignment(action.assignment)
        }
    }

    private fun finishAssignment(assignment: Assignment) = viewModelScope.launch {
        val updatedAssignment = assignment.copy(isCompleted = !assignment.isCompleted)
        if (updatedAssignment.isCompleted) {
            updateUiState { copy(totalXP = totalXP + assignment.assignmentXP) }
        } else {
            updateUiState { copy(totalXP = totalXP - assignment.assignmentXP) }
        }
        val index =
            uiState.value.assignments.indexOfFirst { it.assignmentID == assignment.assignmentID }
        if (index != -1) {
            val updatedAssignments = uiState.value.assignments.toMutableList().apply {
                this[index] = updatedAssignment
            }
            updateUiState {
                copy(
                    assignments = updatedAssignments,
                )
            }
        }
        MotikocSingleton.changeAssignmentHistory(uiState.value.assignments)
        firebaseRepository.updateAssignmentToFirestore(
            MotikocSingleton.getUserID(),
            updatedAssignment
        )
        updateUserXP(uiState.value.totalXP)
        firebaseRepository.changeUserXP(
            MotikocSingleton.getUserID(),
            uiState.value.totalXP
        )
    }

    private fun getAssignments() = viewModelScope.launch {
        val user = MotikocSingleton.getUser()
        user?.let {
            val oldAssignments = user.assignmentHistory.filter {
                it.dueDate.isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
            }
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
                                    updateUiState {
                                        copy(
                                            assignments = assignments + newAssignment,
                                            isLoading = false
                                        )
                                    }
                                }
                                MotikocSingleton.changeAssignmentHistory(uiState.value.assignments)
                            }

                            is ResponseState.Error -> {
                                updateUiState { copy(isLoading = false) }
                                emitUiEffect(
                                    AssignmentEffect.ShowToast(
                                        response.exception.message
                                            ?: "Bir hata oluştu, Lütfen tekrar deneyin."
                                    )
                                )
                            }

                            ResponseState.Loading -> {}
                        }
                    }
            } else {
                updateUiState { copy(assignments = oldAssignments, isLoading = false) }
            }
        }
    }

    private fun updateUiState(block: AssignmentState.() -> AssignmentState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: AssignmentEffect) {
        _uiEffect.send(uiEffect)
    }

}
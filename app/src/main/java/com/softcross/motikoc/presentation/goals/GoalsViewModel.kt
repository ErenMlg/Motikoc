package com.softcross.motikoc.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

import com.softcross.motikoc.presentation.goals.GoalsContract.UiState
import com.softcross.motikoc.presentation.goals.GoalsContract.UiEffect
import com.softcross.motikoc.presentation.goals.GoalsContract.UiAction
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnDreamUniversityChanged -> {
                updateUiState {
                    copy(dreamUniversity = uiAction.dreamUniversity)
                }
            }

            is UiAction.OnDreamDepartmentChanged -> {
                updateUiState {
                    copy(dreamDepartment = uiAction.dreamDepartment)
                }
            }

            is UiAction.OnDreamPointChanged -> {
                updateUiState {
                    copy(dreamPoint = uiAction.dreamPoint)
                }
            }

            is UiAction.OnDreamRankChanged -> {
                updateUiState {
                    copy(dreamRank = uiAction.dreamRank)
                }
            }

            is UiAction.OnSaveButtonClicked -> {
                addDreams()
            }
        }
    }

    private fun addDreams() = viewModelScope.launch {
        try {
            firebaseRepository.addDreamsToFirestore(
                userID = MotikocSingleton.getUserID(),
                dreamUniversity = _uiState.value.dreamUniversity,
                dreamDepartment = _uiState.value.dreamDepartment,
                dreamPoint = _uiState.value.dreamPoint,
                dreamRank = _uiState.value.dreamRank
            )
            emitUiEffect(UiEffect.NavigateToHome)
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowErrorToast("Bir hata oluştu\n${e.localizedMessage}"))
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}
package com.softcross.motikoc.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.softcross.motikoc.presentation.register.RegisterContract.UiState
import com.softcross.motikoc.presentation.register.RegisterContract.UiAction
import com.softcross.motikoc.presentation.register.RegisterContract.UiEffect

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.FullNameChanged -> updateUiState { copy(fullName = uiAction.fullName) }
            is UiAction.EmailChanged -> updateUiState { copy(email = uiAction.email.trim()) }
            is UiAction.PasswordChanged -> updateUiState { copy(password = uiAction.password.trim()) }
            is UiAction.ConfirmPasswordChanged -> updateUiState { copy(rePassword = uiAction.password.trim()) }
            is UiAction.RegisterClick -> register()
        }
    }

    private fun register() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = firebaseRepository.registerUser(
            uiState.value.email,
            uiState.value.password,
            uiState.value.fullName
        )) {
            is ResponseState.Error -> emitUiEffect(UiEffect.ShowSnackbar(result.exception.message.toString()))

            is ResponseState.Success -> {
                val user = firebaseRepository.getUserDetailFromFirestore()
                MotikocSingleton.setUser(user)
                if (user.dreamJob.isEmpty()) {
                    emitUiEffect(UiEffect.NavigateToJobWizard)
                } else {
                    if (user.dreamUniversity.isEmpty()){
                        emitUiEffect(UiEffect.NavigateToGoals)
                    } else {
                        emitUiEffect(UiEffect.NavigateToHome)
                    }
                }
            }

            is ResponseState.Loading -> {}
        }
        updateUiState { copy(isLoading = false) }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}
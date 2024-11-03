package com.softcross.motikoc.presentation.login

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
import javax.inject.Inject
import kotlinx.coroutines.flow.update

import com.softcross.motikoc.presentation.login.LoginContract.UiState
import com.softcross.motikoc.presentation.login.LoginContract.UiAction
import com.softcross.motikoc.presentation.login.LoginContract.UiEffect
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.LoginClick -> login()
            is UiAction.EmailChanged -> updateUiState { copy(email = uiAction.email) }
            is UiAction.PasswordChanged -> updateUiState { copy(password = uiAction.password) }
        }
    }

    private fun login() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = firebaseRepository.loginUser(
            uiState.value.email.trim(),
            uiState.value.password.trim()
        )) {
            is ResponseState.Error -> emitUiEffect(UiEffect.ShowSnackbar(result.exception.message.toString()))

            is ResponseState.Success -> {
                MotikocSingleton.setUser(result.result)
                if (result.result.dreamJob.isEmpty()) {
                    emitUiEffect(UiEffect.NavigateToJobWizard)
                } else {
                    if (result.result.dreamUniversity.isEmpty()) {
                        emitUiEffect(UiEffect.NavigateToGoals)
                    } else {
                        emitUiEffect(UiEffect.NavigateToHome)
                    }
                }
            }

            ResponseState.Loading -> {}
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
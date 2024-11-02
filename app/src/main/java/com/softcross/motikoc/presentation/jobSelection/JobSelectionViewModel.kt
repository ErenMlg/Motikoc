package com.softcross.motikoc.presentation.jobSelection

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.JobRecommend
import com.softcross.motikoc.domain.repository.FirebaseRepository
import com.softcross.motikoc.domain.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

import com.softcross.motikoc.presentation.jobSelection.JobSelectionContract.UiState
import com.softcross.motikoc.presentation.jobSelection.JobSelectionContract.UiAction
import com.softcross.motikoc.presentation.jobSelection.JobSelectionContract.UiEffect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class JobSelectionViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val geminiRepository: GeminiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        var personalProperties = ""
        var userInterests = ""
        var userAbilities = ""
        var userArea = ""
        var userIdentify = ""
        savedStateHandle.get<String>("personalProperties")?.let { personal ->
            personalProperties = personal
            updateUiState { copy(prompt = "$prompt\nKişisel özelliklerim : $personal") }
        }
        savedStateHandle.get<String>("interests")?.let { interests ->
            userInterests = interests
            updateUiState { copy(prompt = "$prompt\nİlgi alanlarım : $interests") }
        }
        savedStateHandle.get<String>("abilities")?.let { abilities ->
            userAbilities = abilities
            updateUiState { copy(prompt = "$prompt\nYetenek ve Becerilerim : $abilities") }
        }
        savedStateHandle.get<String>("area")?.let { area ->
            userArea = area
            updateUiState { copy(prompt = "$prompt\nAlan Tercihim : $area") }
        }
        savedStateHandle.get<String>("identify")?.let { identify ->
            userIdentify = identify
            updateUiState { copy(prompt = "$prompt\nKendimi Tanıtıcı Metnim : $identify") }
        }
        saveUserInfosToFirestore(
            personalProperties = personalProperties,
            interests = userInterests,
            abilities = userAbilities,
            area = userArea,
            identify = userIdentify
        )
        sendPrompt()
    }

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.ReSendPrompt -> {
                val jobs = uiState.value.jobRecommendList.map { it.name }.joinToString(",")
                if (uiState.value.retryCount == 0) {
                    updateUiState {
                        copy(
                            retryCount = retryCount + 1,
                            prompt = "$prompt\nBana uygun aşağıdaki meslekler haricinde 5 adet meslek önerir misin;\n$jobs"
                        )
                    }
                } else {
                    updateUiState { copy(retryCount = retryCount + 1, prompt = "$prompt,$jobs") }
                }
                sendPrompt()
            }

            is UiAction.SelectJob -> {
                selectJob(action.job)
            }

            UiAction.TryAgain -> sendPrompt()
        }
    }

    private fun saveUserInfosToFirestore(
        personalProperties: String,
        interests: String,
        abilities: String,
        area: String,
        identify: String
    ) = viewModelScope.launch {
        firebaseRepository.addPersonalInfosToFirestore(
            MotikocSingleton.getUserID(),
            personalProperties,
            interests,
            abilities,
            area,
            identify
        )
    }

    private fun sendPrompt() = viewModelScope.launch {
        geminiRepository.jobRecommendChat(uiState.value.prompt).collect { responseState ->
            Log.e("sendPrompt", uiState.value.prompt)
            when (responseState) {
                is ResponseState.Loading -> updateUiState { copy(isLoading = true) }

                is ResponseState.Success -> {
                    updateUiState {
                        copy(
                            isLoading = false,
                            jobRecommendList = responseState.result
                        )
                    }
                }

                is ResponseState.Error -> {
                    println(responseState.exception.message ?: "An error occurred")
                    updateUiState {
                        copy(isLoading = false)
                    }
                    emitUiEffect(
                        UiEffect.ShowSnackbar(
                            responseState.exception.message ?: "An error occurred"
                        )
                    )
                }
            }
        }
    }

    private fun selectJob(job: JobRecommend) = viewModelScope.launch {
        firebaseRepository.addJobToFirestore(job.name, MotikocSingleton.getUserID())
        val user = firebaseRepository.getUserDetailFromFirestore()
        MotikocSingleton.setUser(user)
        emitUiEffect(UiEffect.NavigateToHome)
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}
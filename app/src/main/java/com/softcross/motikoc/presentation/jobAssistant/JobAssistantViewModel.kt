package com.softcross.motikoc.presentation.jobAssistant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.ChatItem
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

import com.softcross.motikoc.presentation.jobAssistant.JobAssistantContract.UiState
import com.softcross.motikoc.presentation.jobAssistant.JobAssistantContract.UiEffect
import com.softcross.motikoc.presentation.jobAssistant.JobAssistantContract.UiAction
import kotlinx.coroutines.launch

@HiltViewModel
class JobAssistantViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val geminiRepository: GeminiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        savedStateHandle.get<String>("recommendedJobs")?.let { jsonJobs ->
            savedStateHandle.get<String>("clickedJob")?.let { clickedJob ->
                val jobs: List<AssistantJobItem> =
                    Gson().fromJson(jsonJobs, object : TypeToken<List<AssistantJobItem>>() {}.type)
                updateUiState {
                    copy(
                        jobList = jobs,
                        selectedJob = AssistantJobItem(
                            clickedJob,
                            jobs.find { it.name == clickedJob }?.image ?: ""
                        ),
                        originalPrompt = "Merhaba senden kariyer asistanı gibi davranmanı istiyorum, sana seçtiğim meslek olan $clickedJob hakkında sorular soracağım onları yanıtlamanı istiyorum. "
                    )
                }
            }
        }
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.SendMessage -> sendMessage()
            is UiAction.OnPromptChanged -> updateUiState { copy(prompt = uiAction.prompt) }
            is UiAction.OnJobSelectionChanged -> updateUiState {
                copy(
                    selectedJob = uiAction.job,
                    originalPrompt = "Merhaba senden kariyer asistanı gibi davranmanı istiyorum, sana seçtiğim meslek olan ${uiAction.job.name} hakkında sorular soracağım onları yanıtlamanı istiyorum. "
                )
            }

            UiAction.OnDoneClicked -> {
                setUserTargetJob()
            }
        }
    }

    private fun setUserTargetJob() = viewModelScope.launch {
        firebaseRepository.addJobToFirestore(
            uiState.value.selectedJob?.name ?: "",
            MotikocSingleton.getUserID()
        )
        emitUiEffect(UiEffect.NavigateToHome)
    }

    private fun sendMessage() = viewModelScope.launch {
        geminiRepository.chatWithAssistant(uiState.value.originalPrompt + uiState.value.prompt)
            .collect { response ->
                when (response) {
                    is ResponseState.Loading -> updateUiState {
                        copy(
                            isLoading = true,
                            chatList = chatList + ChatItem(prompt, true)
                        )
                    }

                    is ResponseState.Success -> {
                        updateUiState {
                            copy(
                                chatList = chatList + response.result,
                                prompt = "",
                                isLoading = false,
                            )
                        }
                    }

                    is ResponseState.Error -> {
                        updateUiState {
                            copy(
                                isLoading = false,
                                isError = true
                            )
                        }
                    }
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

data class AssistantJobItem(
    val name: String,
    val image: String
)
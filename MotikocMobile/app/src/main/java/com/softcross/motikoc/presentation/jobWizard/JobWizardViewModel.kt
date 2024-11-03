package com.softcross.motikoc.presentation.jobWizard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

import com.softcross.motikoc.presentation.jobWizard.JobWizardContract.UiState
import com.softcross.motikoc.presentation.jobWizard.JobWizardContract.UiEffect
import com.softcross.motikoc.presentation.jobWizard.JobWizardContract.UiAction


@HiltViewModel
class JobWizardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnInterestAdded -> {
                updateUiState {
                    copy(interests = interests + uiAction.interest)
                }
            }

            is UiAction.OnInterestRemoved -> {
                updateUiState {
                    copy(interests = interests - uiAction.interest)
                }
            }

            is UiAction.OnAbilityAdded -> {
                updateUiState {
                    copy(abilities = abilities + uiAction.interest)
                }
            }

            is UiAction.OnAbilityRemoved -> {
                updateUiState {
                    copy(abilities = abilities - uiAction.interest)
                }
            }

            is UiAction.OnPersonalPropertyAdded -> {
                updateUiState {
                    copy(personalProperties = personalProperties + uiAction.interest)
                }
            }

            is UiAction.OnPersonalPropertyRemoved -> {
                updateUiState {
                    copy(personalProperties = personalProperties - uiAction.interest)
                }
            }

            is UiAction.OnAreaAdded -> {
                updateUiState {
                    copy(area = area + uiAction.interest)
                }
            }

            is UiAction.OnAreaRemoved -> {
                updateUiState {
                    copy(area = area - uiAction.interest)
                }
            }

            is UiAction.OnIdentifyPromptChanged -> {
                updateUiState {
                    copy(identifyPrompt = uiAction.identifyPrompt)
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
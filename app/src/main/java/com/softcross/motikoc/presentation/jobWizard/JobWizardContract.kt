package com.softcross.motikoc.presentation.jobWizard

object JobWizardContract {
    data class UiState(
        val interests : List<String> = emptyList(),
        val abilities : List<String> = emptyList(),
        val personalProperties : List<String> = emptyList(),
        val area : List<String> = emptyList(),
        val identifyPrompt : String = "",
    )

    sealed class UiAction {
        data class OnInterestAdded(val interest: String) : UiAction()
        data class OnInterestRemoved(val interest: String) : UiAction()
        data class OnAbilityAdded(val interest: String) : UiAction()
        data class OnAbilityRemoved(val interest: String) : UiAction()
        data class OnPersonalPropertyAdded(val interest: String) : UiAction()
        data class OnPersonalPropertyRemoved(val interest: String) : UiAction()
        data class OnAreaAdded(val interest: String) : UiAction()
        data class OnAreaRemoved(val interest: String) : UiAction()
        data class OnIdentifyPromptChanged(val identifyPrompt: String) : UiAction()
    }

    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
    }
}
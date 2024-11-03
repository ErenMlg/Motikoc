package com.softcross.motikoc.presentation.register

object RegisterContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val rePassword: String = "",
        val fullName: String = ""
    )

    sealed class UiAction {
        data object RegisterClick : UiAction()
        data class FullNameChanged(val fullName: String) : UiAction()
        data class EmailChanged(val email: String) : UiAction()
        data class PasswordChanged(val password: String) : UiAction()
        data class ConfirmPasswordChanged(val password: String) : UiAction()
    }

    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data object NavigateToHome : UiEffect()
        data object NavigateToJobWizard : UiEffect()
        data object NavigateToGoals : UiEffect()
    }
}
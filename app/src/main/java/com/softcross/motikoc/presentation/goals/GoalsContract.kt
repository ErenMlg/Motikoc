package com.softcross.motikoc.presentation.goals

object GoalsContract {

    data class UiState(
        val dreamUniversity : String = "",
        val dreamDepartment : String = "",
        val dreamPoint: String = "",
        val dreamRank: String = "",
    )

    sealed class UiAction {
        data class OnDreamUniversityChanged(val dreamUniversity: String) : UiAction()
        data class OnDreamDepartmentChanged(val dreamDepartment: String) : UiAction()
        data class OnDreamPointChanged(val dreamPoint: String) : UiAction()
        data class OnDreamRankChanged(val dreamRank: String) : UiAction()
        data object OnSaveButtonClicked : UiAction()
    }

    sealed class UiEffect {
        object NavigateToHome : UiEffect()
        data class ShowErrorToast(val message:String) : UiEffect()
    }

}
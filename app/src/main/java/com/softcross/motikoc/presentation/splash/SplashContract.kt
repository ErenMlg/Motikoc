package com.softcross.motikoc.presentation.splash

object SplashContract {
    sealed class UiEffect {
        data object NavigateToHome : UiEffect()
        data object NavigateToIntroduce : UiEffect()
        data object NavigateToJobWizard : UiEffect()
    }
}
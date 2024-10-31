package com.softcross.motikoc.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiEffect by lazy { Channel<SplashContract.UiEffect>() }
    val uiEffect: Flow<SplashContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        if (firebaseRepository.checkLoggedUser()) {
            try {
                val user = firebaseRepository.getUserDetailFromFirestore()
                MotikocSingleton.setUser(user)
                delay(2000)
                if(user.dreamJob.isEmpty()){
                    emitUiEffect(SplashContract.UiEffect.NavigateToJobWizard)
                }else{
                    emitUiEffect(SplashContract.UiEffect.NavigateToHome)
                }
            } catch (e: Exception) {
                emitUiEffect(SplashContract.UiEffect.NavigateToIntroduce)
            }
        }else{
            delay(2000)
            emitUiEffect(SplashContract.UiEffect.NavigateToIntroduce)
        }
    }


    private suspend fun emitUiEffect(uiEffect: SplashContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}
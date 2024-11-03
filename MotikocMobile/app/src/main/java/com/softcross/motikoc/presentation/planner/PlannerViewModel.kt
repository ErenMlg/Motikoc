package com.softcross.motikoc.presentation.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.common.extensions.getMonthMaxDay
import com.softcross.motikoc.common.extensions.stringToLocalDate
import com.softcross.motikoc.domain.model.PlannerItem
import com.softcross.motikoc.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerState
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerEffect
import com.softcross.motikoc.presentation.planner.PlannerContract.PlannerAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _plannerState = MutableStateFlow(PlannerState())
    val plannerState: StateFlow<PlannerState> = _plannerState.asStateFlow()

    private val _plannerEffect by lazy { Channel<PlannerEffect>() }
    val plannerEffect: Flow<PlannerEffect> by lazy { _plannerEffect.receiveAsFlow() }

    init {
        val date = LocalDate.now()
        updatePlannerState {
            copy(
                selectedMonth = date.monthValue,
                selectedYear = date.year,
                maxDayOfMonth = getMonthMaxDay(date.year, date.monthValue)
            )
        }
        if (MotikocSingleton.getUser()?.schedule?.isEmpty() == true) {
            getDayPlans()
        }else{
            updatePlannerState { copy(plannerItems = MotikocSingleton.getUser()?.schedule?.filter { it.plannerDate.stringToLocalDate("MM-dd-yyyy") == date } ?: emptyList()) }
        }
    }


    fun onAction(plannerAction: PlannerAction) {
        when (plannerAction) {
            is PlannerAction.OnNextMonthClicked -> {
                if (plannerState.value.selectedMonth + 1 > 12) {
                    updatePlannerState {
                        copy(
                            selectedMonth = 1,
                            selectedYear = selectedYear + 1,
                            maxDayOfMonth = getMonthMaxDay(selectedYear + 1, 1)
                        )
                    }
                } else {
                    updatePlannerState {
                        copy(
                            selectedMonth = selectedMonth + 1,
                            maxDayOfMonth = getMonthMaxDay(selectedYear, selectedMonth + 1)
                        )
                    }
                }
            }

            is PlannerAction.OnPreviousMonthClicked -> {
                if (plannerState.value.selectedMonth - 1 < 1) {
                    updatePlannerState {
                        copy(
                            selectedMonth = 12,
                            selectedYear = selectedYear - 1,
                            maxDayOfMonth = getMonthMaxDay(selectedYear - 1, 12)
                        )
                    }
                } else {
                    updatePlannerState {
                        copy(
                            selectedMonth = selectedMonth - 1,
                            maxDayOfMonth = getMonthMaxDay(selectedYear, selectedMonth - 1)
                        )
                    }
                }
            }

            is PlannerAction.OnDaySelected -> {
                updatePlannerState { copy(selectedDay = plannerAction.day) }
                getDayPlans()
            }

            is PlannerAction.OnAddNewPlan -> addNewPlan(plannerAction.plannerItem)
        }
    }

    private fun getDayPlans() = viewModelScope.launch {
        updatePlannerState { copy(isLoading = true) }
        try {
            val response = firebaseRepository.getPlansFromFirestore(
                MotikocSingleton.getUserID(),
                plannerState.value.selectedDay
            )
            updatePlannerState { copy(plannerItems = response, isLoading = false) }
        } catch (e: Exception) {
            updatePlannerState { copy(isLoading = false) }
            emitPlannerEffect(
                PlannerEffect.ShowError(
                    e.message ?: "Birşeyler yanlış gitti, lütfen tekrar deneyin"
                )
            )
        } finally {
            updatePlannerState { copy(isLoading = false) }
        }
    }

    private fun addNewPlan(plannerItem: PlannerItem) = viewModelScope.launch {
        try {
            firebaseRepository.addPlanToFirestore(
                MotikocSingleton.getUserID(),
                plannerItem
            )
            getDayPlans()
            emitPlannerEffect(PlannerEffect.ShowSuccess("Plan başarıyla eklendi."))
        } catch (e: Exception) {
            emitPlannerEffect(
                PlannerEffect.ShowError(
                    e.message ?: "Birşeyler yanlış gitti, tekrar deneyiniz."
                )
            )
        }
    }

    private fun updatePlannerState(block: PlannerState.() -> PlannerState) {
        _plannerState.value = _plannerState.value.block()
    }

    private suspend fun emitPlannerEffect(uiEffect: PlannerEffect) {
        _plannerEffect.send(uiEffect)
    }

}
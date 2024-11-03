package com.softcross.motikoc.presentation.planner

import com.softcross.motikoc.domain.model.PlannerItem
import java.time.LocalDate

object PlannerContract {
    data class PlannerState(
        val isLoading: Boolean = false,
        val plannerItems: List<PlannerItem> = emptyList(),
        val selectedMonth: Int = 1,
        val selectedYear: Int = 2024,
        val selectedDay: LocalDate = LocalDate.now(),
        val maxDayOfMonth: Int = 1
    )

    sealed class PlannerAction {
        data object OnNextMonthClicked : PlannerAction()
        data object OnPreviousMonthClicked : PlannerAction()
        data class OnAddNewPlan(val plannerItem: PlannerItem) : PlannerAction()
        data class OnDaySelected(val day: LocalDate) : PlannerAction()
    }

    sealed class PlannerEffect {
        data class ShowError(val message: String) : PlannerEffect()
        data class ShowSuccess(val message: String) : PlannerEffect()
    }

}
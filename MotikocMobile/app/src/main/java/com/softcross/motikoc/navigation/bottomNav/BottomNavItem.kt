package com.softcross.motikoc.navigation.bottomNav

import com.softcross.motikoc.R

sealed class BottomNavItem(
    val route: String = "",
    val icon: Int = 0
) {

    data object Home : BottomNavItem(
        route = "home",
        icon = R.drawable.icon_home
    )

    data object Planner : BottomNavItem(
        route = "planner",
        icon = R.drawable.icon_planner
    )

    data object Exams : BottomNavItem(
        route = "exams",
        icon = R.drawable.icon_exam
    )

    data object Assignments : BottomNavItem(
        route = "assignments",
        icon = R.drawable.icon_daily_assigments
    )

}
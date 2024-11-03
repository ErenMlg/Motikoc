package com.softcross.motikoc.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}

object Splash : Destination {
    override val route = "splash"
}

object Introduction : Destination {
    override val route = "introduction"
}

object Login : Destination {
    override val route = "login"
}

object Register : Destination {
    override val route = "register"
}

object JobWizard : Destination {
    override val route = "jobWizard"
}

object JobSelection : Destination {
    override val route = "jobSelection"
    val routeWithArgs = "jobSelection/{personalProperties}/{interests}/{abilities}/{area}/{identify}"
    val arguments = listOf(
        navArgument("personalProperties") { type = NavType.StringType },
        navArgument("interests") { type = NavType.StringType },
        navArgument("abilities") { type = NavType.StringType },
        navArgument("area") { type = NavType.StringType },
        navArgument("identify") { type = NavType.StringType },
    )
}

object JobAssistant : Destination {
    override val route = "jobAssistant"
    val routeWithArgs = "jobAssistant/{recommendedJobs}/{clickedJob}"
    val arguments = listOf(
        navArgument("recommendedJobs") { type = NavType.StringType },
        navArgument("clickedJob") { type = NavType.StringType },
    )
}

object Goals : Destination {
    override val route = "goals"
}

object Home : Destination {
    override val route = "home"
}

object Planner : Destination {
    override val route = "planner"
}

object Assignments : Destination {
    override val route = "assignments"
}

object Exams : Destination {
    override val route = "exams"
}
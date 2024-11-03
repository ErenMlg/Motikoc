package com.softcross.motikoc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.softcross.motikoc.presentation.assignments.AssignmentViewModel
import com.softcross.motikoc.presentation.assignments.Assignments
import com.softcross.motikoc.presentation.exams.Exams
import com.softcross.motikoc.presentation.exams.ExamsViewModel
import com.softcross.motikoc.presentation.goals.Goals
import com.softcross.motikoc.presentation.goals.GoalsViewModel
import com.softcross.motikoc.presentation.home.Home
import com.softcross.motikoc.presentation.home.HomeViewModel
import com.softcross.motikoc.presentation.introduction.IntroductionRoute
import com.softcross.motikoc.presentation.jobAssistant.AssistantJobItem
import com.softcross.motikoc.presentation.jobAssistant.JobAssistant
import com.softcross.motikoc.presentation.jobAssistant.JobAssistantViewModel
import com.softcross.motikoc.presentation.jobSelection.JobSelection
import com.softcross.motikoc.presentation.jobSelection.JobSelectionViewModel
import com.softcross.motikoc.presentation.jobWizard.JobWizard
import com.softcross.motikoc.presentation.jobWizard.JobWizardViewModel
import com.softcross.motikoc.presentation.login.LoginRoute
import com.softcross.motikoc.presentation.login.LoginViewModel
import com.softcross.motikoc.presentation.planner.Planner
import com.softcross.motikoc.presentation.planner.PlannerViewModel
import com.softcross.motikoc.presentation.register.RegisterRoute
import com.softcross.motikoc.presentation.register.RegisterViewModel
import com.softcross.motikoc.presentation.splash.Splash
import com.softcross.motikoc.presentation.splash.SplashViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MotikocNavigator(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Splash.route,
        modifier = modifier
    ) {
        composable(Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            Splash(
                uiEffect = uiEffect,
                navigateToMain = {
                    navHostController.navigate(Home.route) {
                        popUpTo(Splash.route) { inclusive = true }
                    }
                },
                navigateToIntroduce = {
                    navHostController.navigate(Introduction.route) {
                        popUpTo(Splash.route) { inclusive = true }
                    }
                },
                navigateJobWizard = {
                    navHostController.navigate(JobWizard.route) {
                        popUpTo(Splash.route) { inclusive = true }
                    }
                },
                navigateToGoals = {
                    navHostController.navigate(Goals.route) {
                        popUpTo(Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Introduction.route) {
            IntroductionRoute(
                onLoginClick = {
                    navHostController.navigate(Login.route)
                },
                onRegisterClick = {
                    navHostController.navigate(Register.route)
                }
            )
        }

        composable(Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LoginRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToRegister = {
                    navHostController.navigate(Register.route) {
                        popUpTo(Login.route) { inclusive = true }
                    }
                },
                navigateToJobWizard = {
                    navHostController.navigate(JobWizard.route) {
                        popUpTo(navHostController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                navigateToHome = {
                    navHostController.navigate(Home.route) {
                        popUpTo(Login.route) { inclusive = true }
                    }
                },
                navigateToGoals = {
                    navHostController.navigate(Goals.route) {
                        popUpTo(Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            RegisterRoute(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToLogin = {
                    navHostController.navigate(Login.route) {
                        popUpTo(Register.route) { inclusive = true }
                    }
                },
                navigateToJobWizard = {
                    navHostController.navigate("jobWizard") {
                        popUpTo(navHostController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                navigateToGoal = {
                    navHostController.navigate(Goals.route) {
                        popUpTo(Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(JobWizard.route) {
            val viewModel: JobWizardViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            JobWizard(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToJobSelection = { personalProperties, interests, abilities, area, identify ->
                    navHostController.navigate("${JobSelection.route}/$personalProperties/$interests/$abilities/$area/$identify") {
                        popUpTo(JobWizard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(JobSelection.routeWithArgs, arguments = JobSelection.arguments) {
            val viewModel: JobSelectionViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val jobNamesJson = URLEncoder.encode(Gson().toJson(uiState.jobRecommendList.map {
                AssistantJobItem(
                    it.name,
                    it.image
                )
            }), StandardCharsets.UTF_8.toString()).replace("+", "%20")
            JobSelection(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToAssistant = { clickedJob ->
                    navHostController.navigate("${JobAssistant.route}/$jobNamesJson/${clickedJob.name}")
                },
                navigateToGoals = {
                    navHostController.navigate(Goals.route) {
                        popUpTo(JobSelection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(JobAssistant.routeWithArgs, arguments = JobAssistant.arguments) {
            val viewModel: JobAssistantViewModel = hiltViewModel()
            val uiEffect = viewModel.uiEffect
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            JobAssistant(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToJobSelection = {
                    navHostController.popBackStack()
                },
                navigateToGoals = {
                    navHostController.navigate(Goals.route) {
                        popUpTo(JobAssistant.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Goals.route) {
            val viewModel: GoalsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            Goals(
                uiState = uiState,
                uiEffect = uiEffect,
                uiAction = viewModel::onAction,
                navigateToHome = {
                    navHostController.navigate(Home.route) {
                        popUpTo(Goals.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            Home(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                navigateToIntroduce = {
                    navHostController.navigate(Introduction.route) {
                        popUpTo(Home.route) { inclusive = true }
                    }
                },
                navigateToPlans = {
                    navHostController.navigate(Planner.route)
                }
            )
        }

        composable(Planner.route) {
            val viewModel: PlannerViewModel = hiltViewModel()
            val uiState by viewModel.plannerState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.plannerEffect
            Planner(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }

        composable(Assignments.route) {
            val viewModel: AssignmentViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            Assignments(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }

        composable(Exams.route) {
            val viewModel: ExamsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            Exams(
                uiState = uiState,
                uiEffect = uiEffect,
                onEvent = viewModel::onEvent
            )
        }

    }

}
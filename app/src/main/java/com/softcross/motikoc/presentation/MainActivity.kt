package com.softcross.motikoc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softcross.motikoc.navigation.Assignments
import com.softcross.motikoc.navigation.Exams
import com.softcross.motikoc.navigation.Home
import com.softcross.motikoc.navigation.MotikocNavigator
import com.softcross.motikoc.navigation.Planner
import com.softcross.motikoc.navigation.bottomNav.MotikocBottomNavigationBar
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route
            val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
            LaunchedEffect(currentRoute) {
                when (currentRoute) {
                    Home.route -> bottomBarState.value = true
                    Assignments.route -> bottomBarState.value = true
                    Exams.route -> bottomBarState.value = true
                    Planner.route -> bottomBarState.value = true
                    else -> bottomBarState.value = false
                }
            }

            MotikocTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MotikocBottomNavigationBar(
                            navController = navController,
                            bottomBarState = bottomBarState
                        )
                    }
                ) { innerPadding ->
                    MotikocNavigator(
                        navHostController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundColor)
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }
}

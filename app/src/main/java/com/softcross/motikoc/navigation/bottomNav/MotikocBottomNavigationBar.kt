package com.softcross.motikoc.navigation.bottomNav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.DarkBackground
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.PrimarySurface

@Composable
fun MotikocBottomNavigationBar(
    navController: NavController,
    bottomBarState: MutableState<Boolean>
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Planner,
        BottomNavItem.Exams,
        BottomNavItem.Assignments
    )

    AnimatedVisibility(visible = bottomBarState.value) {
        NavigationBar(
            containerColor = PrimarySurface,
            modifier = Modifier.height(90.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.route,
                            tint = if (currentRoute != item.route) {
                                DarkBackground
                            } else {
                                GoldColor
                            }
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldColor,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = BackgroundColor,
                    ),
                    alwaysShowLabel = false,
                    modifier = Modifier
                        .background(PrimarySurface)
                        .height(80.dp)
                )

            }

        }
    }
}
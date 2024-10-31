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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.softcross.motikoc.navigation.MotikocNavigator
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.MotikocTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotikocTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

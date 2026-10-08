package com.stockpos.terminal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stockpos.terminal.ui.theme.StockPOSTheme
import com.stockpos.terminal.ui.login.LoginScreen
import com.stockpos.terminal.ui.pos.PosScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockPOSTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = if (isLoggedIn) "pos" else "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn = true
                    navController.navigate("pos") { popUpTo("login") { inclusive = true } }
                }
            )
        }
        composable("pos") {
            PosScreen(
                onExit = {
                    isLoggedIn = false
                    navController.navigate("login") { popUpTo("pos") { inclusive = true } }
                }
            )
        }
    }
}

package com.rodolfo.hackaton.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodolfo.hackaton.feature.login.ui.LoginScreen
import com.rodolfo.hackaton.feature.login.ui.LoginViewModel
import com.rodolfo.hackaton.feature.home.ui.HomeScreen
import com.rodolfo.hackaton.feature.menu.MainScreen

object Destinations {
    const val LOGIN = "login"
    const val MAIN = "main"
    const val PURCHASE_DETAIL = "purchase_detail"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destinations.LOGIN) {
        composable(Destinations.LOGIN) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Destinations.MAIN) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Destinations.MAIN) {
            MainScreen()
        }
    }
}
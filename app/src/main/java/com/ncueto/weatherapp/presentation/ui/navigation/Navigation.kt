package com.ncueto.weatherapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ncueto.weatherapp.presentation.ui.screens.WeatherScreen
import com.ncueto.weatherapp.presentation.ui.screens.WelcomeScreen
import com.ncueto.weatherapp.presentation.viewmodel.WeatherViewModel

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Weather : Screen("weather")
}

@Composable
fun WeatherNavHost(
    navController: NavHostController,
    viewModel: WeatherViewModel,
    onRequestPermission: () -> Unit,
    onShareWeather: () -> Unit,
    hasLocationPermission: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStartClick = {
                    navController.navigate(Screen.Weather.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                    if (hasLocationPermission()) {
                        viewModel.onPermissionResult(true)
                    }
                }
            )
        }

        composable(Screen.Weather.route) {
            WeatherScreen(
                uiState = uiState,
                onRequestPermission = onRequestPermission,
                onRetry = { viewModel.refresh() },
                onSearchCity = { cityName -> viewModel.searchCity(cityName) },
                onSettingsClick = { viewModel.onSettingsClick() },
                onShareClick = { viewModel.onShareClick() },
                onLocationClick = { viewModel.loadWeather() }
            )
        }
    }
}
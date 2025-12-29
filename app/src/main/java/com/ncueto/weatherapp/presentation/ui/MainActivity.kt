package com.ncueto.weatherapp.presentation.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.compose.rememberNavController
import com.ncueto.weatherapp.presentation.ui.navigation.WeatherNavHost
import com.ncueto.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.ncueto.weatherapp.presentation.viewmodel.WeatherEvent
import com.ncueto.weatherapp.presentation.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModel()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        viewModel.onPermissionResult(fineLocationGranted || coarseLocationGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    viewModel.events.collect { event ->
                        when (event) {
                            is WeatherEvent.RequestLocationPermission -> requestLocationPermission()
                            is WeatherEvent.OpenLocationSettings -> openLocationSettings()
                            is WeatherEvent.OpenSettings -> {
                                Toast.makeText(this@MainActivity, "Configuración", Toast.LENGTH_SHORT).show()
                            }
                            is WeatherEvent.ShareWeather -> {
                                shareWeather()
                            }
                            else -> {}
                        }
                    }
                }
                WeatherNavHost(
                    navController = navController,
                    viewModel = viewModel,
                    onRequestPermission = { requestLocationPermission() },
                    onShareWeather = { shareWeather() },
                    hasLocationPermission = { hasLocationPermission() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PermissionChecker.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    private fun shareWeather() {
        val weather = viewModel.uiState.value.weather ?: return
        val shareText = buildString {
            append("🌤 Clima en ${weather.locationName}, ${weather.country}\n")
            append("🌡 Temperatura: ${weather.temperature.roundToInt()}°C\n")
            append("💨 Viento: ${(weather.windSpeed * 3.6).roundToInt()} km/h\n")
            append("💧 Humedad: ${weather.humidity}%\n")
            append("☁ Nubes: ${weather.clouds}%\n")
            append("📊 Presión: ${weather.pressure} hPa")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Compartir clima"))
    }
}

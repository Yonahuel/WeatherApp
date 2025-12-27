package com.ncueto.weatherapp.presentation.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.ncueto.weatherapp.presentation.ui.screens.WeatherScreen
import com.ncueto.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.ncueto.weatherapp.presentation.viewmodel.WeatherEvent
import com.ncueto.weatherapp.presentation.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.events.collect { event ->
                        when (event) {
                            is WeatherEvent.RequestLocationPermission -> requestLocationPermission()
                            is WeatherEvent.OpenLocationSettings -> openLocationSettings()
                            else -> {}
                        }
                    }
                }

                // Check permission on start
                LaunchedEffect(Unit) {
                    if (hasLocationPermission()) {
                        viewModel.onPermissionResult(true)
                    }
                }

                WeatherScreen(
                    uiState = uiState,
                    onRequestPermission = { requestLocationPermission() },
                    onRetry = { viewModel.refresh() },
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
}

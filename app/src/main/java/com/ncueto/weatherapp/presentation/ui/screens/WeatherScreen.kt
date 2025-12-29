package com.ncueto.weatherapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ncueto.weatherapp.domain.model.Weather
import com.ncueto.weatherapp.presentation.ui.components.ErrorState
import com.ncueto.weatherapp.presentation.ui.components.LoadingState
import com.ncueto.weatherapp.presentation.ui.components.LocationHeader
import com.ncueto.weatherapp.presentation.ui.components.MainWeatherDisplay
import com.ncueto.weatherapp.presentation.ui.components.PermissionRequestState
import com.ncueto.weatherapp.presentation.ui.components.SearchCityDialog
import com.ncueto.weatherapp.presentation.ui.components.SunriseSunsetCard
import com.ncueto.weatherapp.presentation.ui.components.WeatherIndicatorsRow
import com.ncueto.weatherapp.presentation.ui.components.WindCard
import com.ncueto.weatherapp.presentation.ui.theme.BackgroundBlue
import com.ncueto.weatherapp.presentation.viewmodel.WeatherUiState

@Composable
fun WeatherScreen(
    uiState: WeatherUiState,
    onRequestPermission: () -> Unit,
    onRetry: () -> Unit,
    onSearchCity: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onShareClick: () -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSearchDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlue)
    ) {
        when {
            uiState.shouldRequestPermission && !uiState.locationPermissionGranted -> {
                PermissionRequestState(onRequestPermission = onRequestPermission)
            }
            uiState.isLoading -> {
                LoadingState()
            }
            uiState.isError -> {
                ErrorState(
                    message = uiState.error ?: "Error desconocido",
                    onRetry = onRetry
                )
            }
            uiState.isSuccess && uiState.weather != null -> {
                WeatherContent(
                    weather = uiState.weather,
                    onSearchClick = { showSearchDialog = true },
                    onSettingsClick = onSettingsClick,
                    onShareClick = onShareClick,
                    onLocationClick = onLocationClick
                )
            }
        }

        if (showSearchDialog) {
            SearchCityDialog(
                onDismiss = { showSearchDialog = false },
                onSearch = { cityName ->
                    onSearchCity(cityName)
                }
            )
        }
    }
}

@Composable
private fun WeatherContent(
    weather: Weather,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onShareClick: () -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp, bottom = 24.dp)
    ) {
        LocationHeader(
            locationName = "${weather.locationName}, ${weather.country}",
            onSettingsClick = onSettingsClick,
            onShareClick = onShareClick,
            onSearchClick = onSearchClick,
            onLocationClick = onLocationClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        MainWeatherDisplay(
            temperature = weather.temperature,
            feelsLike = weather.feelsLike,
            description = weather.description,
            tempMin = weather.tempMin,
            tempMax = weather.tempMax,
            windSpeed = weather.windSpeed,
            rain = weather.rain,
            timestamp = weather.timestamp,
            timezone = weather.timezone,
            icon = weather.icon
        )

        Spacer(modifier = Modifier.height(20.dp))

        WeatherIndicatorsRow(
            pressure = weather.pressure,
            clouds = weather.clouds,
            humidity = weather.humidity
        )

        Spacer(modifier = Modifier.height(20.dp))

        WindCard(
            windSpeed = weather.windSpeed,
            windGust = weather.windGust,
            windDegree = weather.windDegree
        )

        Spacer(modifier = Modifier.height(32.dp))

        SunriseSunsetCard(
            sunrise = weather.sunrise,
            sunset = weather.sunset,
            timezone = weather.timezone
        )
    }
}

@Preview
@Composable
fun WeatherContentPreview() {
    WeatherContent(
        Weather(
            locationName = "Buenos Aires",
            country = "Argentina",
            temperature = 20.0,
            feelsLike = 18.5,
            description = "Descripción del clima",
            tempMin = 15.0,
            tempMax = 25.0,
            windSpeed = 5.0,
            rain = 0.0,
            timestamp = 1623456789,
            timezone = -10800,
            icon = "01d",
            pressure = 1013,
            clouds = 50,
            humidity = 60,
            sunrise = 1623478900,
            sunset = 1623521300,
            windDegree = 45,
            windGust = 8.0
        ),
        {},
        {},
        {},
        {}
    )
}
package com.ncueto.weatherapp.presentation.viewmodel

import com.ncueto.weatherapp.domain.model.Weather

data class WeatherUiState(
    val isLoading: Boolean = true,
    val weather: Weather? = null,
    val error: String? = null,
    val locationPermissionGranted: Boolean = false,
    val shouldRequestPermission: Boolean = true
) {
    val isSuccess: Boolean get() = weather != null && error == null && !isLoading
    val isError: Boolean get() = error != null && !isLoading
}

sealed class WeatherEvent {
    data object RequestLocationPermission : WeatherEvent()
    data object RefreshWeather : WeatherEvent()
    data object PermissionGranted : WeatherEvent()
    data object PermissionDenied : WeatherEvent()
    data object OpenLocationSettings : WeatherEvent()
}

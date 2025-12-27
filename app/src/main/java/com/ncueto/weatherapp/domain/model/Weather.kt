package com.ncueto.weatherapp.domain.model

data class Weather(
    val locationName: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windGust: Double?,
    val windDegree: Int,
    val clouds: Int,
    val rain: Double?,
    val description: String,
    val icon: String,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Int,
    val timestamp: Long
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

sealed class LocationError {
    data object PermissionDenied : LocationError()
    data object GpsDisabled : LocationError()
    data object LocationUnavailable : LocationError()
    data class Unknown(val message: String) : LocationError()
}

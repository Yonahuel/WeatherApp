package com.ncueto.weatherapp.domain.usecase

import com.ncueto.weatherapp.domain.model.Location
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.model.Weather
import com.ncueto.weatherapp.domain.repository.LocationRepository
import com.ncueto.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow


class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Weather> {
        return weatherRepository.getWeather(latitude, longitude)
    }
}

class GetWeatherByCityUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<Weather> {
        return weatherRepository.getWeatherByCity(cityName)
    }
}

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Result<Location>> {
        return locationRepository.getCurrentLocation()
    }
}

class IsLocationEnabledUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Boolean {
        return locationRepository.isLocationEnabled()
    }
}
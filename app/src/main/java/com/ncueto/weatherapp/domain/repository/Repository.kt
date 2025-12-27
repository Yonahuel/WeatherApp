package com.ncueto.weatherapp.domain.repository

import com.ncueto.weatherapp.domain.model.Location
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Result<Weather>
}

interface LocationRepository {
    fun getCurrentLocation(): Flow<Result<Location>>
    fun isLocationEnabled(): Boolean
}
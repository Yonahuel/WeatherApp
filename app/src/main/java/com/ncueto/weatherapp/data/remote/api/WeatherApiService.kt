package com.ncueto.weatherapp.data.remote.api

import com.ncueto.weatherapp.BuildConfig
import com.ncueto.weatherapp.data.remote.dto.WeatherResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface WeatherApiService {
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponseDto
}

class WeatherApiServiceImpl(
    private val client: HttpClient
) : WeatherApiService {

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
        private const val API_KEY = "3bb6ad9c1b317157813c4c20367d32ea"
    }

    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponseDto {
        return client.get("$BASE_URL/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", BuildConfig.WEATHER_API_KEY)
            parameter("units", "metric")
            parameter("lang", "es")
        }.body()
    }
}
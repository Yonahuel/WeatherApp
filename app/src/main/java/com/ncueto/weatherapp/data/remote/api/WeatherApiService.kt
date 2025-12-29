package com.ncueto.weatherapp.data.remote.api

import com.ncueto.weatherapp.BuildConfig
import com.ncueto.weatherapp.data.remote.dto.WeatherResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface WeatherApiService {
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponseDto
    suspend fun getWeatherByCity(cityName: String): WeatherResponseDto
}

class WeatherApiServiceImpl(
    private val client: HttpClient
) : WeatherApiService {

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
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

    override suspend fun getWeatherByCity(cityName: String): WeatherResponseDto {
        return client.get("$BASE_URL/weather") {
            parameter("q", cityName)
            parameter("appid", BuildConfig.WEATHER_API_KEY)
            parameter("units", "metric")
            parameter("lang", "es")
        }.body()
    }
}
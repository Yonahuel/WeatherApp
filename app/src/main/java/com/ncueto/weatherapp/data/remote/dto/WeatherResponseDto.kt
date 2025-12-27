package com.ncueto.weatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDto(
    @SerialName("coord") val coord: CoordDto,
    @SerialName("weather") val weather: List<WeatherDto>,
    @SerialName("base") val base: String,
    @SerialName("main") val main: MainDto,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind") val wind: WindDto,
    @SerialName("rain") val rain: RainDto? = null,
    @SerialName("clouds") val clouds: CloudsDto,
    @SerialName("dt") val dt: Long,
    @SerialName("sys") val sys: SysDto,
    @SerialName("timezone") val timezone: Int,
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("cod") val cod: Int
)

@Serializable
data class CoordDto(
    @SerialName("lon") val lon: Double,
    @SerialName("lat") val lat: Double
)

@Serializable
data class WeatherDto(
    @SerialName("id") val id: Int,
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class MainDto(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double,
    @SerialName("pressure") val pressure: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("sea_level") val seaLevel: Int? = null,
    @SerialName("grnd_level") val grndLevel: Int? = null
)

@Serializable
data class WindDto(
    @SerialName("speed") val speed: Double,
    @SerialName("deg") val deg: Int,
    @SerialName("gust") val gust: Double? = null
)

@Serializable
data class RainDto(
    @SerialName("1h") val oneHour: Double? = null,
    @SerialName("3h") val threeHours: Double? = null
)

@Serializable
data class CloudsDto(
    @SerialName("all") val all: Int
)

@Serializable
data class SysDto(
    @SerialName("type") val type: Int? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("country") val country: String,
    @SerialName("sunrise") val sunrise: Long,
    @SerialName("sunset") val sunset: Long
)

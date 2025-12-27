package com.ncueto.weatherapp.data.remote.dto

import com.ncueto.weatherapp.domain.model.Weather

fun WeatherResponseDto.toDomain(): Weather {
    return Weather(
        locationName = name,
        country = sys.country,
        temperature = main.temp,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        humidity = main.humidity,
        pressure = main.pressure,
        windSpeed = wind.speed,
        windGust = wind.gust,
        windDegree = wind.deg,
        clouds = clouds.all,
        rain = rain?.oneHour ?: rain?.threeHours,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        timezone = timezone,
        timestamp = dt
    )
}
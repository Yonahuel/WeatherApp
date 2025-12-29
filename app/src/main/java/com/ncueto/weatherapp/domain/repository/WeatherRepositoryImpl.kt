package com.ncueto.weatherapp.domain.repository

import com.ncueto.weatherapp.data.remote.api.WeatherApiService
import com.ncueto.weatherapp.data.remote.dto.toDomain
import com.ncueto.weatherapp.domain.model.AppException
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.model.Weather
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService
): WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double): Result<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getWeather(latitude, longitude)
                Result.Success(response.toDomain())
            } catch (e: UnknownHostException) {
                Result.Error(AppException.NetworkError)
            } catch (e: ClientRequestException) {
                Result.Error(AppException.ServerError)
            } catch (e: ServerResponseException) {
                Result.Error(AppException.ServerError)
            } catch (e: Exception) {
                Result.Error(AppException.Unknown(e.message ?: "Error desconocido"))
            }
        }
    }

    override suspend fun getWeatherByCity(cityName: String): Result<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getWeatherByCity(cityName)
                Result.Success(response.toDomain())
            } catch (e: UnknownHostException) {
                Result.Error(AppException.NetworkError)
            } catch (e: ClientRequestException) {
                Result.Error(AppException.CityNotFound)
            } catch (e: ServerResponseException) {
                Result.Error(AppException.ServerError)
            } catch (e: Exception) {
                Result.Error(AppException.Unknown(e.message ?: "Error desconocido"))
            }
        }
    }
}
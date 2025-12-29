package com.ncueto.weatherapp.domain.usecase

import com.ncueto.weatherapp.domain.model.AppException
import com.ncueto.weatherapp.domain.model.Weather
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Before

class GetWeatherUseCaseTest {
    @MockK
    lateinit var weatherRepository: WeatherRepository
    @InjectMockKs
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getWeatherUseCase = GetWeatherUseCase(weatherRepository)
    }

    @Test
    fun `invoke calls repository with correct coordinates`() = runTest {
        // Given
        val latitude = -34.6037
        val longitude = -58.3816
        val expectedWeather = createMockWeather()
        coEvery { weatherRepository.getWeather(latitude, longitude) } returns Result.Success(expectedWeather)

        // When
        getWeatherUseCase(latitude, longitude)

        // Then
        coVerify(exactly = 1) { weatherRepository.getWeather(latitude, longitude) }
    }

    @Test
    fun `invoke returns Success when repository succeeds`() = runTest {
        // Given
        val expectedWeather = createMockWeather()
        coEvery { weatherRepository.getWeather(any(), any()) } returns Result.Success(expectedWeather)

        // When
        val result = getWeatherUseCase(-34.6037, -58.3816)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedWeather, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        // Given
        coEvery { weatherRepository.getWeather(any(), any()) } returns Result.Error(AppException.NetworkError)

        // When
        val result = getWeatherUseCase(-34.6037, -58.3816)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(AppException.NetworkError, (result as Result.Error).exception)
    }

    private fun createMockWeather(): Weather {
        return Weather(
            locationName = "Buenos Aires",
            country = "AR",
            temperature = 25.0,
            feelsLike = 26.0,
            tempMin = 22.0,
            tempMax = 28.0,
            humidity = 65,
            pressure = 1013,
            windSpeed = 3.5,
            windGust = 5.0,
            windDegree = 180,
            clouds = 10,
            rain = null,
            description = "cielo claro",
            icon = "01d",
            sunrise = 1699950000,
            sunset = 1699995000,
            timezone = -10800,
            timestamp = 1699999999
        )
    }
}
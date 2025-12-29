package com.ncueto.weatherapp.data.repository

import com.ncueto.weatherapp.data.remote.api.WeatherApiService
import com.ncueto.weatherapp.data.remote.dto.CloudsDto
import com.ncueto.weatherapp.data.remote.dto.CoordDto
import com.ncueto.weatherapp.data.remote.dto.MainDto
import com.ncueto.weatherapp.data.remote.dto.SysDto
import com.ncueto.weatherapp.data.remote.dto.WeatherDto
import com.ncueto.weatherapp.data.remote.dto.WeatherResponseDto
import com.ncueto.weatherapp.data.remote.dto.WindDto
import com.ncueto.weatherapp.domain.model.AppException
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.repository.WeatherRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class WeatherRepositoryImplTest {
    @MockK(relaxed = true)
    lateinit var apiService: WeatherApiService
    @InjectMockKs
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getWeather returns Success when API call succeeds`() = runTest {
        // Given
        val expectedResponse = createMockWeatherResponse()
        coEvery { apiService.getWeather(any(), any()) } returns expectedResponse

        // When
        val result = repository.getWeather(-34.6037, -58.3816)

        // Then
        assertTrue(result is Result.Success)
        val weather = (result as Result.Success).data
        assertEquals("Buenos Aires", weather.locationName)
        assertEquals("AR", weather.country)
        assertEquals(25.0, weather.temperature, 0.01)
    }

    @Test
    fun `getWeather returns NetworkError when UnknownHostException is thrown`() = runTest {
        // Given
        coEvery { apiService.getWeather(any(), any()) } throws UnknownHostException()

        // When
        val result = repository.getWeather(-34.6037, -58.3816)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(AppException.NetworkError, (result as Result.Error).exception)
    }

    @Test
    fun `getWeather returns Unknown error for unexpected exceptions`() = runTest {
        // Given
        coEvery { apiService.getWeather(any(), any()) } throws RuntimeException("Unexpected error")

        // When
        val result = repository.getWeather(-34.6037, -58.3816)

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is AppException.Unknown)
    }

    private fun createMockWeatherResponse(): WeatherResponseDto {
        return WeatherResponseDto(
            coord = CoordDto(lon = -58.3816, lat = -34.6037),
            weather = listOf(
                WeatherDto(id = 800, main = "Clear", description = "cielo claro", icon = "01d")
            ),
            base = "stations",
            main = MainDto(
                temp = 25.0,
                feelsLike = 26.0,
                tempMin = 22.0,
                tempMax = 28.0,
                pressure = 1013,
                humidity = 65
            ),
            visibility = 10000,
            wind = WindDto(speed = 3.5, deg = 180, gust = 5.0),
            rain = null,
            clouds = CloudsDto(all = 10),
            dt = 1699999999,
            sys = SysDto(
                type = 1,
                id = 1234,
                country = "AR",
                sunrise = 1699950000,
                sunset = 1699995000
            ),
            timezone = -10800,
            id = 3435910,
            name = "Buenos Aires",
            cod = 200
        )
    }
}
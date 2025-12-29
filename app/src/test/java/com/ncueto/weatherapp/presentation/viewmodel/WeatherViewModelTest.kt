package com.ncueto.weatherapp.presentation.viewmodel

import app.cash.turbine.test
import com.ncueto.weatherapp.domain.model.AppException
import com.ncueto.weatherapp.domain.model.Location
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.model.Weather
import com.ncueto.weatherapp.domain.usecase.GetCurrentLocationUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherByCityUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherUseCase
import com.ncueto.weatherapp.domain.usecase.IsLocationEnabledUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase
    @MockK
    lateinit var getWeatherByCityUseCase: GetWeatherByCityUseCase
    @MockK
    lateinit var getCurrentLocationUseCase: GetCurrentLocationUseCase
    @MockK
    lateinit var isLocationEnabledUseCase: IsLocationEnabledUseCase
    @InjectMockKs
    lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        viewModel.cancelAutoRefresh()
    }

    @Test
    fun `initial state is loading with permission request`() = runTest {
        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
        assertTrue(state.shouldRequestPermission)
        assertNull(state.weather)
        assertNull(state.error)
    }

    @Test
    fun `onPermissionResult with granted true updates state and loads weather`() = runTest {
        // Given
        val mockWeather = createMockWeather()
        every { isLocationEnabledUseCase() } returns true
        every { getCurrentLocationUseCase() } returns flowOf(
            Result.Success(Location(-34.6037, -58.3816))
        )
        coEvery { getWeatherUseCase(any(), any()) } returns Result.Success(mockWeather)

        // When
        viewModel.onPermissionResult(true)
        viewModel.cancelAutoRefresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.locationPermissionGranted)
        assertFalse(state.shouldRequestPermission)
        assertFalse(state.isLoading)
        assertNotNull(state.weather)
        assertEquals("Buenos Aires", state.weather?.locationName)
    }

    @Test
    fun `onPermissionResult with granted false shows error`() = runTest {
        // When
        viewModel.onPermissionResult(false)
        //testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.locationPermissionGranted)
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("Permiso"))
    }

    @Test
    fun `loadWeather with disabled location shows error`() = runTest {
        // Given
        every { isLocationEnabledUseCase() } returns false

        viewModel.onPermissionResult(true)

        // When
        viewModel.loadWeather()
        viewModel.cancelAutoRefresh()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.error)
            assertTrue(state.error!!.contains("GPS"))
        }
    }

    @Test
    fun `loadWeather with network error shows error state`() = runTest {
        // Given
        every { isLocationEnabledUseCase() } returns true
        every { getCurrentLocationUseCase() } returns flowOf(
            Result.Success(Location(-34.6037, -58.3816))
        )
        coEvery { getWeatherUseCase(any(), any()) } returns Result.Error(AppException.NetworkError)

        viewModel.onPermissionResult(true)

        // When
        viewModel.cancelAutoRefresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertNull(state.weather)
    }

    @Test
    fun `loadWeather success updates state with weather data`() = runTest {
        // Given
        val mockWeather = createMockWeather()
        every { isLocationEnabledUseCase() } returns true
        every { getCurrentLocationUseCase() } returns flowOf(
            Result.Success(Location(-34.6037, -58.3816))
        )
        coEvery { getWeatherUseCase(any(), any()) } returns Result.Success(mockWeather)

        viewModel.onPermissionResult(true)

        // When
        viewModel.cancelAutoRefresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.weather)
        assertEquals(25.0, state.weather?.temperature ?: 0.0, 0.01)
        assertEquals(65, state.weather?.humidity)
    }

    @Test
    fun `refresh calls loadWeather when permission granted`() = runTest {
        // Given
        val mockWeather = createMockWeather()
        every { isLocationEnabledUseCase() } returns true
        every { getCurrentLocationUseCase() } returns flowOf(
            Result.Success(Location(-34.6037, -58.3816))
        )
        coEvery { getWeatherUseCase(any(), any()) } returns Result.Success(mockWeather)

        viewModel.onPermissionResult(true)

        // When
        viewModel.refresh()
        viewModel.cancelAutoRefresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertNotNull(state.weather)
    }

    @Test
    fun `searchCity with empty string does nothing`() = runTest {
        // Given
        val initialState = viewModel.uiState.value

        // When
        viewModel.searchCity("")

        // Then - state should remain unchanged (still initial loading state)
        val state = viewModel.uiState.value
        assertEquals(initialState.weather, state.weather)
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

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
): TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}